package cmsz.autoflow.engine.schedule;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.entity.Plan;
import cmsz.autoflow.engine.helper.ELHelper;
import cmsz.autoflow.engine.helper.JsonHelper;

public class PlanScheduler {
	
	private static Logger logger = LoggerFactory.getLogger(PlanScheduler.class);

	private AutoEngine engine;

	public AutoEngine getEngine() {
		return engine;
	}

	public void setEngine(AutoEngine engine) {
		this.engine = engine;
	}

	private static SchedulerFactory sf = new StdSchedulerFactory();
	private static String JOB_GROUP = "DEFAULT";
	private static String TRIGGER_GROUP = "DEFAULT";
	private static String DEFAULT_ENGINE = "Default_Engine";

	public void addPlan(Plan plan) throws SchedulerException, ParseException {
		
		/*  原有代码 quartz 1.8.5 API
		Scheduler sch = sf.getScheduler();
		JobDetail jobdetail = new JobDetail(plan.getId(), JOB_GROUP,
				PlanJob.class);
		jobdetail.getJobDataMap().put(plan.getId(), plan);
		jobdetail.getJobDataMap().put(DEFAULT_ENGINE, engine);
		CronTrigger trigger = new CronTrigger(plan.getId(), TRIGGER_GROUP,
				plan.getCron());
		sch.scheduleJob(jobdetail, trigger);
		sch.start(); 
		*/
		
		//下面是用quartz 2.2.2 API 代码。修改原因：原有代码与新版版本API不兼容。 yaoQingCan 2015-11-25
        JobDetail jobDetail = JobBuilder.newJob(PlanJob.class).withIdentity(plan.getId(), JOB_GROUP).build();
        jobDetail.getJobDataMap().put(plan.getId(), plan);
        jobDetail.getJobDataMap().put(DEFAULT_ENGINE, engine);

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(plan.getId(),TRIGGER_GROUP).withSchedule(CronScheduleBuilder.cronSchedule(plan.getCron())) .build();
         
        Scheduler sch = sf.getScheduler();
        sch.scheduleJob(jobDetail, trigger);
        sch.start(); 
        
	}

	public static class PlanJob implements Job {

		private static Logger logger = LoggerFactory.getLogger(PlanJob.class);

		@Override 
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
             
			/* 此行代码为原有代码 quartz 1.8.5 API
			Plan plan = (Plan) context.getJobDetail().getJobDataMap()
					.get(context.getJobDetail().getName());
             */
			
			// 下面是用quartz 2.2.2 API 代码。修改原因：原有代码与新版版本API不兼容。 yaoQingCan 2015-11-25
			Plan plan = (Plan) context.getJobDetail().getJobDataMap()
					.get(context.getJobDetail().getKey().getName());
			
			//Variables中存储的是本次对账的时间、省份、业务线
			String var = ELHelper.reassemble(plan.getVariables());

			if (!JsonHelper.validate(var)) {
				logger.error("variables is not a json string : {} ", var);
				return;
			}

			Map<String, Object> args = JsonHelper.fromJson(var, Map.class);
			AutoEngine engine = (AutoEngine) context.getJobDetail()
					.getJobDataMap().get(DEFAULT_ENGINE);
			try {
				logger.info("Scheduler start plan : {}", plan);
				//启动引擎；startInstanceById为引擎的入口函数
				engine.startInstanceById(plan.getProcessId(), args, null,
						plan.getFlowName());

			} catch (Exception e) {
				logger.error("start plan failed :"+plan, e);
			}

		}

	}

	public void start() {
		//取了AF_SCHEDULE表的所有数据，一行记录代表某个省某次对账
		List<Plan> list = engine.plan().getAllPlans();
		for (Plan p : list) {
			try {
				logger.info("Add Plan : {}", p);
				//把Plan放到quartz的一个定时任务中
				addPlan(p);
			} catch (SchedulerException | ParseException e) {
				logger.error("Exception happend whend add plan :"+p, e);
			}
		}
	}
}

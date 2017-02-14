package cmsz.autoflow.engine.core;

import java.util.List;

import cmsz.autoflow.engine.IPlanService;
import cmsz.autoflow.engine.entity.Plan;

public class PlanService extends AccessService implements IPlanService {

	@Override
	public List<Plan> getAllPlans() {
		return access().getAllPlan();
	}
	

}

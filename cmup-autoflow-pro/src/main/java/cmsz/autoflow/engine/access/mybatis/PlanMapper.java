package cmsz.autoflow.engine.access.mybatis;

import java.util.List;

import cmsz.autoflow.engine.entity.Plan;

public interface PlanMapper {
	List<Plan>	selectAll();
}

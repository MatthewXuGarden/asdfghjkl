package com.carel.supervisor.report;

import java.util.Map;

import com.carel.supervisor.base.factory.FactoryObject;

public class ReportFactory {
	public static Report buildReport(String classname, Map<String, Object> parameters) throws Exception {
		Report report = (Report) FactoryObject.newInstance(classname, null, null);
		report.setParameters(parameters);
		return report;
	}
}

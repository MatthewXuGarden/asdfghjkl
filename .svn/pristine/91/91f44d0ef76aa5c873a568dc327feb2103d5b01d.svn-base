package com.carel.supervisor.report.bean.test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import com.carel.supervisor.report.bean.HistoryReportBean;

public class FactoryHistory {

	public FactoryHistory() {
	}

	public static Collection<HistoryReportBean> createBeanCollection() {
		Random r = new Random();
		String[] values = new String[15];
		LinkedList<HistoryReportBean> ll = new LinkedList<HistoryReportBean>();
		for (int i = 0; i < 100; i++) {
			
			for(int j=0;j<15;j++){
				values[j] = ""+r.nextFloat();
			}
			ll.add(new HistoryReportBean(new Timestamp(System.currentTimeMillis()), values));			
		}
		return ll;
	}
}

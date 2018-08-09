package com.carel.supervisor.report.bean.test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import com.carel.supervisor.report.bean.Report15FBean;

public class Factory15Bean {

	public Factory15Bean() {
	}

	public static Collection<Report15FBean> createBeanCollection() {
		Random r = new Random();
		LinkedList<Report15FBean> ll = new LinkedList<Report15FBean>();
		for (int i = 0; i < 100; i++) {
			ll.add(new Report15FBean(
					new Timestamp(System.currentTimeMillis()+(60000l*i)), 
					""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(),
					""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(),
					""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat(), ""+r.nextFloat()));			
		}
		return ll;
	}
}

package com.carel.supervisor.report.bean.test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import com.carel.supervisor.report.bean.VarValueBean;

public class FactoryInstant {

	public FactoryInstant() {
	}

	public static Collection<VarValueBean> createBeanCollection() {
		Random r = new Random();
		LinkedList<VarValueBean> ll = new LinkedList<VarValueBean>();
		for (int i = 0; i < 100; i++) {
			ll.add(new VarValueBean("device","variable",""+r.nextFloat(), new Timestamp(System.currentTimeMillis())));
		}
		return ll;
	}
}

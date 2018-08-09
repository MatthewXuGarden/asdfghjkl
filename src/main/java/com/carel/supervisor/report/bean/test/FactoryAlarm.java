package com.carel.supervisor.report.bean.test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

import com.carel.supervisor.report.bean.AlarmBean;

public class FactoryAlarm {

	public FactoryAlarm() {
	}

	public static Collection<AlarmBean> createBeanCollection() {
		LinkedList<AlarmBean> ll = new LinkedList<AlarmBean>();
		ll.add(new AlarmBean("start", new Timestamp(System.currentTimeMillis()), "d1", "a1"));
		ll.add(new AlarmBean("start", new Timestamp(System.currentTimeMillis()), "d2", "a1"));
		ll.add(new AlarmBean("stop", new Timestamp(System.currentTimeMillis()), "d3", "a1"));
		ll.add(new AlarmBean("stop", new Timestamp(System.currentTimeMillis()), "d3", "a2"));
		ll.add(new AlarmBean("start", new Timestamp(System.currentTimeMillis()), "d3", "a3"));
		ll.add(new AlarmBean("start", new Timestamp(System.currentTimeMillis()), "d3", "a3"));
		ll.add(new AlarmBean("start", new Timestamp(System.currentTimeMillis()), "d3", "a3"));
		return ll;
	}
}

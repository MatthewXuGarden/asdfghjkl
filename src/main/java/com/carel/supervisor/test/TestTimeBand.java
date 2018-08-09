package com.carel.supervisor.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.controller.time.TimeValidityFactory;
import java.util.*;


public class TestTimeBand
{
	private TestTimeBand()
    {
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        Integer idSite = new Integer(1);
        TimeBandList timebandList = new TimeBandList(null,
                BaseConfig.getPlantId(), idSite);
        TimeBandBean timeBandBean = null;
        TimeValidity timeValidity = null;
        Integer[] ids = timebandList.getIds();
        Map map = new HashMap();
/*
        for (int i = 0; i < ids.length; i++)
        {
            timeBandBean = timebandList.get(ids[i]);
            timeValidity = TimeValidityFactory.createTime(timeBandBean);
            map.put(ids[i], timeValidity);
        }
*/
       
        /*
        Test:
        HourChain
        //13:30-14:30;15:30-18:30

         test dell'ora:
         13:00        false
         13:30  true
         14:00  true
         14:30  true
         14:31  false
         15:00  false
         15:30  true
         17:00  true
         18:30  true
         24:00  false
         */
        TimeBandBean time = new TimeBandBean(new Integer(1), "", null, TimeBandBean.DAYLY,
        		"13:30-14:30;15:30-18:30",false);
        timeValidity = TimeValidityFactory.createTime(time);
        System.out.println(timeValidity.getData().toString());
        Date now = new Date();

        now = DateUtils.setTime(now, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 13, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 14, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 14, 31, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 15, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 17, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 18, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.setTime(now, 24, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        /*
          WeekValidity Lun, Mar, Dom
        ////  1+2|13:30-14:30;15:30-18:30,7|13:30-14:30;15:30-18:30

        test giorno:
        1 13:30  true
        1 14:00  true
        1 19:00  false
        2 13:30  true
        2 14:00  true
        2 19:00  false
        3 12:30  false
        3 14:00  false
        7 18:00  true
        7 19:00  false
        7 12:00  false
        7 14:00  true
        */
        time = new TimeBandBean(new Integer(1), "", null, TimeBandBean.WEEKLY,
        		"1+2|13:30-14:30;15:30-18:30,7|13:30-14:30;15:30-18:30",false);
        timeValidity = TimeValidityFactory.createTime(time);
        System.out.println(timeValidity.getData().toString());
        now = DateUtils.createDayWeek(1, 13, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(2, 13, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(2, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(2, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(3, 12, 30, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(3, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(7, 18, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(7, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(7, 12, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayWeek(7, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        /*

         MonthValidity
        ////  1+10|13:30-14:30;15:30-18:30,13|13:30-14:30;15:30-18:30

        test giorno:
        1 13:30  true
        1 14:00  true
        1 19:00  false
        4 13:30  true
        4 14:00  true
        4 19:00  false
        10 18:00  true
        10 19:00  false
        13 12:00  false
        13 14:00  true
        11 12:00  false
        11 14:00  false
        14 12:00  false
        14 14:00         false

                */
        time = new TimeBandBean(new Integer(1), "", null, TimeBandBean.MONTHLY,
        		"1+4+10|13:30-14:30;15:30-18:30,13|13:30-14:30;15:30-18:30",false);
        timeValidity = TimeValidityFactory.createTime(time);
        System.out.println(timeValidity.getData().toString());
        now = DateUtils.createDayMonth(1, 13, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(4, 13, 30, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(4, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(4, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(10, 18, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(10, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(13, 12, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(13, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(11, 12, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(11, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(14, 12, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayMonth(14, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        /*
        YearValidity
        //  1/10/2006|13:30-14:30;15:30-18:30


        1/10/2006 13:00  false
        1/10/2006 14:00  true
        1/10/2006 15:00  false
        1/10/2006 16:00  true
        1/10/2006 19:00  false
        1/10/2007 13:00  false
        1/10/2007 14:00  false
        1/10/2007 15:00  false
        1/10/2007 16:00  false
        1/10/2007 19:00  false
        1/9/2006 13:00  false
        1/9/2006 14:00  false
        1/9/2006 15:00  false
        1/9/2006 16:00  false
        1/9/2006 19:00  false
        
        2/10/2006 13:00  false
        2/10/2006 14:00  true
        2/10/2006 15:00  false
        2/10/2006 16:00  true
        2/10/2006 19:00  false
        
        7/10/2006 13:00  false
        7/10/2006 14:00  false
        7/10/2006 15:00  false
        7/10/2006 16:00  false
        7/10/2006 19:00  false
        
        8/10/2006 13:00  false
        8/10/2006 14:00  true
        8/10/2006 15:00  false
        8/10/2006 16:00  true
        8/10/2006 19:00  false
        
        */
        time = new TimeBandBean(new Integer(1), "", null, TimeBandBean.YEAR_ONE_SHOT,
        		"1/10/2006-5/10/2006|13:30-14:30;15:30-18:30,8/10/2006|13:30-14:30;15:30-18:30",false);
        timeValidity = TimeValidityFactory.createTime(time);
        System.out.println(timeValidity.getData().toString());
        now = DateUtils.createDayYear(2006, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 16, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 16, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }
        
        
        //________________________________
        
        now = DateUtils.createDayYear(2006, 10, 2, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 2, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 2, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 2, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 2, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }
        
        //66666666
        
        now = DateUtils.createDayYear(2006, 10, 7, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 7, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 7, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 7, 16, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 7, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }
        //+++++++++
        
        now = DateUtils.createDayYear(2006, 10, 8, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 8, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 8, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 8, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 8, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }
        
        //________________________________
        /*
         //  1/10|13:30-14:30;15:30-18:30

         1/10/2006 13:00  false
         1/10/2006 14:00  true
         1/10/2006 15:00  false
         1/10/2006 16:00  true
         1/10/2006 19:00  false
         1/10/2007 13:00  false
         1/10/2007 14:00  true
         1/10/2007 15:00  false
         1/10/2007 16:00  true
         1/10/2007 19:00  false
         1/9/2006 13:00  false
         1/9/2006 14:00  false
         1/9/2006 15:00  false
         1/9/2006 16:00  false
         1/9/2006 19:00  false
         *
         */
        time = new TimeBandBean(new Integer(1), "", null, TimeBandBean.YEAR_REPEAT,
        		"1/10|13:30-14:30;15:30-18:30,11/10|13:30-14:30;15:30-18:30",true);
        timeValidity = TimeValidityFactory.createTime(time);
        System.out.println(timeValidity.getData().toString());
        now = DateUtils.createDayYear(2006, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 14, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 16, 0, 0);

        if (!timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2007, 10, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 10, 1, 13, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 14, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 15, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 16, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }

        now = DateUtils.createDayYear(2006, 9, 1, 19, 0, 0);

        if (timeValidity.isValid(now))
        {
            throw new TestException();
        }
    }
}

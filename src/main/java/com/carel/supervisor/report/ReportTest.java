package com.carel.supervisor.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.director.graph.GraphConstant;


public class ReportTest
{
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

//        ArrayList[] series = null;
        long init;
        long end;
        long endTime = new GregorianCalendar(2006, GregorianCalendar.JUNE,
                10, 0, 30, 0).getTimeInMillis();
        init = System.currentTimeMillis();

        Object[] info = new Object[] { new Integer(1), new Integer(1) }; //idsite,idvariable
        ReportInformation reportInformation = new ReportInformation(10);
        reportInformation.enqueRecord(info);
        reportInformation.setEndTime(new Timestamp(endTime));
        reportInformation.setReportType(GraphConstant.TYPE_HACCP);

        ReportRequest reportRequest = new ReportRequest(reportInformation);
        reportRequest.startRetrieve();
        PaddingReport paddingReport= new PaddingReport(reportRequest);
        paddingReport.startPadding();

        end = System.currentTimeMillis();
        System.out.println("Time = " + (end - init));
        

        File fileOutput = new File("C:\\prova.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
        PrintStream printStream = new PrintStream(fileOutputStream);
        printStream.println(paddingReport);
        System.out.println(paddingReport);
        
    } //main
} //Class ReportTest

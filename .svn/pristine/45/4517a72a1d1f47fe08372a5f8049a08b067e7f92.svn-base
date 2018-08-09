package com.carel.supervisor.director;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.function.Function;
import com.carel.supervisor.field.Variable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;



public class FunctionHistorical
{
    private static FunctionHistorical me = new FunctionHistorical();
    private static String basePath = BaseConfig.getCarelPath();
    private String DIR_SAVE = "fhs";
    private PrintWriter printWriter = null;
    private long timeNow = 0;
    private String openFileName = null; //file attualmente in uso
    private Logger logger = logger = LoggerMgr.getLogger(this.getClass());

    private FunctionHistorical()
    {
    } //FunctionHistorical

    public static FunctionHistorical getInstance()
    {
        return me;
    } //getInstance

    public synchronized void write(long time, Variable variable)
    {
        timeNow = time;

        try
        {
            this.checkFile();
        }
        catch (IOException e)
        {
            logger.error(e);
        }

        String str = getTime() + getExpression(variable);
        printWriter.println(str);

        //System.out.print("Nel File: "+openFileName+" "+str);
    } //write

    public void flush()
    {
        if (printWriter != null)
            printWriter.flush();
    }

    private String getTime()
    {
        StringBuffer time = new StringBuffer();
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeNow);

        //String year = String.valueOf((calendar.get(Calendar.YEAR) - 2000));
        //String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        //String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        //time.append((year.length() == 1) ? ("0" + year) : year);
        //time.append((month.length() == 1) ? ("0" + month) : month);
        //time.append((day.length() == 1) ? ("0" + day) : day);
        time.append((hour.length() == 1) ? ("0" + hour) : hour);
        time.append((minute.length() == 1) ? ("0" + minute) : minute);
        time.append((second.length() == 1) ? ("0" + second) : second);
        time.append(";");

        return time.toString();
    } //getTime

    private String getExpression(Variable variable)
    {
        StringBuffer expression = new StringBuffer();
        expression.append(variable.getInfo().getId());
        expression.append(";");
        NumberFormat formatter = new DecimalFormat(".##");
        expression.append(formatter.format(variable.getCurrentValue()).replace(",","."));
        expression.append(";");

        HashMap map = ((Function) variable.getRetriever()).getVariableComposeFormula();
        Object[] objects = map.keySet().toArray();

        for (int j = 0; j < objects.length; j++)
        {
            expression.append(objects[j]);
            expression.append(";");
            expression.append(map.get(objects[j]));
            expression.append(";");
        } //for

        return expression.toString();
    } //getExpression

    private void checkFile() throws IOException
    {
        StringBuffer fileName = new StringBuffer();
        fileName.append(basePath);
        fileName.append(DIR_SAVE);
        fileName.append(File.separator);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeNow);

        String year = String.valueOf((calendar.get(Calendar.YEAR)));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        fileName.append((day.length() == 1) ? ("0" + day) : day);
        fileName.append((month.length() == 1) ? ("0" + month) : month);
        fileName.append((year.length() == 1) ? ("0" + year) : year);
        fileName.append(".fhs");

        if (openFileName == null)
        {
            openFileName = fileName.toString();
            printWriter = new PrintWriter(new BufferedWriter(
                        new FileWriter(openFileName, true)));

            return;
        } //if

        if (!openFileName.equals(fileName.toString()))
        {
            openFileName = fileName.toString();
            printWriter.flush();
            printWriter.close();
            printWriter = new PrintWriter(new BufferedWriter(
                        new FileWriter(openFileName, true)));

            return;
        } //if
    } //checkFile
} //Class FunctionHistorical

package com.carel.supervisor.base.test;

import com.carel.supervisor.base.excel.DataExcel;
import com.carel.supervisor.base.excel.Export2Excel;
import junit.framework.TestCase;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;


public class Export2ExcelTest extends TestCase
{
    private final static int ROW = 100;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(Export2ExcelTest.class);
    }

    public void testCreateExcelFile() throws Throwable
    {
        String[] titoli = { "A", "B", "C", "D", "E", "F", "G", "H" };
        Object[][] objects = null;

        objects = new Object[ROW][];

        for (int i = 0; i < ROW; i++)
        {
            objects[i] = new Object[titoli.length];

            for (int j = 0; j < titoli.length; j++)
            {
                objects[i][j] = new Integer(i * j);
            }
        } //for

        DataExcel dataExcel = new DataExcel("titolo", titoli, objects);
        ByteArrayOutputStream o = Export2Excel.createExcelFile(dataExcel);
        FileOutputStream file = new FileOutputStream("c:\\ExcelFile\\Test0.xls");
        file.write(o.toByteArray());
        file.close();
        Export2Excel.createExcelFile(dataExcel);

        dataExcel = new DataExcel("titolo", titoli, null);

        try
        {
            o = Export2Excel.createExcelFile(dataExcel);
            file = new FileOutputStream("c:\\ExcelFile\\Test1.xls");
            file.write(o.toByteArray());
            file.close();
            throw new TestException("Comportamento non corretto");
        } //try
        catch (Exception e)
        {
            if (!e.getMessage().equals("Each columns must have its own descriptions"))
            {
                throw new TestException("Comportamento non corretto");
            }
        } //catch

        dataExcel = new DataExcel("titolo", null, objects);

        try
        {
            o = Export2Excel.createExcelFile(dataExcel);
            file = new FileOutputStream("c:\\ExcelFile\\Test2.xls");
            file.write(o.toByteArray());
            file.close();
            throw new TestException("Comportamento non corretto");
        } //try
        catch (Exception e)
        {
            if (!e.getMessage().equals("Each columns must have its own descriptions"))
            {
                throw new TestException("Comportamento non corretto");
            }
        } //catch

        dataExcel = new DataExcel("titolo", null, null);
        o = Export2Excel.createExcelFile(dataExcel);
        file = new FileOutputStream("c:\\ExcelFile\\Test3.xls");
        file.write(o.toByteArray());
        file.close();

        dataExcel = new DataExcel(null, titoli, objects);
        o = Export2Excel.createExcelFile(dataExcel);
        file = new FileOutputStream("c:\\ExcelFile\\Test4.xls");
        file.write(o.toByteArray());
        file.close();

        dataExcel = new DataExcel(null, titoli, null);

        try
        {
            o = Export2Excel.createExcelFile(dataExcel);
            file = new FileOutputStream("c:\\ExcelFile\\Test5.xls");
            file.write(o.toByteArray());
            file.close();

            throw new TestException("Comportamento non corretto");
        } //try
        catch (Exception e)
        {
            if (!e.getMessage().equals("Each columns must have its own descriptions"))
            {
                throw new TestException("Comportamento non corretto");
            }
        } //catch

        try
        {
            dataExcel = new DataExcel(null, null, objects);

            o = Export2Excel.createExcelFile(dataExcel);
            file = new FileOutputStream("c:\\ExcelFile\\Test6.xls");
            file.write(o.toByteArray());
            file.close();
            throw new TestException("Comportamento non corretto");
        } //try
        catch (Exception e)
        {
            if (!e.getMessage().equals("Each columns must have its own descriptions"))
            {
                throw new TestException("Comportamento non corretto");
            }
        } //catch

        dataExcel = new DataExcel(null, null, null);
        o = Export2Excel.createExcelFile(dataExcel);
        file = new FileOutputStream("c:\\ExcelFile\\Test7.xls");
        file.write(o.toByteArray());
        file.close();

        dataExcel.getTitlePage().setFontBold(true);
        dataExcel.getTitlePage().setFontItalic(true);
        dataExcel.getTitlePage().setFontHeightInPonts((short) 12);
        dataExcel.getTitlePage().setFontName(null);
        dataExcel.getTitlePage().setFontColor(null);

        dataExcel.getBodyTable().setFontBold(true);
        dataExcel.getBodyTable().setFontItalic(true);
        dataExcel.getBodyTable().setFontHeightInPonts((short) 20);
        dataExcel.getBodyTable().setFontName("Arial");
        dataExcel.getBodyTable().setFontColor(new Color(0, 255, 0));

        dataExcel.getTitleTable().setFontBold(true);
        dataExcel.getTitleTable().setFontItalic(true);
        dataExcel.getTitleTable().setFontHeightInPonts((short) 10);
        dataExcel.getTitleTable().setFontName("Tahoma");
        dataExcel.getTitleTable().setFontColor(new Color(0, 255, 255));
    }
}

package com.carel.supervisor.base.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;


public class BodyTable extends Element
{
    private Object[][] objects = null;
    private int rows = 0;
    private int columns = 0;

    public BodyTable(Object[][] tableData)
    {
        objects = tableData;

        try
        {
            columns = objects[0].length;
            rows = objects.length;
        } //try
        catch (Exception e)
        {
            rows = 0;
            columns = 0;
        } //catch
    } //BodyTable

    public int getRowLenght()
    {
        return rows;
    } //getRowLenght

    public int getColumnsLenght()
    {
        return columns;
    } //getColumnsLenght

    protected Object getData()
    {
        return objects;
    } //getData

    protected void setData(Object object)
    {
        objects = (Object[][]) object;
    } //setData

    protected void createStyleCell(HSSFCellStyle style, HSSFFont font,
        HSSFPalette palette)
    {
        font.setFontHeightInPoints(fontHeightInPonts);
        font.setFontName(fontName);
        font.setItalic(italic);
        font.setStrikeout(strikeout);

        if (bold)
        {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }

        palette.setColorAtIndex(HSSFColor.AQUA.index,
            (byte) fontColor.getRed(), (byte) fontColor.getGreen(),
            (byte) fontColor.getBlue());

        font.setColor(HSSFColor.AQUA.index);
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    } //createCellstyle
} //classBodyTable

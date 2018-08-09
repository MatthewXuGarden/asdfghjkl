package com.carel.supervisor.base.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;


public class TitleTable extends Element
{
    private String[] titleTable = null;
    private int columns = 0;

    public TitleTable(String[] titleTable)
    {
        this.titleTable = titleTable;

        try
        {
            columns = titleTable.length;
        }
        catch (Exception e)
        {
            columns = 0;
        }
    } //TitleTable

    protected Object getData()
    {
        return titleTable;
    } //getData

    protected void setData(Object object)
    {
        titleTable = (String[]) object;
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

        palette.setColorAtIndex(HSSFColor.RED.index, (byte) fontColor.getRed(),
            (byte) fontColor.getGreen(), (byte) fontColor.getBlue());
        font.setColor(HSSFColor.RED.index);
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
    } //createStyleCell

    public int getColumnsLength()
    {
        return columns;
    }
} //Class TitleTable

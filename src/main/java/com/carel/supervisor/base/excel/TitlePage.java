package com.carel.supervisor.base.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;


public class TitlePage extends Element
{
    private String titlePage = null;

    public TitlePage(String titlePage)
    {
        this.titlePage = titlePage;
    } //TitlePage

    protected Object getData()
    {
        return titlePage;
    } //getData

    protected void setData(Object object)
    {
        titlePage = (String) object;
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

        palette.setColorAtIndex(HSSFColor.GREEN.index,
            (byte) fontColor.getRed(), (byte) fontColor.getGreen(),
            (byte) fontColor.getBlue());

        font.setColor(HSSFColor.GREEN.index);
        style.setFont(font);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    } //createStyle
} //Class TitlePage

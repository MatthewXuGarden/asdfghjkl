package com.carel.supervisor.base.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import java.awt.Color;


abstract class Element
{
    protected boolean bold = false;
    protected boolean italic = false;
    protected boolean strikeout = false;
    protected short fontHeightInPonts = 12;
    protected String fontName = "Arial";
    protected Color fontColor = new Color(0, 0, 0);

    public void setFontBold(boolean bold)
    {
        this.bold = bold;
    } //setFontBold

    public void setFontItalic(boolean italic)
    {
        this.italic = italic;
    } //setFontItalic

    public void setFontStrikout(boolean strikeout)
    {
        this.strikeout = strikeout;
    } //setFontStrikout

    public void setFontHeightInPonts(short fontHeightInPonts)
    {
        if (fontHeightInPonts > 0)
        {
            this.fontHeightInPonts = fontHeightInPonts;
        }
    } //setFontHeightInPonts

    public void setFontName(String fontName)
    {
        if (fontName != null)
        {
            this.fontName = fontName;
        }
    } //setFontName

    public void setFontColor(Color fontColor)
    {
        if (fontColor != null)
        {
            this.fontColor = fontColor;
        }
    } //setFontColor

    protected abstract Object getData();

    protected abstract void setData(Object object);

    protected abstract void createStyleCell(HSSFCellStyle style, HSSFFont font,
        HSSFPalette palette);
} //Class Element

package com.carel.supervisor.base.excel;

import org.apache.poi.hssf.usermodel.*;
import java.io.*;


public class Export2Excel
{
    private final static int START_TB_ROW = 4;
    private final static int START_TB_COLUMN = 3;
    
    private Export2Excel()
    {	
    }
    
    public static ByteArrayOutputStream createExcelFile(DataExcel dataExcel)
        throws Throwable
    {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        HSSFRow row = sheet.createRow((short) START_TB_ROW);
        HSSFCellStyle styleTitlePage = wb.createCellStyle();
        HSSFCellStyle styleTitleTable = wb.createCellStyle();
        HSSFCellStyle styleBodyTable = wb.createCellStyle();

        HSSFFont fontTitlePage = wb.createFont();
        HSSFFont fontTitleTable = wb.createFont();
        HSSFFont fontBodyTable = wb.createFont();

        HSSFPalette paletteTitlePage = wb.getCustomPalette();
        HSSFPalette paletteTitleTable = wb.getCustomPalette();
        HSSFPalette paletteBodyTable = wb.getCustomPalette();

        int rows = rows = dataExcel.getBodyTable().getRowLenght();
        int columns = dataExcel.getBodyTable().getColumnsLenght();

        if (dataExcel.getTitleTable().getColumnsLength() != columns)
        {
            throw new ExcelCreateException(
                "Each columns must have its own descriptions");
        }

        String titlePage = (String) dataExcel.getTitlePage().getData();
        String[] titleTable = (String[]) dataExcel.getTitleTable().getData();
        Object[][] dataTable = (Object[][]) dataExcel.getBodyTable().getData();

        try
        {
            dataExcel.getTitlePage().createStyleCell(styleTitlePage,
                fontTitlePage, paletteTitlePage);
            createCell(sheet.createRow((short) (START_TB_ROW - 3)),
                (short) (START_TB_COLUMN + 1), HSSFCellStyle.ALIGN_CENTER,
                styleTitlePage).setCellValue(titlePage);
        }
        catch (Exception e)
        {
            throw new ExcelCreateException("Error during write Title Page");
        }

        //Creazione del titolo della tabella
        try
        {
            dataExcel.getTitleTable().createStyleCell(styleTitleTable,
                fontTitleTable, paletteTitleTable);

            for (int i = 0; i < columns; i++)
            {
                createCell(row, (short) (START_TB_COLUMN + i),
                    HSSFCellStyle.ALIGN_CENTER, styleTitleTable).setCellValue(titleTable[i]);
            }
        } //try
        catch (Exception e)
        {
            throw new ExcelCreateException("Error during write Title Table");
        } //catch
          //Riempimento della tabella

        try
        {
            dataExcel.getBodyTable().createStyleCell(styleBodyTable,
                fontBodyTable, paletteBodyTable);

            for (int i = 0; i < rows; i++)
            {
                row = sheet.createRow((short) (START_TB_ROW + i + 1));

                for (int j = 0; j < columns; j++)
                {
                    createCell(row, (short) (START_TB_COLUMN + j),
                        HSSFCellStyle.ALIGN_CENTER, styleBodyTable)
                        .setCellValue(dataTable[i][j].toString());
                }
            } //for
        } //try
        catch (Exception e)
        {
            throw new ExcelCreateException("Error during write Data Table");
        } //catch

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);

        return out;
    } //createExcelFile

    private static HSSFCell createCell(HSSFRow row, short column, short align,
        HSSFCellStyle style)
    {
        HSSFCell cell = row.createCell(column);
        HSSFCellStyle cellStyle = style;
        cellStyle.setAlignment(align);
        cell.setCellStyle(cellStyle);

        return cell;
    } //create cell 
} //Class Export2Excel

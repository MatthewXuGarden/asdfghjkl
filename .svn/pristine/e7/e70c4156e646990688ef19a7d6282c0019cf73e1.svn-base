package com.carel.supervisor.base.excel;

public class DataExcel
{
    private BodyTable bodyTable = null;
    private TitleTable titleTable = null;
    private TitlePage titlePage = null;

    public DataExcel(String titlePage, String[] titleTable, Object[][] tableData)
    {
        this.bodyTable = new BodyTable(tableData);
        this.titlePage = new TitlePage(titlePage);
        this.titleTable = new TitleTable(titleTable);
    } //DataExcel

    public BodyTable getBodyTable()
    {
        return bodyTable;
    } //getBodyTable

    public TitleTable getTitleTable()
    {
        return titleTable;
    } //getTileTable

    public TitlePage getTitlePage()
    {
        return titlePage;
    } //getTitlePage

    public void setBodyTable(BodyTable bodyTable)
    {
        this.bodyTable = bodyTable;
    } //setBodyTable

    public void setTitleTable(TitleTable titleTable)
    {
        this.titleTable = titleTable;
    } //setTileTable

    public void setTitlePage(TitlePage titlePage)
    {
        this.titlePage = titlePage;
    } //setTitlePage
} //Class DataExcel

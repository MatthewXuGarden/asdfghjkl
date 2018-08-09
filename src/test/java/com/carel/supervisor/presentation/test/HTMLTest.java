package com.carel.supervisor.presentation.test;

import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*;
import junit.framework.TestCase;


public class HTMLTest extends TestCase
{
    private static final int ROWS = 10;
    private static final int COLUMNS = 3;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(HTMLTest.class);
    } //main

    public void test()
    {
        HTMLElement[][] dati = new HTMLElement[ROWS][];
        String[] headerTable = { "Colonna1", "Colonna2", "Colonna3" };
        String[] dblClickRowFunction = new String[ROWS];
        String[] rowsClasses = new String[ROWS];

        for (int i = 0; i < ROWS; i++)
        {
            dati[i] = new HTMLElement[COLUMNS];
        }

        for (int i = 0; i < ROWS; i++)
        {
            dblClickRowFunction[i] = new String("alert('Hello Word!');");
            rowsClasses[i] = new String("Row1");

            HTMLDiv div = null;

            for (int j = 0; j < COLUMNS; j++)
            {
                switch (j)
                {
                case 0:
                    div = new HTMLDiv(new HTMLDiv(String.valueOf('A' + i)).getHTMLText());

                    break;

                case 1:
                    div = new HTMLDiv(new HTMLDiv(String.valueOf(i)).getHTMLText());

                    break;

                case 2:
                    div = new HTMLDiv(new HTMLDiv(String.valueOf('B' + i)).getHTMLText()); //new HTMLButton("bottone","alert('Hello Word!');");

                    break;
                } //switch

                dati[i][j] = div; //new HTMLImage("prova.jpg", 30);
            } //for
        } //for

        HTMLTable table = new HTMLTable("tabella", headerTable, dati, false,
                true);
        table.setDlbClickRowFunction(dblClickRowFunction);
        table.setRowsClasses(rowsClasses);

        HTMLDiv mainDiv = new HTMLDiv(table.getHTMLText());
        mainDiv.addAttribute("style", "height:500px;width:300px;overflow:auto;");
        mainDiv.addAttribute("id", "blocco");

        System.out.println(mainDiv.getHTMLText());

        // System.out.println(menu.getHTMLMenuText());
    }
} //Class HTMLTest

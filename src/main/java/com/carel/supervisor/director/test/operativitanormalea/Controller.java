package com.carel.supervisor.director.test.operativitanormalea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Controller
{
    private static final String PATH = "test data file/";

    public static void main(String[] args) throws Exception
    {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader myInput = new BufferedReader(reader);
        String str = new String();

        try
        {
            System.out.print("Inserisci il Code Test: ");
            str = myInput.readLine();
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e);
            System.exit(-1);
        }

        FileReader fileWDT = new FileReader(PATH + "WriteDataTest" + str +
                ".txt");
        FileReader fileRDT = new FileReader(PATH + "ReadDataTest" + str +
                ".txt");
        BufferedReader bufferedWDT = new BufferedReader(fileWDT);
        BufferedReader bufferedRDT = new BufferedReader(fileRDT);

        for (int i = 0;; i++)
        {
            String w = bufferedWDT.readLine();
            String r = bufferedRDT.readLine();

            if ((w == null) && (r == null))
            {
                System.out.println(
                    "Test concluso correttamente\nNumero di righe lette:" + i);
                System.exit(1);
            }

            if ((w != null) && (r != null))
            {
                if (!w.equals(r))
                {
                    System.out.println("I file presentano differenza in riga " +
                        i + " verifica il test");
                    System.out.println(
                        "Potrebbe essere un cambio di riga Attenzione a quanti dati inserisci");
                    System.exit(1);
                }
            }
            else
            {
                System.out.println("I file presentano differenza in riga " + i +
                    " verifica il test");
                System.exit(1);
            } //else
        } //for
    } //main
}

package com.carel.supervisor.director.test;

import com.carel.supervisor.field.Status;
import java.sql.Types;


public class Prove
{
    public static void main(String[] args)
    {
        Status status = new Status();

        for (int i = 0; i < 64; i++)
        {
            if ((i % 444) == 0)
            {
                status.setStatus((byte) i, Status.ACTIVE);
            }

            System.out.println("Stato:" + i + " della posizione:" +
                status.getStatus((byte) i));
        }

        for (int i = 0; i < 256; i++)
        {
            System.out.println("insert into hsvariable values(1,1," +
                (i + 100) + ");");
        }

        System.out.println(Types.CHAR);
    }
}

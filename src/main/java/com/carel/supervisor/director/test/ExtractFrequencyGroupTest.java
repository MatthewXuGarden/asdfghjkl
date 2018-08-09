package com.carel.supervisor.director.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.PollingVarList;
import com.carel.supervisor.director.SetPollingVarList;
import com.carel.supervisor.director.SetPollingVarListFactory;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DataCollector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;


public class ExtractFrequencyGroupTest
{
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        Connection connection = DatabaseMgr.getInstance().getConnection(null);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "truncate cfvariable");
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("truncate buffer");
        preparedStatement.execute();
        connection.commit();

        preparedStatement = connection.prepareStatement(
                "INSERT INTO cfvariable (idvariable, pvcode, idsite, iddevice, islogic, idvarmdl, functioncode, code, \"type\", address, todisplay, buttonpath, priority, readwrite, \"minvalue\", \"maxvalue\", defaultvalue, measureunit, idvargroup, imageon, imageoff, frequency, delta, delay, isactive, iscancelled, grpcategory, inserttime, lastupdate) VALUES (?, 'firstPV', 1, 6, 'FALSE', 441, NULL, 'ALL_LPRESS_mand', 4, 43, 'FALSE', NULL, 1, '1', NULL, NULL, '0', NULL, 4, ' ', ' ', ?, 1, 0, 'FALSE', 'FALSE', NULL, '2006-02-23 09:11:33.544', '2006-02-23 09:11:33.544');");

        //Preparo le variabili
        int[] frequencies = new int[] { 1, 5, 30, 60, 120, 300 };
        int[] numVariables = new int[] { 3, 3, 10, 5, 20, 30 };

        for (int freqCount = 0, idVariable = 0; freqCount < frequencies.length;
                freqCount++)
        {
            for (int i = 0; i < numVariables[freqCount]; i++, idVariable++)
            {
                preparedStatement.setInt(1, idVariable);
                preparedStatement.setInt(2, frequencies[freqCount]);
                preparedStatement.execute();
            } //for
        } //for

        connection.commit();

        preparedStatement = connection.prepareStatement(
                "INSERT INTO buffer VALUES (1, ?,10, -1,false);");

        for (int freqCount = 0, idVariable = 0; freqCount < frequencies.length;
                freqCount++)
        {
            for (int i = 0; i < numVariables[freqCount]; i++, idVariable++)
            {
                preparedStatement.setInt(1, idVariable);
                preparedStatement.execute();
            } //for
        } //for

        connection.commit();

        SetPollingVarListFactory setPollingVarListFactory = new SetPollingVarListFactory();
        setPollingVarListFactory.init();

        ControllerMgr.getInstance().load();

        Map functions = ControllerMgr.getInstance().getFunctions();
        DataCollector dataCollector = dataCollector = FieldConnectorMgr.getInstance()
                                                                       .getDataCollector();
        
        DeviceStatusMgr.getInstance().loadPhysic(dataCollector);
        DeviceStatusMgr.getInstance().loadLogic(functions);

        setPollingVarListFactory.assign(dataCollector, functions);

        SetPollingVarList setPollingVarList = setPollingVarListFactory.createSetPollingVarList();
        int freqGCD = setPollingVarListFactory.getGCDFreq();

        PollingVarList pollingVarList = null;

        //Test 24
        System.out.println("Test 24 Start");

        for (int i = 0, idVariable = 0; i < frequencies.length; i++)
        {
            pollingVarList = setPollingVarList.getPolligVarList(i);

            //Controllo della frequenza di classe
            if (pollingVarList.getFrequency() != frequencies[i])
            {
                throw new Exception(
                    "Comportamento inatteso sulla frequenza di gruppo");
            }

            //Controllo la corretta capacità della classe
            if (pollingVarList.size() != numVariables[i])
            {
                throw new Exception(
                    "Comportamento inatteso sul numero di variabili nel gruppo di frequenza: " +
                    frequencies[i]);
            }

            //Controllo che tutte le variabili introdotte con tale frequenza sia nella classe giusta
            ArrayList arrayList = new ArrayList();

            for (int j = 0; j < pollingVarList.size(); j++)
            {
                Variable variable = pollingVarList.getVariable(j);
                arrayList.add(variable.getInfo().getId());
            } //for

            for (int j = 0; j < pollingVarList.size(); j++)
            {
                if (!arrayList.remove(new Integer(idVariable)))
                {
                    throw new Exception("Comportamento inatteso variabile: " +
                        idVariable + " non presente nel gruppo atteso");
                }

                idVariable++;
            } //for

            if (!arrayList.isEmpty())
            {
                throw new Exception(
                    "Comportamento inatteso sul numero di variabili nel gruppo di frequenza: " +
                    frequencies[i]);
            }
        } //for

        System.out.println("Test 24 End");

        //Test 23
        System.out.println("Test 25 Start");

        for (int i = 0; i < frequencies.length; i++)
        {
            pollingVarList = setPollingVarList.getPolligVarList(i);
            pollingVarList.updateCountFrequencyCicle(freqGCD); //incremento dello startup

            for (int j = 0; j < 10; j++)
            {
                if (pollingVarList.getCountFrequencyCicle() != 0)
                {
                    throw new Exception(
                        "Comportamento inatteso sul internal counter of Frequency freqGroup=" +
                        frequencies[i]);
                }

                for (int z = 0; z < frequencies[i]; z++)
                {
                    pollingVarList.updateCountFrequencyCicle(freqGCD);

                    if ((pollingVarList.getCountFrequencyCicle() == 0) &&
                            (frequencies[i] != (z + 1)))
                    {
                        throw new Exception(
                            "Comportamento inatteso sul internal counter of Frequency freqGroup=" +
                            frequencies[i]);
                    }
                } //for in z
            } //for in j
        } //for in i

        System.out.println("Test 25 End");

        System.out.println("Fine Ok!");
    } //main
} //Class ExtractfrequencyGroupTest

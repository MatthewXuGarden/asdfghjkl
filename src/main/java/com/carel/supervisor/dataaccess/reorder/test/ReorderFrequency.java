package com.carel.supervisor.dataaccess.reorder.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

//CLASSE UTILIZZATA NEL TEST


//UTILIZZO DELLO STATO PER AGEVOLARE LA LETTURA DELLE CONDIZIONI
//Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE
//    0      ||     0      ||   0                      A 
//    0      ||     0      ||   1                      B 
//    0      ||     1      ||   0                      C 
//    0      ||     1      ||   1                      D 
//    1      ||     0      ||   0                      E 
//    1      ||     0      ||   1                      F  

public class ReorderFrequency
{
//    //Costanti per le query e statement
//    private static final String KEY = "key";
//    private static final String INSERT_TIME = "inserttime";
//    private static final String KEY_MAX = "keymax";
//    private static final String KEY_ACTUAL = "keyactual";
//    private static final String IS_TURN = "isturn";
//    private static final int FETCH_SIZE = 100;
//
//    //Costanti Vedi tavola di verità sopra per il senso delle costanti
//    private final byte A = 0;
//    private final byte B = 1;
//    private final byte C = 2;
//    private final byte D = 3;
//    private final byte E = 4;
//    private final byte F = 5;
//    private Connection connection = null; //Connessione al db
//    private ReorderInformation queue = null; //Coda da dove mi arrivano le variabili con i dati sulla nuova frequenza
//    private int idVariable = 0; //id della variabile
//    private int idSite = 0; //id del sito
//    private boolean isTurn = false; //se ha già ciclato sul buffer e quindi se è in update o ancora in insert nelle nuove righe  
//    private short oldKeyMax = 0; //Vecchio valore del keyMax  
//    private short newKeyMax = 0; //Nuovo valore del keyMax		 	
//    private short oldKeyActual = 0; //Vecchio valore del keyActual
//    private short newKeyActual = 0; //Nuovo valore del keyActual
//    //private final String[]tableNames= new String[]{"hsvarhaccp","hsvarhistor"};
//    //private String tableName=null;
//
//    public ReorderFrequency(ReorderInformation queue) throws Exception
//    {
//        this.queue = queue;
//        connection = DatabaseMgr.getInstance().getConnection(null);
//    } //ReorderFrequency
//
//    public void startReorderHistorical() throws Exception
//    {
//        //ricavo i vecchi dati relativi 
//        ResultSet resultSet = null;
//        PreparedStatement preparedStatement = null;
//        Object[][] objects = queue.dequeAllRecords();
//        int numVariable = 0;
//
//        if (objects != null)
//        {
//            numVariable = objects.length;
//        }
//
//        for (int i = 0; i < numVariable; i++)
//        {
//            idSite = ((Integer) objects[i][ReorderInformation.INDEX_ID_SITE]).intValue();
//            idVariable = ((Integer) objects[i][ReorderInformation.INDEX_ID_VARIABLE]).intValue();
//-------->>>>>>MODIFICA EFFETTUATA PER AGEVOLARE I TEST            newKeyMax =((Integer)objects[i][ReorderInformation.INDEX_SAMPLING_PERIOD]).shortValue();
//            //tableName=((String)objects[i][ReorderInformation.INDEX_IS_HACCP]).equals("TRUE")?tableNames[0]:tableNames[1];
//            
//           //tableName;
//
//            preparedStatement = connection.prepareStatement(
//                    "SELECT keymax,keyactual,isturn FROM buffer WHERE idsite=? AND idvariable=?");
//            preparedStatement.setInt(1,
//                ((Integer) objects[i][ReorderInformation.INDEX_ID_SITE]).intValue());
//            preparedStatement.setInt(2,
//                ((Integer) objects[i][ReorderInformation.INDEX_ID_VARIABLE]).intValue());
//
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet != null)
//            {
//                resultSet.next();
//                oldKeyMax = resultSet.getShort(KEY_MAX);
//                oldKeyActual = resultSet.getShort(KEY_ACTUAL);
//                isTurn = resultSet.getBoolean(IS_TURN);
//
//                if (oldKeyMax != newKeyMax)
//                {
//                    byte select = 0;
//
//                    if (isTurn)
//                    {
//                        select |= (1 << 0);
//                    }
//
//                    if (oldKeyActual >= newKeyMax)
//                    {
//                        select |= (1 << 1);
//                    }
//
//                    if (newKeyMax > oldKeyMax)
//                    {
//                        select |= (1 << 2);
//                    }
//
//                    //select = F;
//                    switch (select)
//                    {
//                    case A:
//                        functionA();
//
//                        break;
//
//                    case B:
//                        functionB();
//
//                        break;
//
//                    case C:
//                        functionC();
//
//                        break;
//
//                    case D:
//                        functionD();
//
//                        break;
//
//                    case E:
//                        functionE();
//
//                        break;
//
//                    case F:
//                        functionF();
//
//                        break;
//                    } //switch
//                } //if
//                //Aggiorno la tabella con lo storico delle frequenze
//                /*preparedStatement.close();
//                preparedStatement = connection.prepareStatement(
//                        "INSERT INTO hsfrequency VALUES(?,?,?,?)");
//                preparedStatement.setInt(1, idSite);
//                preparedStatement.setInt(2, idVariable);
//                preparedStatement.setInt(3,
//                    ((Integer) objects[i][ReorderInformation.INDEX_SAMPLING_PERIOD]).intValue());
//                preparedStatement.setTimestamp(4,
//                    new Timestamp(System.currentTimeMillis()));
//                preparedStatement.execute();*/
//            } //if
//        } //for
//        
//        connection.commit();
//        DatabaseMgr.getInstance().closeConnection(null, connection);
//    } //startReorderHistorical
//
//    //////////////////////////// FUNCTION A //////////////////////////////  
//    //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE        //
//    //      0      ||     0      ||   0                      A          //
//    //////////////////////////////////////////////////////////////////////
//    private void functionA() throws SQLException
//    {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setBoolean(2, false);
//        preparedStatement.setInt(3, idSite);
//        preparedStatement.setInt(4, idVariable);
//        preparedStatement.execute();
//    } //FunctionA
//
//    //////////////////////////// FUNCTION B //////////////////////////////    
//    //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE        //
//    //      0      ||     0      ||   1                      B          //
//    //////////////////////////////////////////////////////////////////////
//    private void functionB() throws SQLException
//    {
//        //pongo il nuovo kactual a (kmax' -1) e il turn a true
//        //Mi faccio dare le chiavi ordinate per tempo
//        ResultSet resultSet = null;
//        ArrayList keyList = new ArrayList();
//        ArrayList insertTimeList = new ArrayList();
//        newKeyActual = (short) (newKeyMax - 1);
//
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "SELECT key,inserttime from hsvarhistor where idsite=? and idvariable=? order by inserttime");
//
//        preparedStatement.setShort(1, (short) idSite);
//        preparedStatement.setInt(2, idVariable);
//        preparedStatement.setFetchSize(FETCH_SIZE);
//        resultSet = preparedStatement.executeQuery();
//
//        //Ho la lista ordinata dei record 
//        while (resultSet.next())
//        {
//            keyList.add(new Short(resultSet.getShort(KEY)));
//            insertTimeList.add(resultSet.getTimestamp(INSERT_TIME));
//        } //while
//
//        preparedStatement.close();
//
//        //elimino le kmax-kmax' più vecchie
//        preparedStatement = connection.prepareStatement(
//                "DELETE FROM hsvarhistor WHERE idsite=? AND idvariable=? AND key=?");
//
//        for (int i = 0; i < (oldKeyMax - newKeyMax); i++)
//        {
//            preparedStatement.setShort(1, (short) idSite);
//            preparedStatement.setInt(2, idVariable);
//            preparedStatement.setShort(3, ((Short) keyList.get(i)).shortValue());
//            preparedStatement.execute();
//        } //for
//
//        preparedStatement.close();
//
//        //riordino gli indici dei restanti
//        preparedStatement = connection.prepareStatement(
//                "UPDATE hsvarhistor SET key=? WHERE idsite=? AND idvariable=? AND key=? AND inserttime=?");
//
//        for (short i = (short) (oldKeyMax - newKeyMax); i < keyList.size();
//                i++)
//        {
//            preparedStatement.setShort(1,
//                new Short((short) (i - (oldKeyMax - newKeyMax))).shortValue());
//            preparedStatement.setShort(2, (short) idSite);
//            preparedStatement.setInt(3, idVariable);
//            preparedStatement.setShort(4, ((Short) keyList.get(i)).shortValue());
//            preparedStatement.setTimestamp(5, (Timestamp) insertTimeList.get(i));
//            preparedStatement.execute();
//        } //for
//
//        //Aggiorno il valore di key acktual
//        preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,keyactual=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setShort(2, newKeyActual);
//        preparedStatement.setBoolean(3, true);
//        preparedStatement.setInt(4, idSite);
//        preparedStatement.setInt(5, idVariable);
//        preparedStatement.execute();
//    } //functionB
//      //////////////////////////// FUNCTION C //////////////////////////////    
//      //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE        //
//      //      0      ||     1      ||   0                      C          //
//      //////////////////////////////////////////////////////////////////////
//
//    private void functionC() throws SQLException
//    {
//        //cancello i record in più cioè da 0 a kactual - kmax' +1
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "DELETE FROM hsvarhistor WHERE idsite=? AND idvariable=? AND key=?");
//        newKeyActual = (short) (newKeyMax - 1);
//
//        int i = 0;
//
//        for (; i < (oldKeyActual - newKeyMax + 1); i++)
//        {
//            preparedStatement.setShort(1, (short) idSite);
//            preparedStatement.setInt(2, idVariable);
//            preparedStatement.setShort(3, (short) i);
//            preparedStatement.execute();
//        } //for
//
//        preparedStatement = connection.prepareStatement(
//                "UPDATE hsvarhistor SET key=key-? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, (short) i);
//        preparedStatement.setShort(2, (short) idSite);
//        preparedStatement.setInt(3, idVariable);
//        preparedStatement.execute();
//        preparedStatement.close();
//
//        //Aggiorno il valore di key acktual
//        preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,keyactual=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setShort(2, newKeyActual);
//        preparedStatement.setBoolean(3, true);
//        preparedStatement.setInt(4, idSite);
//        preparedStatement.setInt(5, idVariable);
//        preparedStatement.execute();
//    } //functionC
//
//    //////////////////////////// FUNCTION D //////////////////////////////       
//    //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE        //
//    //      0      ||     1      ||   1                      D          //
//    //////////////////////////////////////////////////////////////////////
//    private void functionD() throws SQLException
//    {
//        //cancello i record in più cioè da 0 a kactual - kmax' +1
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "DELETE FROM hsvarhistor WHERE idsite=? AND idvariable=? AND key=?");
//        newKeyActual = (short) (newKeyMax - 1);
//
//        int i = 0;
//
//        //cancello i vecchi scritti prima di un blocco di dimensione kmax' da dove mi trovo cioè in oldkeyactual
//        for (; i < (oldKeyActual - newKeyMax + 1); i++)
//        {
//            preparedStatement.setShort(1, (short) idSite);
//            preparedStatement.setInt(2, idVariable);
//            preparedStatement.setShort(3, (short) i);
//            preparedStatement.execute();
//        } //for
//
//        //cancello i più vecchi che sarei andato a sovrascrivere
//        for (int j = oldKeyActual + 1; j < oldKeyMax; j++)
//        {
//            preparedStatement.setShort(1, (short) idSite);
//            preparedStatement.setInt(2, idVariable);
//            preparedStatement.setShort(3, (short) j);
//            preparedStatement.execute();
//        } //for
//
//        preparedStatement = connection.prepareStatement(
//                "UPDATE hsvarhistor SET key=key-? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, (short) i);
//        preparedStatement.setShort(2, (short) idSite);
//        preparedStatement.setInt(3, idVariable);
//        preparedStatement.execute();
//        preparedStatement.close();
//
//        //Aggiorno il valore di key acktual
//        preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,keyactual=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setShort(2, newKeyActual);
//        preparedStatement.setBoolean(3, true);
//        preparedStatement.setInt(4, idSite);
//        preparedStatement.setInt(5, idVariable);
//        preparedStatement.execute();
//    } //functionD
//
//    //////////////////////////// FUNCTION E //////////////////////////////    
//    //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE  Visto che Kmax'>Kmax e Kmax>Kactual allora Kactual<Kmax'  strettamente minore
//    //      1      ||     0      ||   0                      E          //
//    //////////////////////////////////////////////////////////////////////
//    private void functionE() throws SQLException
//    {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setBoolean(2, false);
//        preparedStatement.setInt(3, idSite);
//        preparedStatement.setInt(4, idVariable);
//        preparedStatement.execute();
//    } //functionE
//
//    /////////////////////////// FUNCTION F ///////////////////////////////
//    //  Kmax'>Kmax || Kact>=Kmax'|| isTurn          --> COSTANTE        //
//    //      1      ||     0      ||   1                      F          //
//    //////////////////////////////////////////////////////////////////////
//    private void functionF() throws DataBaseException, SQLException
//    {
//        ResultSet resultSet = null;
//        ArrayList keyList = new ArrayList();
//        ArrayList insertTimeList = new ArrayList();
//        newKeyActual = (short) (oldKeyMax - 1);
//
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "SELECT key,inserttime from hsvarhistor where idsite=? and idvariable=? order by inserttime");
//
//        preparedStatement.setShort(1, (short) idSite);
//        preparedStatement.setInt(2, idVariable);
//        preparedStatement.setFetchSize(FETCH_SIZE);
//        resultSet = preparedStatement.executeQuery();
//
//        //Ho la lista ordinata dei record 
//        while (resultSet.next())
//        {
//            keyList.add(new Short(resultSet.getShort(KEY)));
//            insertTimeList.add(resultSet.getTimestamp(INSERT_TIME));
//        } //while
//
//        preparedStatement.close();
//
//        //Rinomino le chiavi ordinandole
//        preparedStatement = connection.prepareStatement(
//                "UPDATE hsvarhistor SET key=? WHERE idsite=? AND idvariable=? AND key=? AND inserttime=?");
//
//        for (short i = 0; i < keyList.size(); i++)
//        {
//            preparedStatement.setShort(1, new Short(i).shortValue());
//            preparedStatement.setShort(2, (short) idSite);
//            preparedStatement.setInt(3, idVariable);
//            preparedStatement.setShort(4, ((Short) keyList.get(i)).shortValue());
//            preparedStatement.setTimestamp(5, (Timestamp) insertTimeList.get(i));
//            preparedStatement.execute();
//        } //for
//
//        //Aggiorno il valore di key acktual
//        preparedStatement = connection.prepareStatement(
//                "UPDATE buffer SET keymax=?,keyactual=?,isturn=? WHERE idsite=? AND idvariable=?");
//        preparedStatement.setShort(1, newKeyMax);
//        preparedStatement.setShort(2, newKeyActual);
//        preparedStatement.setBoolean(3, false);
//        preparedStatement.setInt(4, idSite);
//        preparedStatement.setInt(5, idVariable);
//        preparedStatement.execute();
//    } //functionE
} //Class ReorderFrequency

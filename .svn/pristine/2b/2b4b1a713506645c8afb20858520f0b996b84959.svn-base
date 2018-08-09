package com.carel.supervisor.director.graph;

import com.carel.supervisor.base.config.BaseConfig;
import java.sql.Timestamp;
import java.util.GregorianCalendar;


public class GraphTest
{
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        long endTime = new GregorianCalendar(2006, GregorianCalendar.MAY, 29, 13, 30, 0).getTimeInMillis();

        Object[] info = new Object[] { new Integer(1), new Integer(17) }; //idsite,idvariable
        Object[] info1 = new Object[] { new Integer(1), new Integer(18) }; //idsite,idvariable
        GraphInformation graphInformation = new GraphInformation(7);
        graphInformation.enqueRecord(info);
        graphInformation.enqueRecord(info1);

        graphInformation.setGraphPeriod(GraphConstant.HOURS6);
        graphInformation.setGraphType(GraphConstant.TYPE_HISTORICAL);

        graphInformation.setEndTime(new Timestamp(endTime));

        GraphRequest graphRequest = new GraphRequest(graphInformation);

        for (int i = 0; i < 1000; i++)
        {
            graphRequest.startRetrieve();
        }

        //YGraphCoordinates [][]coordinates= graphRequest.getGraphYsSeries();
        //preparo il vettore dei tempi

        /*        long []times =new long[(int) GraphConstant.NUM_X_POINT];
                 times[0] = graphInformation.getStartTime().getTime();
        times[(int) (GraphConstant.NUM_X_POINT - 1)] = graphInformation.getEndTime()
                                                                          .getTime();

        for (int i = 1; i < (GraphConstant.NUM_X_POINT - 1); i++)
        {
            times[i] = times[i - 1] + graphInformation.getTimeGranularity();
        }//for


        //Interpretazione dei dati: il null implica il padding dal valore precedente al successivo il null,null indica il buco del grafo
                System.out.println("Time:" + (end - init));
                for (int i = 0; i < coordinates.length; i++)
                {
                        NormalizeSerie normalizeSerie= new NormalizeSerie();
                        normalizeSerie.startNormalize(coordinates[i]);
                        int [][]y=normalizeSerie.getYSerie();
                        System.out.println("Serie:" + i);
                    for (int j = 0; j < coordinates[i].length; j++)
                            System.out.println("#"+j+" Time "+new Timestamp(times[j])+" Values "+coordinates[i][j] +" Normalized Values "+y[j][1]+";"+y[j][0]);
                    System.out.println(normalizeSerie.getXMLNormalized());
                }//for
        */
    } //main
} //GraphTest

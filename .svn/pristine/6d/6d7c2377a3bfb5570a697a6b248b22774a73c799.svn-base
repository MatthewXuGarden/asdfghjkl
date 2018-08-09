package com.carel.supervisor.plugin.fs;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.FSSetCallback;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.Variable;


public class FSCalculateDC
{
    public static final String INCREMENT = "inc";
    public static final String DECREMENT = "dec";
    public static final String INVARIATE = "inv";
    public static final String OFFLINE = "off";
    public static final String SOLENOIDNULL = "solenoidnull";

    public static String calculatePartialDC(FSUtil util, Integer max_offline_time)
    {
        int t_on = 0;
        int t_off = 0;
        int t_offline = 0;

        Boolean tmp_val = null;

        //lettura finestra storico
        FSCyclicList < Boolean > window = util.getStatus();

        int t_tot = window.size();

        for (int i = 0; i < t_tot; i++)
        {
            tmp_val = window.get(i);
            
            if (tmp_val==null)
            {
            	t_offline++;
            }
            else
            {
            	 if (tmp_val == true)
                 {
                     t_on++;
                 }
                 else if (tmp_val == false)
                 {
                     t_off++;
                 }
            }
        }

        Integer calculated_DC = null;

        // verificare missing non superiore soglia consentita
        if (t_tot>0 && ((t_offline*100/t_tot)) > max_offline_time)
        {
            //log banco offline FS05
        	if(util.getOnline()){
        		EventMgr.getInstance().warning(1,"System","fs","FS05",util.getDescription());
        	}
    		util.setOnline(false);
        	util.setActualDC(null);
            return OFFLINE;
        }
        else // confronto DC calcolato con il MaxDC
        {
            util.setOnline(true);
            calculated_DC = (t_on*100)/ (t_on + t_off);
                        
            Integer max_DC = util.getMaxDC();

            // allineamento max dc,minimo dc, e actual dc per il banco
            if (calculated_DC != null)
            {
                if (calculated_DC > util.getMaxDcRecorded())
                {
                    util.setMaxDcRecorded(calculated_DC);
                }

                if (calculated_DC < util.getMinDCRecorded())
                {
                    util.setMinDCRecorded(calculated_DC);
                }

                util.setActualDC(calculated_DC);
            }

            if (calculated_DC > max_DC)
            {
                return DECREMENT; //richiesta decremento set centrale
            }
            else if (calculated_DC < max_DC)
            {
                return INCREMENT; //richiesta aumento set centrale
            }
            else
            {
                return INVARIATE; //lasciare inalterato il set
            }
        }
    }

    public static String calculateRackDC(String[] partialDC, Integer max_offline_util,String rack_desc)
    {
        int n_inc = 0;
        int n_dec = 0;
        int n_inv = 0;
        int n_offline = 0;
        String tmp_dc = "";

        for (int i = 0; i < partialDC.length; i++)
        {
            tmp_dc = partialDC[i];

            if (tmp_dc.equalsIgnoreCase(INCREMENT))
            {
                n_inc++;
            }
            else if (tmp_dc.equalsIgnoreCase(DECREMENT))
            {
                n_dec++;
            }
            else if (tmp_dc.equalsIgnoreCase(INVARIATE))
            {
                n_inv++;
            }
            else
            {
                n_offline++;
            }
        }

        //se offline esco
        if (n_offline > max_offline_util)
        {
            return OFFLINE;
        }

        // se un solo banco richiede di diminuire il set, faccio la richiesta di abbassare il set della centrale
        if (n_dec > 0)
        {
            return DECREMENT;
        }

        // altrimenti se nessuno richiede di abbassare e almeno uno di aumentare, richiedo di aumentare il set.
        else if (n_inc > 0)
        {
            return INCREMENT;
        }

        // altrimenti lascio invariato il set se nessuno richiede aumento o diminuzione
        else
        {
            return INVARIATE;
        }
    }
    public static Variable getRackSetpointVar(FSRack rack)
    {
    	int id_setpoint = rack.getId_setpoint().intValue();
        //Float actual_set = ControllerMgr.getInstance().getFromField(id_setpoint).getCurrentValue();
        //Float gradient = old_type?rack.getId_gradient():ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getCurrentValue();
        Variable v = FSManager.getInstance().getFieldvariables().get(id_setpoint);
		if(v==null)
		{
			LoggerMgr.getLogger(FSCalculateDC.class).error("!!! variable "+id_setpoint+" not defined -> reload");
			FSManager.getInstance().getClock().loadcache();
			v = FSManager.getInstance().getFieldvariables().get(id_setpoint);
			if(v==null)
			{
				LoggerMgr.getLogger(FSCalculateDC.class).error("!!! variable "+id_setpoint+" not defined again");
				return null;	
			}
		}
        v.getRetriever().retrieve(v);
        return v;
    }
    public static void setRack(FSRack rack, String result, SetContext setContext) throws Exception
    {
        //tipo di centrale
    	//se � di tipo old, i valori idmin,idmax e gradiente, rappresentano i valori assoluti di min,max e gradiente
    	//se � di tipo nuovo i valori rappresentano gli id delle variabili, e i valori assoluti vengono letti dal campo
    	boolean old_type = "old".equalsIgnoreCase(rack.getAux())?true:false;
    	
    	//retrieve min,max e gradiente
    	int id_setpoint = rack.getId_setpoint().intValue();
    	Variable v = getRackSetpointVar(rack);
    	if(v == null)
    		return;
    	if(Float.isNaN(v.getCurrentValue()))
        	return;
        Float actual_set = v.getCurrentValue();
        Float gradient;
        if(old_type)
        {
        	gradient = rack.getId_gradient();
        }
        else
        {
            v = FSManager.getInstance().getFieldvariables().get(rack.getId_gradient().intValue());
    		if(v==null)
    		{
    			LoggerMgr.getLogger(FSCalculateDC.class).error("!!! variable "+rack.getId_gradient()+" not defined -> reload");
    			FSManager.getInstance().getClock().loadcache();
    			v = FSManager.getInstance().getFieldvariables().get(rack.getId_gradient());
    			if(v==null)
    			{
    				LoggerMgr.getLogger(FSCalculateDC.class).error("!!! variable "+rack.getId_gradient()+" not defined again");
    				return;	
    			}
    		}
            v.getRetriever().retrieve(v);
            if(Float.isNaN(v.getCurrentValue()))
            	return;
            gradient = v.getCurrentValue();
        }
        Float new_set = null;

        if (!INVARIATE.equals(result))
        {
            //variare set +
            if (INCREMENT.equals(result))
            {
                //new_set = actual_set + gradient;
            	Float tmp_actual_set = actual_set*10; 
            	Float tmp_gradient = gradient*10;
            	Float tmp_new_set = tmp_actual_set + tmp_gradient;
            	new_set = tmp_new_set/10;

                if (canSetRack(new_set, rack,old_type))
                {
                    prepareSet(id_setpoint, new_set, setContext);
                    if(!rack.isNewAlg())
                    	rack.changeSetpoint(new_set);
                    //log che il set viene incrementato FS03 {nome centrale}{vecchio set}{nuovo set}
                    EventMgr.getInstance().info(1,"System","fs","FS03",new Object[]{FSManager.getInstance().getRackDescription(rack),actual_set,new_set});
                }
            }

            // variare set -
            else if (DECREMENT.equals(result))
            {
                //new_set = actual_set - gradient;
            	Float tmp_actual_set = actual_set*10; 
            	Float tmp_gradient = gradient*10;
            	Float tmp_new_set = tmp_actual_set - tmp_gradient;
            	new_set = tmp_new_set/10;

                if (canSetRack(new_set, rack,old_type))
                {
                    prepareSet(id_setpoint, new_set, setContext);
                    if(!rack.isNewAlg())
                    	rack.changeSetpoint(new_set);
                    //log che il set viene decrementato FS03 {nome centrale}{vecchio set}{nuovo set}
                    EventMgr.getInstance().info(1,"System","fs","FS03",new Object[]{FSManager.getInstance().getRackDescription(rack),actual_set,new_set});
                }
            }
            //offline+solenoidnullerror(old mpxpro HW doesn't have solenoid variable)
            else //caso offline set minimo - cambio spec - diminuisco gradualmente 
            {
                //new_set = actual_set - gradient;
            	Float tmp_actual_set = actual_set*10; 
            	Float tmp_gradient = gradient*10;
            	Float tmp_new_set = tmp_actual_set - tmp_gradient;
            	new_set = tmp_new_set/10;

                if (canSetRack(new_set, rack,old_type))
                {
                    prepareSet(id_setpoint, new_set, setContext);
                    if(!rack.isNewAlg())
                    	rack.changeSetpoint(new_set);
                    //log che il set viene decrementato FS03 {nome centrale}{vecchio set}{nuovo set}
//                    EventMgr.getInstance().info(1,"System","fs","FS03",new Object[]{rack.getDescription(),actual_set,new_set});
                    if(OFFLINE.equals(result))
                    	EventMgr.getInstance().warning(1,"System","fs","FS04",new Object[]{FSManager.getInstance().getRackDescription(rack),new_set});
                    else if(SOLENOIDNULL.equals(result))
                    	EventMgr.getInstance().warning(1,"System","fs","FS13",new Object[]{FSManager.getInstance().getRackDescription(rack),actual_set,new_set});
                }

//                FSManager.securityRackSet(rack);
//                // FS04 {nome centrale} logare siamo nel caso offline. Entro in modalit� sicurezza e setto il minimo
//            	Float min = old_type?rack.getId_minset():ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getCurrentValue();
//            	EventMgr.getInstance().warning(1,"System","fs","FS04",new Object[]{rack.getDescription(),min});
            }
        }
        else
        {
            //se serve logare che il set � rimasto inalterato
        }
    }

    private static boolean canSetRack(Float set, FSRack rack, boolean oldtype) throws Exception
    {
//        Float min_set = oldtype?rack.getId_minset():ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getCurrentValue();
//        Float max_set = oldtype?rack.getId_maxset():ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getCurrentValue();
    	Float min_set;
    	Float max_set;
    	if(oldtype)
    	{
			min_set = rack.getId_minset();
			max_set = rack.getId_maxset();    		
    	}
    	else
    	{
    		Variable v1 = FSManager.getInstance().getFieldvariables().get(rack.getId_minset().intValue());
    		Variable v2 = FSManager.getInstance().getFieldvariables().get(rack.getId_maxset().intValue());
    		if(v1==null||v2==null)
    		{
    			LoggerMgr.getLogger(FSCalculateDC.class).error("!!! variable "+rack.getId_minset()+" or "+rack.getId_minset()+" not defined -> reload");
    			FSManager.getInstance().getClock().loadcache();
    			return false;
    		}
    		v1.getRetriever().retrieve(v1);
    		v2.getRetriever().retrieve(v2);
    		if(Float.isNaN(v1.getCurrentValue()) || Float.isNaN(v2.getCurrentValue()))
    			return false;
			min_set = v1.getCurrentValue();
			max_set = v2.getCurrentValue();
    	}
    	
        if ((set >= min_set) && (set <= max_set))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static void prepareSet(int idvar, Float set, SetContext setContext)
    {
        setContext.addVariable(idvar, set);
    }

    public static void executeSet(SetContext setContext)
    {
        setContext.setUser("Floating Suction Pressure Control");
        setContext.setCallback(new FSSetCallback());
        SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(FSCalculateDC.class.getName()));
    }
}

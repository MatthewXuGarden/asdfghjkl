package com.carel.supervisor.presentation.polling;



import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;



public class PollingStatusMrg {

	private static PollingStatusMrg me = new PollingStatusMrg();
	private ArrayList recordPolling = new ArrayList();
	private static SimpleDateFormat formato = new SimpleDateFormat("HH:mm:59");
	private static SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
	//private static SimpleDateFormat formatoHTML = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat formatoTimeHTML = new SimpleDateFormat("HH:mm");
	
	private PollingStatusMrg()
    {
    }
	

	public static PollingStatusMrg getInstance()
    {
        return me;
    }
	
	private void initRecord(){
	 try {	
	//Estraggo tutti i siti associati a fasce con current_row valorizzato o meno
    // a seconda della data corrente
    //- Se current_row è null allora devo fare insert in rmpollingstatus
    //- Se current_row è valorizzato allora vado in update in rmpollingstatus
	//- A.id è id della tabella rmpollingstatus
   String sql = "select rmtimesite.idsite,rmtimesite.idrmtimetable,A.id,A.current_row," +
			"lastdialup,A.slot_1,A.slot_2,A.slot_3,A.slot_4,A.lastupdate,cfsite.name " +
			"from ( " +
			" select rmtimesite.idsite,rmtimesite.idrmtimetable,slot_1,slot_2,slot_3," +
			" slot_4,current_row,rmpollingstatus.id,rmpollingstatus.lastupdate from rmtimesite " +
			" inner join rmpollingstatus on rmtimesite.idsite = rmpollingstatus.idsite " +
			" where status='true' and current_row = ? ) as A " +
			" right join rmtimesite on A.idsite = rmtimesite.idsite " +
			" inner join cfsite on cfsite.idsite=rmtimesite.idsite where status='true' order by idsite";
	Object[] values = new Object[1];
	Date today = Calendar.getInstance().getTime();
	//SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	String data=formatoData.format(today);
	values[0] = data;
	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, values);
    GroupPollingStatus polling = null;
    Record record = null;
    
    //Per saper i siti estratti per poi ottenere tutti i parametri delle singole fascie orarie
    ArrayList tmpId = new ArrayList();
    
     for (int i = 0; i < recordset.size(); i++)
     {
            record = recordset.get(i);
            //int id = ((Integer) record.get("idsite")).intValue();
            tmpId.add((Integer) record.get("idsite"));
            polling = new GroupPollingStatus(record);
            recordPolling.add(polling);
     }
     
     StringBuffer numIDsite = new StringBuffer();
     for(int i=0; i < tmpId.size(); i++)
    	 numIDsite.append(","+tmpId.get(i));
     
     if(numIDsite.length() > 0){	//Per evitare che la query successiva vada in errore se non ci sono siti configurati o impostati
    	 numIDsite.deleteCharAt(0);
     }
     else
    	 numIDsite.append("-1");
     
     
     //id estratto è quello di rmtime
     sql="select rmtimesite.idsite,rmtimetable.name,rmtime.* from rmtimesite " +
     	" inner join rmtime on rmtimesite.idrmtimetable=rmtime.idrmtimetable " +
     	" inner join rmtimetable on rmtimetable.idrmtimetable=rmtime.idrmtimetable " +
     	" where rmtimesite.idsite in (" +numIDsite.toString()+") order by rmtimesite.idsite,nslot";
     
     recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
     record = null;
     
     //Associo ad ogni idsite il nome della fascia e la lista di tutti gli slot di
     //tempo ad esso associato
     for (int i = 0; i < recordset.size(); i++)
     {
         record = recordset.get(i);
         int id = ((Integer) record.get("idsite")).intValue();
         for(int y=0; y < recordPolling.size(); y++){
            polling = (GroupPollingStatus) recordPolling.get(y);
            if(polling.getIdsite() == id){
            	polling.setName(UtilBean.trim(record.get("name")));
            	polling.addTimeRow(record);
            	break;
            }
         }
     }
     
	 }catch(Exception e){
		 Logger logger = LoggerMgr.getLogger(this.getClass());
         logger.error(e);
 	  }
    }
	

/*	
	public void refreshPollingStatus()
    {
		recordPolling.clear();
		initRecord();
    }
*/

	//Ciò che deve fare la run
	public void run()
    {
		recordPolling.clear();
		initRecord();
		createRecordValue();
    }
	
	
	public void createRecordValue(){
		GroupPollingStatus polling = null;
		PollingRecord recordpolling = null;
		for(int i=0; i<recordPolling.size(); i++){
			recordpolling = null;
			polling = (GroupPollingStatus) recordPolling.get(i);
			//System.out.println("->PrintIDSITE: "+polling.getIdsite()+"<- "+"->PrintSITENAME: "+polling.getSitename()+"<-");
			//System.out.println("->PrintNOME FASCIA: "+polling.getName());
			Timestamp lastDial = polling.getLastdialup();
			int slot_ok = 0; //Identificativo di slot che rappresenta l'ora di connessione dell'ultimo dialup
			if(lastDial != null){ //Se la data dell'ultima connessione è odierna colloco l'ora di connessione in uno degli slot per il sito
				long timeLast = lastDial.getTime();
				Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();	//Data odierna
				cal.setTimeInMillis(timeLast);
				Date d_lastdialup = cal.getTime();	//Data ultimo dialup
				//System.out.println("Now: "+now + "Last: "+d_lastdialup);
				String now_txt = formatoData.format(now); 					//Data odierna formattata
				String d_lastdialup_txt = formatoData.format(d_lastdialup); //Data lastdialup formattata
				String time_lastdialup_txt = formato.format(d_lastdialup);  //Ora ultimo dialup
				if(now_txt.equalsIgnoreCase(d_lastdialup_txt)){	//Se la data dell'ultimo dialup è odierna posiziono l'ora in una delle fascie configurate per il sito
					TimeRow timeRow = null;
					Time timerow_from = null;
					Time timerow_to = null;
					Time timerow_lastdialup = null;
					slot_ok = 0;
					for(int z=0; z < polling.getTimeRow().size(); z++){
						timeRow = (TimeRow) polling.getTimeRow().get(z);
						timerow_from = Time.valueOf(timeRow.getHour_from()+":"+timeRow.getMinute_from()+":00");
						timerow_to = Time.valueOf(timeRow.getHour_to()+":"+timeRow.getMinute_to()+":00");
						timerow_lastdialup = Time.valueOf(time_lastdialup_txt);
						//System.out.println("PrintSLOT: "+timeRow.getNslot()+" "+timeRow.getHour_from()+":"+timeRow.getMinute_from()+" - "+timeRow.getHour_to()+":"+timeRow.getMinute_to());
						if(timerow_from.compareTo(timerow_lastdialup) <=0 && timerow_to.compareTo(timerow_lastdialup) >= 0){ //Se dialup è collocabile dentro ad una fascia
							//System.out.println("Sono dentro la fascia: "+timerow_lastdialup+ " slot: "+timeRow.getNslot());
							slot_ok = timeRow.getNslot();
							break;
						}
					}
					//Inserimento o update se lastdialup è collocabile o meno in uno slot
					recordpolling = new PollingRecord();
					recordpolling.setIdsite(polling.getIdsite());
					recordpolling.setId(polling.getId());
					recordpolling.setSlot_1(polling.getPollingStatus().getSlot_1());
					recordpolling.setSlot_2(polling.getPollingStatus().getSlot_2());
					recordpolling.setSlot_3(polling.getPollingStatus().getSlot_3());
					recordpolling.setSlot_4(polling.getPollingStatus().getSlot_4());
					if(slot_ok != 0){
						recordpolling.setSlotTimestamp(polling.getLastdialup(),slot_ok);
						if(slot_ok == 1){ //Quando sono in insert devo settare lo slot corrispondente con il fatto ke il locale ha chiamato per settare poi il led verde
							polling.getPollingStatus().setSlot_1(polling.getLastdialup());
							polling.getPollingStatus().setSlot_2(polling.getPollingStatus().getSlot_2());
							polling.getPollingStatus().setSlot_3(polling.getPollingStatus().getSlot_3());
							polling.getPollingStatus().setSlot_4(polling.getPollingStatus().getSlot_4());
						}
						if(slot_ok == 2){
							polling.getPollingStatus().setSlot_2(polling.getLastdialup());
							polling.getPollingStatus().setSlot_1(polling.getPollingStatus().getSlot_1());
							polling.getPollingStatus().setSlot_3(polling.getPollingStatus().getSlot_3());
							polling.getPollingStatus().setSlot_4(polling.getPollingStatus().getSlot_4());
						}
						if(slot_ok == 3){
							polling.getPollingStatus().setSlot_3(polling.getLastdialup());
							polling.getPollingStatus().setSlot_1(polling.getPollingStatus().getSlot_1());
							polling.getPollingStatus().setSlot_2(polling.getPollingStatus().getSlot_2());
							polling.getPollingStatus().setSlot_4(polling.getPollingStatus().getSlot_4());
						}
						if(slot_ok == 4){
							polling.getPollingStatus().setSlot_4(polling.getLastdialup());
							polling.getPollingStatus().setSlot_1(polling.getPollingStatus().getSlot_1());
							polling.getPollingStatus().setSlot_3(polling.getPollingStatus().getSlot_3());
							polling.getPollingStatus().setSlot_2(polling.getPollingStatus().getSlot_2());
						}
					}
					recordpolling.mod_type(polling.getCurrent_row()); //Eseguo save o update
				}
				
			}
			
		}
	}


	public ArrayList getRecordPolling() {
		return recordPolling;
	}
	
	public String createHTMLTable(String motorefermo,String noSiteConfig){
		//Se l'utente effettua modifiche alla fascia oraria e inserisce uno slot precedente all'ora attuale
		//allora tale slot deve essere grigio perchè considero solo gli slot dall'ora attuale in poi
		String sql = "select idrmtimetable,lastupdate from rmtimetable where to_date(lastupdate,'YYYY/mm/dd') = ? ";
		Object[] values = new Object[1];
		Date today = Calendar.getInstance().getTime();
		String data=formatoData.format(today);
		values[0] = data;
		Map slotupdatati = new HashMap();	//Contiene le fascie modificate nella giornata odierna
		try {
			Record record = null;
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, values);
			int idrmtimetable = 0;
			Timestamp lastupdate = null;
			for(int i=0; i<recordset.size(); i++){
				 record = recordset.get(i);
				 idrmtimetable = ((Integer) record.get("idrmtimetable")).intValue();
				 lastupdate = ((Timestamp) record.get("lastupdate"));
				 slotupdatati.put(new Integer(idrmtimetable),lastupdate);
			}
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
		
		sql = "select idsite,lastupdate from rmtimesite where to_date(lastupdate,'YYYY/mm/dd') = ? and status = 'true'";
		values = new Object[1];
		values[0] = data;
		Map siteupdatati = new HashMap();	//Contiene dei siti modificate nella giornata odierna
		try {
			Record record = null;
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, values);
			int idsite = 0;
			Timestamp lastupdate = null;
			for(int i=0; i<recordset.size(); i++){
				 record = recordset.get(i);
				 idsite = ((Integer) record.get("idsite")).intValue();
				 lastupdate = ((Timestamp) record.get("lastupdate"));
				 siteupdatati.put(new Integer(idsite),lastupdate);
			}
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
		
		StringBuffer tableHTML = new StringBuffer();
		GroupPollingStatus polling = null;
		Time timerow_now = Time.valueOf(formato.format(Calendar.getInstance().getTime()));
		for(int i=0; i < recordPolling.size(); i++){
			polling = (GroupPollingStatus) recordPolling.get(i);
			int nslot = polling.getTimeRow().size();
			
			
			tableHTML.append("<tr><td>");
			tableHTML.append("<table class='table' border='0' width='100%' cellspacing='1' cellpadding='1'><tbody>");
			tableHTML.append("<tr>");
			tableHTML.append("<td width='3%'   class='standardTxt' align='left'><b>"+(i+1)+"</td>");
			tableHTML.append("<td width='37%'  class='standardTxt' align='left'>"+polling.getSitename()+"</td>");
			tableHTML.append("<td width='29%'  class='standardTxt' align='left'>"+polling.getName()+"</td>");
			
			if(nslot >= 1){
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>"+getLedStatus(polling,polling.getPollingStatus().getSlot_1(),1,timerow_now,slotupdatati,siteupdatati)+"</td>");
			}
			else
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>&nbsp</td>");
			if(nslot >= 2){
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>"+getLedStatus(polling,polling.getPollingStatus().getSlot_2(),2,timerow_now,slotupdatati,siteupdatati)+"</td>");
			}
			else
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>&nbsp</td>");
			if(nslot >= 3){
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>"+getLedStatus(polling,polling.getPollingStatus().getSlot_3(),3,timerow_now,slotupdatati,siteupdatati)+"</td>");
			}
			else
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>&nbsp</td>");
			if(nslot == 4){
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>"+getLedStatus(polling,polling.getPollingStatus().getSlot_4(),4,timerow_now,slotupdatati,siteupdatati)+"</td>");
			}
			else
				tableHTML.append("<td width='8%'  class='standardTxt' align='left'>&nbsp</td>");
			
			
			tableHTML.append("</tr>");
			tableHTML.append("</tbody></table></td>");
			tableHTML.append("</tr>");
			
		}
		
		if(!ThreadController.getInstance().getThreadStarted().isAlive())
		{
			tableHTML.append("<table class='table' border='0' width='100%' cellspacing='1' cellpadding='1'>");
			tableHTML.append("<tr>");
			tableHTML.append("<td colspan=6 class='standardTxt' style='background-Color:#FF0000'><b><div align='center'>"+motorefermo+"</div></td></tr>");
			tableHTML.append("</table>");
		}
			
		if(tableHTML.length() == 0){
			tableHTML.append("<table class='table' border='0' width='100%' cellspacing='1' cellpadding='1'>");
			tableHTML.append("<tr>");
			tableHTML.append("<td colspan=6 class='tdTitleTable'><div align='center'>"+noSiteConfig+"</div></td></tr>");
			tableHTML.append("</table>");
		}
		
		return tableHTML.toString();
	}
	
	public String getLedStatus(GroupPollingStatus polling,Timestamp slot,int nslot,Time now,Map slotupdatati,Map siteupdatati){
		//L0.gif led grigio
		//L1.gif led verde
		//L2.gif led rosso
		//L3.gif led blu
		
		TimeRow timeRow = null;
		Time timerow_from = null;
		Time timerow_to = null;
		
		timeRow = (TimeRow) polling.getTimeRow().get(nslot-1);
		timerow_from = Time.valueOf(timeRow.getHour_from()+":"+timeRow.getMinute_from()+":59");
		timerow_to = Time.valueOf(timeRow.getHour_to()+":"+timeRow.getMinute_to()+":59");
				
		if(nslot == 1){
			//timeRow = (TimeRow) polling.getTimeRow().get(nslot-1);
			//timerow_from = Time.valueOf(timeRow.getHour_from()+":"+timeRow.getMinute_from()+":59");
			//timerow_to = Time.valueOf(timeRow.getHour_to()+":"+timeRow.getMinute_to()+":59");
			if(!ThreadController.getInstance().getThreadStarted().isAlive()){ //Se il motore non è attivo il led è grigio e non rosso
				EventMgr.getInstance().error(new Integer(1),"System", "Polling","POL12",null);
				return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
			}
			if(polling.getPollingStatus().getSlot_1() == null){
				if(timerow_from.compareTo(now) <=0 && timerow_to.compareTo(now) >= 0){ //Sono dentro ad una fascia	
					return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) > 0 && timerow_to.compareTo(now) > 0){ //Non sono ancora arrivato alla fascia	
					return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) < 0 && timerow_to.compareTo(now) < 0){ //Ho superato la fascia
					if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se è stata impostata una fascia oraria precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se il sito è stato abbinato ad un'altra fascia allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
			}
			else{
				if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_1()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(t_update) >=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_1())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
							return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_1()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(timerow_to) <=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_1())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
						return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_1())+"</div>";
			}
		}
		
		if(nslot == 2){
			if(!ThreadController.getInstance().getThreadStarted().isAlive()){ //Se il motore non è attivo il led è grigio e non rosso
				EventMgr.getInstance().error(new Integer(1),"System", "Polling","POL12",null);
				return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
			}
			if(polling.getPollingStatus().getSlot_2() == null){
				if(timerow_from.compareTo(now) <=0 && timerow_to.compareTo(now) >= 0){ //Sono dentro ad una fascia	
					return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) > 0 && timerow_to.compareTo(now) > 0){ //Non sono ancora arrivato alla fascia	
					return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) < 0 && timerow_to.compareTo(now) < 0){ //Ho superato la fascia
					if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se è stata impostata una fascia oraria precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se il sito è stato abbinato ad un'altra fascia allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
			}
			else{
				if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_2()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(t_update) >=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_2())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
							return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_2()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(timerow_to) <=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_2())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
						return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_2())+"</div>";
			}
		}
		
		if(nslot == 3){
			if(!ThreadController.getInstance().getThreadStarted().isAlive()){ //Se il motore non è attivo il led è grigio e non rosso
				EventMgr.getInstance().error(new Integer(1),"System", "Polling","POL12",null);
				return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
			}
			if(polling.getPollingStatus().getSlot_3() == null){
				if(timerow_from.compareTo(now) <=0 && timerow_to.compareTo(now) >= 0){ //Sono dentro ad una fascia	
					return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) > 0 && timerow_to.compareTo(now) > 0){ //Non sono ancora arrivato alla fascia	
					return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) < 0 && timerow_to.compareTo(now) < 0){ //Ho superato la fascia
					if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se è stata impostata una fascia oraria precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se il sito è stato abbinato ad un'altra fascia allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
			}
			else{
				if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_3()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(t_update) >=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_3())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
							return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_3()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(timerow_to) <=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_3())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
						return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_3())+"</div>";
			}
		}
		
		if(nslot == 4){
			if(!ThreadController.getInstance().getThreadStarted().isAlive()){ //Se il motore non è attivo il led è grigio e non rosso
				EventMgr.getInstance().error(new Integer(1),"System", "Polling","POL12",null);
				return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
			}
			if(polling.getPollingStatus().getSlot_4() == null){
				if(timerow_from.compareTo(now) <=0 && timerow_to.compareTo(now) >= 0){ //Sono dentro ad una fascia	
					return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) > 0 && timerow_to.compareTo(now) > 0){ //Non sono ancora arrivato alla fascia	
					return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(timerow_from.compareTo(now) < 0 && timerow_to.compareTo(now) < 0){ //Ho superato la fascia
					if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se è stata impostata una fascia oraria precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
						Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
						Time t_update = Time.valueOf(formato.format(t));
						if(t_update.after(timerow_to)) //Se il sito è stato abbinato ad un'altra fascia allora il led va grigio perchè la fascia non viene presa in considerazione  
							return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
			}
			else{
				if(slotupdatati.containsKey(new Integer(timeRow.getIdrmtimetable()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) slotupdatati.get(new Integer(timeRow.getIdrmtimetable()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_4()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(t_update) >=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_4())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
							return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				if(siteupdatati.containsKey(new Integer(polling.getIdsite()))){ //Se però in data odierna c'è stata una modifica alla fascia allora la fascia va grigria
					Timestamp t = (Timestamp) siteupdatati.get(new Integer(polling.getIdsite()));
					Time t_update = Time.valueOf(formato.format(t));
					Time dialup = Time.valueOf(formato.format(polling.getPollingStatus().getSlot_4()));
					if(t_update.after(timerow_to)) //Se è stata impostata uno slot precedente all'ora di update allora il led va grigio perchè la fascia non viene presa in considerazione  
						return "<div align='center'><img src=images/led/L0.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					if((now.before(timerow_to) && !(dialup.after(timerow_to)))){ //Led blu perchè sono nello sotto-slot dafinito dall'ora dell'update slot e fine slot non chiama
						if(dialup.compareTo(timerow_to) <=0)
							return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_4())+"</div>";
						return "<div align='center'><img src=images/led/L3.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
					}
					if(!(dialup.after(t_update) && dialup.before(timerow_to)))
						return "<div align='center'><img src=images/led/L2.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>&nbsp</div>";
				}
				return "<div align='center'><img src=images/led/L1.gif alt='"+formatoTimeHTML.format(timerow_from)+" - "+formatoTimeHTML.format(timerow_to)+"'><br>"+formatoTimeHTML.format(polling.getPollingStatus().getSlot_4())+"</div>";
			}
		}
		
		return "";
	}	
}

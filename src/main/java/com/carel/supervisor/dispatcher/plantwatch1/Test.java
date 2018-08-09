package com.carel.supervisor.dispatcher.plantwatch1;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Test {
	//enum TVarType {UNKNOWN, TYPE_DIGITAL, TYPE_INTEGER, TYPE_REAL, TYPE_EVENT};
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		 try
		 {
			 
			 
			 Map mp_UserPassword = Collections.synchronizedMap( new TreeMap());
			 PlantWatchUser newrec = new PlantWatchUser();
			 newrec.Ident = 1;
			 newrec.UserName = "RemoteUser";
			 newrec.Password = "4";
			 
			 
			 mp_UserPassword.put( Integer.valueOf(newrec.Ident), newrec);
			 PlantWatch pwd = new PlantWatch("PlantWatchThread", 
					 						 "D-Link DU-562M External Modem",
					 						 null);
			 
			 Date to = new Date();
			 
			 Date from = new Date(to.getTime() - (60*1000)*60*24*10);
			 
			
			
			 PlantWatchUnitVars pwvs = new PlantWatchUnitVars(); 
			 pwd.AddUserPasswordList( mp_UserPassword);
			 if(pwd.ConnectToDevice( 1, 1))
				 pwd.start(); 
			 else
				 return;
			 
			  while(!pwd.isSyncDone())
			 {
				 Thread.sleep(1000);
				 
			 }
			 int count = 1;
			 PlantWatchAlarmList al = new PlantWatchAlarmList();
			 pwd.GetAllarmsFormDate( "C:/swdept_prj/plantvisorpro/developments/applications/dispatcher/bin/xml",
					 				 "alarms.xml",
					 				 new Date(0),
					 				 al
					 				);
			 
			 PlantWatchHistoryVars pwhvs = new PlantWatchHistoryVars();
			 pwd.ReadHistoryVar("C:/swdept_prj/plantvisorpro/developments/applications/dispatcher/bin/xml",
				 		from.getTime(), 
				 		to.getTime(),
				 		pwhvs);
			 /*
			 pwd.ReadHistoryVar("C:/swdept_prj/plantvisorpro/developments/applications/dispatcher/bin/xml",
							    from.getTime(), 
						 		to.getTime(),
						 		1,
						 		pwhvs);
		
			 
			  
			 pwd.ReadHistoryVar("C:/swdept_prj/plantvisorpro/developments/applications/dispatcher/bin/xml",
					 		from.getTime(), 
					 		to.getTime(),
					 		pwhvs);*/
			
			 VarDouble var = new VarDouble(0);
			 if(pwd.ReadVar( 4,3,2,var))
			 {
							 System.out.println(" Unitá MPX ");
							 System.out.println("Valore SetPoint= "+var.getVal().doubleValue());
			 }else
				 System.out.println("Errore lettura Valore SetPoint");
			 var.setVal( (double)2.8);
			 
			 if(pwd.SendVar( 4,3,2,var.getVal()))
			 {
							 System.out.println(" Unitá MPX ");
							 System.out.println("Valore SetPoint= "+var.getVal().doubleValue());
			 }else
				 System.out.println("Errore lettura Valore SetPoint");
		/* 
			while(count>0)
			 {
				 for(int i=1; i<=4;i++)
				 {
					 VarDouble var = new VarDouble(0);
					 for(int j=1;j<=100;j++)
					 {
						 if(pwd.ReadVar( i,j,1,var))
						 {
							 System.out.println("Digitale Unitá = "+i);
							 System.out.println("Valore = "+var.getVal().byteValue());
						 }else
							 System.out.println("Errore lettura Analogica Unitá = "+i);
						 if(pwd.ReadVar( i,j,3,var))
						 {
							 System.out.println("Intera Unitá = "+i);
							 System.out.println("Valore = "+var.getVal().longValue()  );
						 }else
							 System.out.println("Errore letta intera Unitá = "+i);
						 if(pwd.ReadVar( i,j,2,var))
						 {
							 System.out.println("Analogica Unitá = "+i);
							 System.out.println("Valore = "+var.getVal().doubleValue()   );
						 }else
							 System.out.println("Errore Analogica Unitá = "+i);
						
					 }
					 Thread.sleep(1);
				 }
				 count --;
			 }
			 */
			 pwd.ReadUnitVar(1, 
			 		 "C:/swdept_prj/plantvisorpro/developments/applications/dispatcher/bin/xml",
			 		 pwvs);
	
			 pwd.DisconnectDevice();
			 pwd.CloseService();
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 
		 
		
	}

}

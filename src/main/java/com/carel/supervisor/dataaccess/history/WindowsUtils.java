package com.carel.supervisor.dataaccess.history;
import java.io.*;
import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;

public class WindowsUtils {
	
  public static String listRunningProcesses() {
	  StringBuffer str=new StringBuffer();
    try {
      String line;
      Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
      BufferedReader input = new BufferedReader
          (new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
          if (!line.trim().equals("")) {
              // keep only the process name
              
              String data[]=line.split(",");
              
              if(data[0].compareTo("\"postgres.exe\"")==0){
            	 str.append(data[0] +" - "+data[4]+" ");
            	    
              }
              
             
          }

      }
      input.close();
    }
    catch (Exception err) {
      err.printStackTrace();
    }
    return str.toString();
  }

  public static void main(String[] args){
      System.out.println( listRunningProcesses());
     
  }

 
}
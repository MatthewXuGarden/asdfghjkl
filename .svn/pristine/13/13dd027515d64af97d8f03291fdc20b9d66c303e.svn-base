package com.carel.supervisor.dataaccess.hs;


public class Test {


	public static void main(String[] args) {
	
		DataHs dataHs= CreateSqlHs.getDeleteData("cfaction",new String[]{"idaction","pvcode","idsite","code","actioncode","actiontype","isscheduled","template","parameters"}, new Object[]{new Integer(0)},new String[]{"<"},new String[]{"idaction"});
	
		System.out.println(dataHs.getSql());
	Object []objects=dataHs.getObjects();
	for(int i=0;i<objects.length;i++)
		System.out.println("\n ?="+objects[i]);
	}//for

}

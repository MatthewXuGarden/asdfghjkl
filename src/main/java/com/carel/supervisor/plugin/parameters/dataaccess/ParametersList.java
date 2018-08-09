package com.carel.supervisor.plugin.parameters.dataaccess;

import java.util.ArrayList;

import com.carel.supervisor.dataaccess.dataconfig.AbstractBindable;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ParametersList extends AbstractBindable {
	
	private String sql=
	"select var.idsite, var.idvariable, var.iddevice, \n"+
	"teVar.description as variabledescription, \n" +
	"teDev.description as devicedescription \n"+
	"from \n"+
		"( \n"+
	"		select *  \n"+
	        "from cfvariable \n"+
	        "where cfvariable.readwrite <>1  \n"+
				"and cfvariable.isactive='TRUE' \n"+
				"and cfvariable.iscancelled='FALSE' \n"+
	    ") var \n"+
	"inner join \n"+
		"cftableext teVar \n"+
			"on teVar.idsite = var.idsite \n"+
				"and teVar.tableid = var.idvariable \n"+
				"and teVar.tablename='cfvariable' \n"+
				"and teVar.languagecode='EN_en' \n"+
	"inner join \n"+
		"cftableext teDev \n"+
	" \n"+
			"on teDev.idsite = var.idsite \n"+
				"and teDev.tableid = var.iddevice \n"+
				"and teDev.tablename='cfdevice' \n"+
				"and teDev.languagecode='EN_en' \n";

	private ArrayList<Parameter> map=new ArrayList<Parameter>();
	
	public void clear() {
	}

	
	public ParametersList() throws Exception{
		
		
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < recordSet.size(); i++)
        {
        	Parameter p = new Parameter(recordSet.get(i));
        	map.add(p);
        }
        
	}
	
	public Parameter get(int i){
		return map.get(i);
	}
	
	public int size(){
		return map.size();
	}
	public ArrayList<Parameter> list(){
		return map;
	}
}

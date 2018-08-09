package com.carel.supervisor.ide.dc.DbModel;

import java.util.ArrayList;

import com.carel.supervisor.ide.dc.xmlDAO.ComboItemTag;


/**
 * @author pvpro team 20090422
 */

public class ComboDb
{
	private String varCode;
	private Integer idvarmdl;
	private ArrayList<Integer> values = new ArrayList<Integer>();
		
	public ComboDb(String varCode, ArrayList<ComboItemTag> pvalues)
	{
		this.varCode = varCode;	
		for (int i=0; i< pvalues.size();i++)
		{
			this.values.add(pvalues.get(i).getValue());
		}
	}
	
	
	public String getCode()
	{
		return this.varCode;
	}
	
	public ArrayList<Integer> getItemValues()
	{
		return this.values;
	}


	public Integer getIdvarmdl() {
		return idvarmdl;
	}


	public void setIdvarmdl(Integer idvarmdl) {
		this.idvarmdl = idvarmdl;
	}
	
}
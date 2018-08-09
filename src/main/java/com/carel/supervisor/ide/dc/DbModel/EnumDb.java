package com.carel.supervisor.ide.dc.DbModel;

import java.util.ArrayList;

import com.carel.supervisor.ide.dc.xmlDAO.EnumItemTag;


/**
 * @author pvpro team 20090422
 */

public class EnumDb
{
	private String varCode;
	private ArrayList<Integer> values = new ArrayList<Integer>();
		
	public EnumDb(String varCode, ArrayList<EnumItemTag> pvalues)
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
}
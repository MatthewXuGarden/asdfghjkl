package com.carel.supervisor.ide.dc.DbModel;

import java.util.ArrayList;

import com.carel.supervisor.ide.dc.xmlDAO.ComboItemTag;


/**
 * @author pvpro team 20090422
 */

public class CategoryDb
{
	private String catCode;
		
	public CategoryDb(String catCode)
	{
		this.catCode = catCode;	
	}
	
	
	public String getCode()
	{
		return this.catCode;
	}

}
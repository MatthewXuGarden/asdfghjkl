package com.carel.supervisor.director.ide;

import com.carel.supervisor.base.xml.XMLNode;

public interface IUpdater
{
	public abstract void insert(XMLNode xmlNode) throws Exception;
	public abstract void remove(Integer id) throws Exception;
	public abstract Integer update(XMLNode xmlNode) throws Exception;
	public abstract String getRetrieverSql();
}

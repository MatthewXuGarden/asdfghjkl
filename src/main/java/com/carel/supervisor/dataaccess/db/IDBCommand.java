package com.carel.supervisor.dataaccess.db;

import java.sql.Connection;


public interface IDBCommand
{
    public RecordSet execute(Connection connection) throws Exception;

    public String name();
}

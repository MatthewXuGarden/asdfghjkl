package com.carel.supervisor.field;

public class Status
{
    public final static byte DISABLE = 0;
    public final static byte ACTIVE = 1;
    private final long MASK = 0xFFFFFFFFFFFFFFFFL;
    private long status = 0;

    public Status(){
    }//Status
    public Status(long status){
    	this.status=status;
    }//Status
    
    public void setStatus(byte position, long type)
    {
        long mask = MASK ^ (((long) 1) << position);
        status = status & mask;
        status |= (type << position);
    } //setStatus

    public byte getStatus(byte position)
    {
        if ((status & (((long) 1) << position)) != 0)
            return ACTIVE;

        return DISABLE;
    } //getStatus

    public long getAllStatus()
    {
        return status;
    } //getStatus
    
    public void resetStatus(){
    	status=0;
    }//resetStatus
}

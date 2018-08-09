package com.carel.supervisor.presentation.bo.helper;

public class RemoveState
{
    private String message = "";
    private boolean canRemove = true;

    public RemoveState(String message, boolean canRemove)
    {
        this.message = message;
        this.canRemove = canRemove;
    }

    public boolean getCanRemove()
    {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove)
    {
        this.canRemove = canRemove;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}

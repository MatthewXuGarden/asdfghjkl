package com.carel.supervisor.controller.status;

public class StatusRegistry
{
    public static InvalidStatus invalidStatus = new InvalidStatus();
    public static ActiveStatus activeStatus = new ActiveStatus();
    public static ReadyToManageStatus readyToManageStatus = new ReadyToManageStatus();
    public static ToManageStatus toManageStatus = new ToManageStatus();
    public static AlreadyManagedStatus alreadyManagedStatus = new AlreadyManagedStatus();
    public static CalledOffStatus calledOffStatus = new CalledOffStatus();
    public static BlockedStatus blockedStatus = new BlockedStatus();

    public static AbstractStatus get(int status)
    {
        AbstractStatus state = null;

        switch (status)
        {
        case 0:
            state = invalidStatus;

            break;

        case 1:
            state = activeStatus;

            break;

        case 2:
            state = readyToManageStatus;

            break;

        case 3:
            state = toManageStatus;

            break;

        case 4:
            state = alreadyManagedStatus;

            break;

        case 5:
            state = calledOffStatus;

            break;

        case 6:
            state = blockedStatus;

            break;

        default:
            state = invalidStatus;
        }

        return state;
    }

    public static int get(AbstractStatus abstractState)
    {
        int value = 0;

        if (abstractState instanceof ActiveStatus)
        {
            value = 1;
        }
        else if (abstractState instanceof ReadyToManageStatus)
        {
            value = 2;
        }
        else if (abstractState instanceof ToManageStatus)
        {
            value = 3;
        }
        else if (abstractState instanceof AlreadyManagedStatus)
        {
            value = 4;
        }
        else if (abstractState instanceof CalledOffStatus)
        {
            value = 5;
        }
        else if (abstractState instanceof BlockedStatus)
        {
            value = 6;
        }

        return value;
    }
}

package com.carel.supervisor.controller.rule;

import java.util.List;


public class RuleMgr
{
    private static RuleMgr me = new RuleMgr();
    private MapExtens rules = new MapExtens();

    private RuleMgr()
    {
    }

    public static RuleMgr getInstance()
    {
        return me;
    }

    public void addRule(Rule rule)
    {
        rules.add(rule.getIdVar(), rule.getIdRule(), rule);
    }

    public Rule getRule(Integer idVar, Integer idRule)
    {
        return (Rule) rules.get(idVar, idRule);
    }

    public List getRulesList(Integer idVar)
    {
        return rules.getRules(idVar);
    }
    
    public void manualAcknowledge(Integer idVar) throws Exception
    {
        List list = getRulesList(idVar);
        Rule rule = null;

        if (null != list)
        {
            for (int i = 0; i < list.size(); i++)
            {
                rule = (Rule) list.get(i);

                if (null != rule)
                {
                    rule.manualAcknowledge();
                }
            }
        }
    }

    public void manualCancel(Integer idVar) throws Exception
    {
        List list = getRulesList(idVar);
        Rule rule = null;

        if (null != list)
        {
            for (int i = 0; i < list.size(); i++)
            {
                rule = (Rule) list.get(i);

                if (null != rule)
                {
                    rule.manualCancel();
                }
            }
        }
    }

    public void manualReset(Integer idVar) throws Exception
    {
        List list = getRulesList(idVar);
        Rule rule = null;

        if (null != list)
        {
            for (int i = 0; i < list.size(); i++)
            {
                rule = (Rule) list.get(i);

                if (null != rule)
                {
                    rule.manualReset();
                }
            }
        }
    }
        
    public boolean hasRules(Integer idVar)
    {
        return rules.hasKey1(idVar);
    }

    public void clear()
    {
        rules.clear();
    }
}

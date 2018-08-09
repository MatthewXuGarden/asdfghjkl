package com.carel.supervisor.test;

import com.carel.supervisor.action.ActionDebug;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.time.AlwaysValidity;
import com.carel.supervisor.field.Variable;

public class TestRule
{
    public TestRule()
    {
        super();
    }
    
    public static void main(String[] argv) throws Throwable
    {
    	BaseConfig.init();
    	VariableInfoTest vInfo = new VariableInfoTest(new Integer(1),new Integer(1),new Integer(2),
    			new Integer(1),4);
    	Variable v = new Variable(vInfo);
    	System.out.println("Caso 1 : Normale con Delay 0");
    	Rule rule = new Rule(new Integer(1), 0, v, new AlwaysValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a attiva->ready to manage->Tomanage->Already manage");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a calledOff->Invalid");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	System.out.println("--------------------------");
    	
    	
    	System.out.println("Caso 2 : Normale con Delay 1 sec");
    	rule = new Rule(new Integer(1), 1, v, new AlwaysValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a ready to manage->Tomanage->Already manage");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a calledOff->Invalid");
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	System.out.println("--------------------------");
    	
    	System.out.println("Caso 3");
    	rule = new Rule(new Integer(1), 1, v, new AlwaysValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active");
    	Thread.sleep(500);
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Torna invalida");
    	System.out.println("--------------------------");
    	
    	
    	System.out.println("Caso 4 : Sono fuori fascia");
    	rule = new Rule(new Integer(1), 1, v, new NeverValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a ready to manage");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Diventa invalida");
    	System.out.println("--------------------------");
    	
    	System.out.println("Caso 5 : Cancel1");
    	rule = new Rule(new Integer(1), 1, v, new AlwaysValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active");
    	Thread.sleep(500);
    	rule.manualCancel();
    	System.out.println("Passa a already managed");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a calledoff ->invalida");
    	System.out.println("--------------------------");
    	
    	System.out.println("Caso 6 : Cancel2");
    	rule = new Rule(new Integer(1), 0, v, new NeverValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active->readyTomanage");
    	rule.manualCancel();
    	System.out.println("Passa a already managed");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a invalida");
    	System.out.println("--------------------------");
    	
    	System.out.println("Caso 7 : Blocked");
    	rule = new Rule(new Integer(1), 0, v, new AlwaysValidity(), new ActionDebug());
    	System.out.println("La regola parte invalida");
    	rule.executeActions(System.currentTimeMillis());
    	v.setValue(new Float(1));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a active->readyTomanage->Tomanage->AlreadyManaged");
    	rule.manualReset();
    	
    	System.out.println("Passa a blocked");
    	Thread.sleep(1000);
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Non logga niente");
    	v.setValue(new Float(0));
    	rule.executeActions(System.currentTimeMillis());
    	System.out.println("Passa a invalida");
    	System.out.println("--------------------------");
    }
}

package com.xu.drools.rule.rule1;

import com.xu.drools.bean.Person;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;

/**
 * 使用kmodule的方式调用drools
 * /resources/META-INF/kmodule.xml
 */
public class rule1 {
    public static void main(final String[] args) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        System.out.println(kc.verify().getMessages().toString());
        execute(kc);
    }

    private static void execute(KieContainer kc) {
        KieSession ksession = kc.newKieSession("rule1KS");

        Person p1 = new Person(35, "xu", "handsome");
        FactHandle handle = ksession.insert(p1);
        ksession.fireAllRules();
        ksession.dispose();
    }
}

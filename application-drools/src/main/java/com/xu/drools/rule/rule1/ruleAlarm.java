package com.xu.drools.rule.rule1;

import com.alibaba.fastjson.JSON;
import com.xu.drools.bean.AlarmLevel;
import com.xu.drools.bean.Message;
import com.xu.drools.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 * 使用kmodule的方式调用drools
 * /resources/META-INF/kmodule.xml
 */

public class ruleAlarm {
    public static void main(final String[] args) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
//        System.out.println(kc.verify().getMessages().toString());
        execute(kc);
    }

    private static void execute(KieContainer kc) {
        KieSession ksession = kc.newKieSession("rule1KS");
        Message message = new Message();
        message.setAlarmLevel(AlarmLevel.ERROR);
        message.setSystemId("1");
        FactHandle handle = ksession.insert(message);
        ksession.fireAllRules();
        System.out.println(message.toString());
        ksession.dispose();
    }
}

package com.huang.pattern.observer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description: 观察者模式
 *  观察者模式三要素：目标，观察者，事件
 *  目标和观察者通过该模式实现了解耦，用的场景有很多：Springmvc，GUI页面的事件触发
 *  使用该模式的场景：
 *      1、在抽象模型中有两个方面，一方面依赖于另一方面，就可以使用该模式来实现解耦，两方面各自独立的改变和复用
 *      2、一方面触发另一方面，而且不需要知道另一方面有多少对象发生改变，改变了些什么，切勿形成循环依赖
 * 在springboot启动的时候就用到了观察者模式，最开始先初始化了listener：
 *  private void initialize(Object[] sources) {
 *       if (sources != null && sources.length > 0) {
 *           this.sources.addAll(Arrays.asList(sources));
 *        }
 *        this.webEnvironment = deduceWebEnvironment();
 *        setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
 *        setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
 *        this.mainApplicationClass = deduceMainApplicationClass();
 *   }
 *   然后在后面
 *   org.springframework.boot.SpringApplicationRunListener=org.springframework.boot.context.event.EventPublishingRunListener
 *
 *   private SpringApplicationRunListeners getRunListeners(String[] args) {
 *       Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
 *       return new SpringApplicationRunListeners(logger, getSpringFactoriesInstances(
 *       <b>SpringApplicationRunListener.class</b>, types, this, args));
 *   }
 *   这里实例化了EventPublishingRunListener（SpringApplicationRunListeners的实例）并放到了SpringApplicationRunListeners中，然后在EventPublishingRunListener的实例化方法中
 *   持有了所有的listerer，然后最终通过了EventPublishingRunListener中的initialMulticaster来实现事件的广播
 *
 *   public EventPublishingRunListener(SpringApplication application, String[] args) {
 *        this.application = application;
 *        this.args = args;
 *        this.initialMulticaster = new SimpleApplicationEventMulticaster();
 *        for (ApplicationListener<?> listener : application.getListeners()) {
 *           this.initialMulticaster.addApplicationListener(listener);
 *        }
 *    }
 * Created on 2018/2/9 11:36
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player extends AbstractPlayer implements Observer {

    private String name;

    @Override
    public void update(EventObject eo) {
        System.out.println(name + "收到报告:" + eo.getName() + "状态：" + eo.getEvent().name() + "，地点：" + eo.getLocation() + ", 血量：" + eo.getBloodFlow());
    }

    public static void main(String[] args){
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");
        Player p5 = new Player("p5");

        p1.attach(Event.DEAD, p2);
        p1.attach(Event.DEAD, p3);

        p1.attach(Event.HITTED, p4);
        p1.attach(Event.HITTED, p5);

        EventObject eo = new EventObject();
        eo.setName(p1.getName());
        eo.setBloodFlow(60);
        eo.setLocation("上甘岭");
        eo.setEvent(Event.HITTED);
        p1.trigger(eo);
    }

}

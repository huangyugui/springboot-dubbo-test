package com.huang.pattern.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Created on 2018/2/9 10:13
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public abstract class AbstractPlayer implements Subject {

    ConcurrentHashMap<Event, List<Observer>> map = new ConcurrentHashMap();

    @Override
    public void attach(Event event, Observer observer) {
        map.compute(event, (k, v) -> {
            if(v == null){
                v = new ArrayList();
                v.add(observer);
                return v;
            }
            v.add(observer);
            return v;
        });
    }

    @Override
    public void deattch(Event event, Observer observer) {
        map.computeIfPresent(event, (k, v) -> {
            v.remove(observer);
            return v;
        });
    }

    @Override
    public void trigger(EventObject eo) {
        map.get(eo.getEvent()).forEach(item -> item.update(eo));
    }
}

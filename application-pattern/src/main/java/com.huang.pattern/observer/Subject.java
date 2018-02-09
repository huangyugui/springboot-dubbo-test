package com.huang.pattern.observer;

/**
 * Description:
 * Created on 2018/2/9 10:06
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
public interface Subject {

    public void attach(Event event, Observer observer);

    public void deattch(Event event, Observer observer);

    public void trigger(EventObject event);

}

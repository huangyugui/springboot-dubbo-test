package com.huang.pattern.observer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Created on 2018/2/9 10:24
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class EventObject {
    private Event event;
    private String name;
    private int bloodFlow;
    private String location;
}

enum Event{
    HITTED, DEAD;
}
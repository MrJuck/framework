package com.juck.ioc.Test;

import com.juck.ioc.utils.BeanContainer;

public class Entry {
    public static void main(String[] args) {
        BeanContainer container = new BeanContainer("com.juck.ioc");
        Controller2 controller2 = container.getBean(Controller2.class);
        controller2.print();
    }
}

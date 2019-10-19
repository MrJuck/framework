package com.juck.ioc.Test;

import com.juck.ioc.annotation.Autowired;
import com.juck.ioc.annotation.Controller;

@Controller
public class Controller2 {
    @Autowired
    private Controller1 con1;
    public void print() {
        System.out.println("2");
        con1.print();
    }
}

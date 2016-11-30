package com.brctl.web;

import com.brctl.mq.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Ïû·ÑÕßcontroller
 * Created by Orclover on 2016-11-30.
 */
@Controller
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private Consumer consumer;

    @RequestMapping("")
    public String index() {
        return "producer";
    }
}

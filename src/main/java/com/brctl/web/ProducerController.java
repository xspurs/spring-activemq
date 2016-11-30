package com.brctl.web;

import com.brctl.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Éú²úÕßcontroller
 * Created by Orclover on 2016-11-30.
 */
@Controller
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private Producer producer;


    @RequestMapping("")
    public String index() {
        return "producer";
    }

    @RequestMapping("/message/{content}")
    @ResponseBody
    public HttpEntity<String> produceMessage(@PathVariable String content) {
        String replyMessage = producer.sendMessage(content);
        return new ResponseEntity<>(replyMessage, HttpStatus.OK);
    }
}

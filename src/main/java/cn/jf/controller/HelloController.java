package cn.jf.controller;

import cn.jf.Application;
import cn.jf.annotation.OperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求controller
 *
 * @author
 * @create 2017-12-06 17:21
 **/
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    @OperLog(value = "发送hello请求")
    public String sayHello() {
        logger.info("收到请求....");
        return "hello";
    }
}

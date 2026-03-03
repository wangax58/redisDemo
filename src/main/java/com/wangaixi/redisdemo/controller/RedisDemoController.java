package com.wangaixi.redisdemo.controller;

import com.wangaixi.redisdemo.test.RedisDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RESTful风格：GET(查)、POST(增)、PUT(改)、DELETE(删)
 */
@RestController // 等价于：@Controller + @ResponseBody，返回JSON数据
@RequestMapping("/api/redis") // 统一接口前缀
public class RedisDemoController {

    // 注入UserService，Spring自动装配
    @Autowired
    private RedisDemoService redisDemoService;

    /**
     * 请求方式：POST
     * 请求地址：/api/users
     * 请求体：JSON格式的User对象
     */
    @PostMapping("/demo")
    public void demo() {
        redisDemoService.demo();
    }

}
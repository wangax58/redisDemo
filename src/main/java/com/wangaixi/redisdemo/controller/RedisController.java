package com.wangaixi.redisdemo.controller;

import com.wangaixi.redisdemo.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class RedisController {

    @Resource
    private RedisService redisService;

    // 首页（默认显示字符串操作）
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ========== 字符串操作 ==========
    @PostMapping("/string/set")
    public String stringSet(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(defaultValue = "0") long expire,
            Model model
    ) {
        redisService.setString(key, value, expire);
        model.addAttribute("result", "字符串设置成功！Key: " + key + ", Value: " + value);
        model.addAttribute("activeTab", "string");
        return "index";
    }

    @GetMapping("/string/get")
    public String stringGet(@RequestParam String key, Model model) {
        Object value = redisService.getString(key);
        model.addAttribute("result", value == null ? "Key不存在！" : "Value: " + value);
        model.addAttribute("activeTab", "string");
        return "index";
    }

    @GetMapping("/string/delete")
    public String stringDelete(@RequestParam String key, Model model) {
        Boolean success = redisService.deleteString(key);
        model.addAttribute("result", success ? "字符串删除成功！Key: " + key : "删除失败，Key不存在！");
        model.addAttribute("activeTab", "string");
        return "index";
    }

    // ========== 哈希操作 ==========
    @PostMapping("/hash/put")
    public String hashPut(
            @RequestParam String key,
            @RequestParam String field,
            @RequestParam String value,
            Model model
    ) {
        redisService.hashPut(key, field, value);
        model.addAttribute("result", "哈希字段设置成功！Key: " + key + ", Field: " + field + ", Value: " + value);
        model.addAttribute("activeTab", "hash");
        return "index";
    }

    @GetMapping("/hash/get")
    public String hashGet(
            @RequestParam String key,
            @RequestParam String field,
            Model model
    ) {
        Object value = redisService.hashGet(key, field);
        model.addAttribute("result", value == null ? "Hash Key/Field不存在！" : "Value: " + value);
        model.addAttribute("activeTab", "hash");
        return "index";
    }

    @GetMapping("/hash/getAll")
    public String hashGetAll(@RequestParam String key, Model model) {
        Map<Object, Object> map = redisService.hashGetAll(key);
        model.addAttribute("result", map.isEmpty() ? "Hash Key不存在！" : "所有字段：" + map);
        model.addAttribute("activeTab", "hash");
        return "index";
    }

    @GetMapping("/hash/delete")
    public String hashDelete(
            @RequestParam String key,
            @RequestParam String field,
            Model model
    ) {
        redisService.hashDelete(key, field);
        model.addAttribute("result", "哈希字段删除成功！Key: " + key + ", Field: " + field);
        model.addAttribute("activeTab", "hash");
        return "index";
    }

    // ========== 列表操作 ==========
    @PostMapping("/list/leftPush")
    public String listLeftPush(
            @RequestParam String key,
            @RequestParam String value,
            Model model
    ) {
        Long size = redisService.listLeftPush(key, value);
        model.addAttribute("result", "列表左推成功！Key: " + key + ", Value: " + value + ", 当前列表长度：" + size);
        model.addAttribute("activeTab", "list");
        return "index";
    }

    @PostMapping("/list/rightPush")
    public String listRightPush(
            @RequestParam String key,
            @RequestParam String value,
            Model model
    ) {
        Long size = redisService.listRightPush(key, value);
        model.addAttribute("result", "列表右推成功！Key: " + key + ", Value: " + value + ", 当前列表长度：" + size);
        model.addAttribute("activeTab", "list");
        return "index";
    }

    @GetMapping("/list/leftPop")
    public String listLeftPop(@RequestParam String key, Model model) {
        Object value = redisService.listLeftPop(key);
        model.addAttribute("result", value == null ? "列表为空！" : "左弹结果：" + value);
        model.addAttribute("activeTab", "list");
        return "index";
    }

    @GetMapping("/list/rightPop")
    public String listRightPop(@RequestParam String key, Model model) {
        Object value = redisService.listRightPop(key);
        model.addAttribute("result", value == null ? "列表为空！" : "右弹结果：" + value);
        model.addAttribute("activeTab", "list");
        return "index";
    }

    @GetMapping("/list/range")
    public String listRange(@RequestParam String key, Model model) {
        List<Object> list = redisService.listRange(key, 0, -1);
        model.addAttribute("result", list.isEmpty() ? "列表为空！" : "列表内容：" + list);
        model.addAttribute("activeTab", "list");
        return "index";
    }
}
package com.wangaixi.redisdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangaixi.redisdemo.entity.User;
import com.wangaixi.redisdemo.service.UserService;
import com.wangaixi.redisdemo.util.ObjectRedisUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户Controller层，处理前端请求
 * RESTful风格：GET(查)、POST(增)、PUT(改)、DELETE(删)
 */
@RestController // 等价于：@Controller + @ResponseBody，返回JSON数据
@RequestMapping("/api/users") // 统一接口前缀
public class UserController {

    // 注入UserService，Spring自动装配
    @Autowired
    private UserService userService;
    @Resource
    private ObjectRedisUtils redisUtils;

    /**
     * 新增用户
     * 请求方式：POST
     * 请求地址：/api/users
     * 请求体：JSON格式的User对象
     */
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        boolean save = userService.save(user);
        return save ? new ResponseEntity<>(user, HttpStatus.CREATED) : ResponseEntity.badRequest().build();
    }

    /**
     * 根据ID查询用户
     * 请求方式：GET
     * 请求地址：/api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        String userIdKey = "user:id:" + id;
        // 先从redis获取
        User user = redisUtils.getObject(userIdKey, User.class);
        if (user == null) {
            // redis里没有再查库
            user = userService.getById(id);
            if (user != null) {
                // 库里有数据存入redis
                redisUtils.setObjectWithExpire(userIdKey, user, 1, TimeUnit.MINUTES);
            }
        }
        System.out.println(user);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 根据用户名查询用户
     * 请求方式：GET
     * 请求地址：/api/users/name/{username}
     */
    @GetMapping("/name/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 查询所有用户
     * 请求方式：GET
     * 请求地址：/api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> userList = userService.list();
        return ResponseEntity.ok(userList);
    }

    /**
     * 分页查询用户
     * 请求方式：GET
     * 请求地址：/api/users/page?pageNum=1&pageSize=10
     */
    @GetMapping("/page")
    public ResponseEntity<IPage<User>> pageUsers(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage<User> userPage = userService.page(page);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 根据ID修改用户
     * 请求方式：PUT
     * 请求地址：/api/users
     * 请求体：JSON格式的User对象（含ID）
     */
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        boolean update = userService.updateById(user);
        return update ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    /**
     * 根据ID删除用户
     * 请求方式：DELETE
     * 请求地址：/api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean remove = userService.removeById(id);
        return remove ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
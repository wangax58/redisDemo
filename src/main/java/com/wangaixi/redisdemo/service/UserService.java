package com.wangaixi.redisdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangaixi.redisdemo.entity.User;

/**
 * 用户Service接口，继承IService获得高级CRUD能力
 */
public interface UserService extends IService<User> {
    // 基础CRUD由IService实现，自定义业务方法在此声明（如：根据用户名查询用户）
    User getByUsername(String username);
}

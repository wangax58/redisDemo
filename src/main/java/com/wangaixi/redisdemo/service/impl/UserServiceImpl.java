package com.wangaixi.redisdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangaixi.redisdemo.entity.User;
import com.wangaixi.redisdemo.mapper.UserMapper;
import com.wangaixi.redisdemo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service // 标记为Spring服务组件，注入容器
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 自定义业务方法：根据用户名查询用户
     */
    @Override
    public User getByUsername(String username) {
        // LambdaQueryWrapper：MyBatis-Plus条件构造器，避免硬编码SQL字段
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username); // 等价于：where username = ?
        return baseMapper.selectOne(queryWrapper); // baseMapper即UserMapper
    }
}
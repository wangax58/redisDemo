package com.wangaixi.redisdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangaixi.redisdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper层，继承BaseMapper获得基础CRUD能力
 */
@Mapper // 标记为MyBatis的Mapper接口，Spring Boot自动扫描
public interface UserMapper extends BaseMapper<User> {
    // 无需编写基础方法，BaseMapper已实现：增、删、改、查单条、查列表、分页等
    // 自定义复杂SQL可在此添加方法，配合XML实现
}
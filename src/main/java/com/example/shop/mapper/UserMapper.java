package com.example.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shop.entity.User;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 
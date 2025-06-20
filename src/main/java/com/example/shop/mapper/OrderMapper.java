package com.example.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
} 
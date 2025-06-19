package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.entity.Order;

public interface OrderService extends IService<Order> {
    boolean createOrder(Long productId, Long userId);
} 
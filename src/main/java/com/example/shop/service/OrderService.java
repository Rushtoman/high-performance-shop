package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.entity.Order;
import java.util.List;

public interface OrderService extends IService<Order> {
    boolean createOrder(Long productId, Long userId);
    List<Order> getOrdersByUserId(Long userId);
    boolean cancelOrder(Long orderId);
}
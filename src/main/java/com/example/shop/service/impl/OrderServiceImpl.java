package com.example.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.entity.Order;
import com.example.shop.entity.Product;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.mapper.UserMapper;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public boolean createOrder(Long productId, Long userId) {
        // 校验用户是否存在
        if (userMapper.selectById(userId) == null) {
            return false;
        }
        // 查询商品
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStock() <= 0) {
            return false;
        }
        // 乐观锁扣库存
        int update = productMapper.updateStockById(productId, product.getStock(), product.getVersion());
        if (update == 0) {
            // 扣库存失败，可能超卖
            return false;
        }
        // 创建订单
        Order order = new Order();
        order.setProductId(productId);
        order.setUserId(userId);
        order.setStatus(0); // 未支付
        this.save(order);
        return true;
    }
} 
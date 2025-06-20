package com.example.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.shop.entity.Order;
import com.example.shop.entity.Product;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.mapper.UserMapper;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        order.setStatus(Order.STATUS_UNPAID); // 未支付
        this.save(order);
        // 加入Redis延迟队列，30分钟后到期
        String zsetKey = "order:delay:queue";
        long expireAt = System.currentTimeMillis() + 30 * 60 * 1000;
        redisTemplate.opsForZSet().add(zsetKey, order.getId().toString(), expireAt);
        return true;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != Order.STATUS_UNPAID) {
            return false;
        }
        // 更新订单状态为已取消
        order.setStatus(Order.STATUS_CANCELLED);
        boolean result = this.updateById(order);
        if (result) {
            // 恢复商品库存
            productMapper.increaseStockById(order.getProductId());
        }
        return result;
    }

    // 定时任务：每5秒轮询一次，处理到期未支付订单
    @Scheduled(fixedRate = 5000)
    public void processExpiredOrders() {
        String zsetKey = "order:delay:queue";
        long now = System.currentTimeMillis();
        Set<String> expiredOrderIds = redisTemplate.opsForZSet().rangeByScore(zsetKey, 0, now);
        if (expiredOrderIds != null && !expiredOrderIds.isEmpty()) {
            for (String orderIdStr : expiredOrderIds) {
                Long orderId = Long.valueOf(orderIdStr);
                // 取消订单
                cancelOrder(orderId);
                // 从ZSet移除
                redisTemplate.opsForZSet().remove(zsetKey, orderIdStr);
            }
        }
    }
} 
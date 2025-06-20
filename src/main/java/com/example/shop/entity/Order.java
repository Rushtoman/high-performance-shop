package com.example.shop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    // 订单状态常量
    public static final int STATUS_UNPAID = 0;    // 未支付
    public static final int STATUS_PAID = 1;      // 已支付
    public static final int STATUS_CANCELLED = 2; // 已取消
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Long userId;
    private Integer status;
    private LocalDateTime createTime;
} 
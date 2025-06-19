package com.example.shop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product")
public class Product {
    @TableId
    private Long id;
    private String name;
    private Integer stock;
    private Double price;
    @Version
    private Integer version;
    private String description;
}
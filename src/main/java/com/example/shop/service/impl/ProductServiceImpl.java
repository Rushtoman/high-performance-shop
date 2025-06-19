package com.example.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.entity.Product;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PRODUCT_CACHE_KEY = "product:";

    @Override
    @Cacheable(value = "hotProduct", key = "#id")
    public Product getProductById(Long id) {
        String key = PRODUCT_CACHE_KEY + id;
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, Product.class);
            } catch (Exception e) {
                // ignore, fallback to db
            }
        }
        Product product = this.getById(id);
        if (product != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                redisTemplate.opsForValue().set(key, mapper.writeValueAsString(product));
            } catch (Exception e) {
                // ignore
            }
        }
        return product;
    }

    @Override
    public boolean addProduct(Product product) {
        boolean result = this.save(product);
        if (result) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                redisTemplate.opsForValue().set(PRODUCT_CACHE_KEY + product.getId(), mapper.writeValueAsString(product));
            } catch (Exception e) {}
        }
        return result;
    }

    @Override
    @CachePut(value = "hotProduct", key = "#product.id")
    public boolean updateProduct(Product product) {
        boolean result = this.updateById(product);
        if (result) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                redisTemplate.opsForValue().set(PRODUCT_CACHE_KEY + product.getId(), mapper.writeValueAsString(product));
            } catch (Exception e) {}
        }
        return result;
    }

    @Override
    @CacheEvict(value = "hotProduct", key = "#id")
    public boolean deleteProduct(Long id) {
        boolean result = this.removeById(id);
        if (result) {
            redisTemplate.delete(PRODUCT_CACHE_KEY + id);
        }
        return result;
    }

    @Override
    public boolean batchImport(List<Product> productList) {
        boolean result = this.saveBatch(productList);
        if (result) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                for (Product product : productList) {
                    redisTemplate.opsForValue().set(PRODUCT_CACHE_KEY + product.getId(), mapper.writeValueAsString(product));
                }
            } catch (Exception e) {}
        }
        return result;
    }

    @Override
    public IPage<Product> pageQuery(int pageNum, int pageSize, String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Product::getName, name);
        }
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }
} 
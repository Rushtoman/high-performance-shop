package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.entity.Product;
import java.util.List;

public interface ProductService extends IService<Product> {
    Product getProductById(Long id);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(Long id);
    boolean batchImport(List<Product> productList);
} 
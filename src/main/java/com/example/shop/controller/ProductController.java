package com.example.shop.controller;

import com.example.shop.entity.Product;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.baomidou.mybatisplus.core.metadata.IPage;

@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "获取商品")
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "新增商品")
    @PostMapping("/add")
    public boolean addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @Operation(summary = "修改商品")
    @PutMapping("/update")
    public boolean updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/delete/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @Operation(summary = "批量导入商品")
    @PostMapping("/batchImport")
    public boolean batchImport(@RequestBody List<Product> productList) {
        return productService.batchImport(productList);
    }

    @Operation(summary = "分页条件查询商品")
    @GetMapping("/page")
    public IPage<Product> pageQuery(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String name) {
        return productService.pageQuery(pageNum, pageSize, name);
    }
} 
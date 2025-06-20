package com.example.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.shop.entity.User;
import com.example.shop.service.UserService;
import com.example.shop.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录，成功返回JWT令牌
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User dbUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            return JwtUtil.generateToken(user.getUsername());
        }
        throw new RuntimeException("用户名或密码错误");
    }
} 
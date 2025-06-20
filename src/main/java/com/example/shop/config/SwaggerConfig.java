package com.example.shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI文档，添加JWT认证信息到Swagger UI中。
     * 这样在Swagger页面可以直接通过“Authorize”按钮输入JWT Token进行接口调试。
     * 
     * @return OpenAPI 配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // 定义安全方案名称
        return new OpenAPI()
                // 设置API文档的基本信息
                .info(new Info().title("Shop API").version("1.0"))
                // 添加全局安全项，要求所有接口默认都需要JWT认证
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 配置JWT认证的安全方案
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName) // 安全方案名称
                                                .type(SecurityScheme.Type.HTTP) // 类型为HTTP
                                                .scheme("bearer") // 认证方式为bearer
                                                .bearerFormat("JWT") // 令牌格式为JWT
                                )
                );
    }
} 
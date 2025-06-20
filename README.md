# Shop 高性能电商平台后端

## 项目简介
本项目为高性能电商平台核心后端服务，涵盖商品、订单、用户、缓存、接口安全等模块，支持高并发场景下的库存扣减、订单自动取消、接口限流等功能。适合作为面试项目展示，涵盖分布式、事务、缓存、锁、API设计等高频考点。

---

## 技术栈
- Spring Boot 3.x
- MyBatis-Plus
- MySQL
- Redis（含ZSet延迟队列）
- Caffeine（本地缓存）
- JWT（无状态认证）
- Swagger3（OpenAPI）
- Redisson（可选，预留分布式锁扩展点）

---

## 功能亮点
- **商品服务**：多级缓存（Redis+Caffeine）、分页、CRUD、缓存主动更新
- **订单服务**：乐观锁防超卖、延迟队列自动取消、库存恢复、订单查询
- **用户服务**：注册、登录、JWT无状态认证
- **接口安全**：JWT拦截器、接口限流防刷（基于Redis）、Swagger支持Authorize
- **高可用设计**：统一异常处理、分层清晰、易于扩展

---

## 架构设计
```
[前端/第三方] <-> [RESTful API] <-> [Spring Boot服务] <-> [MySQL/Redis/Caffeine]
```
- 商品详情页：本地Caffeine缓存 -> Redis缓存 -> MySQL
- 订单下单：Spring事务 + 乐观锁（version）
- 订单自动取消：Redis ZSet延迟队列
- 接口安全：JWT认证 + 限流拦截器

---

## 核心考点与面试提问点

### 1. 事务与并发控制
- Spring事务传播机制、隔离级别、回滚场景
- 乐观锁与悲观锁的区别与适用场景
- 分布式锁（Redisson）如何扩展？

### 2. 缓存
- 多级缓存设计（本地+分布式）
- 缓存一致性如何保证？（主动更新、失效策略）
- 缓存击穿/雪崩/穿透的防护措施

### 3. 锁
- 乐观锁实现原理（version字段）
- 分布式锁的实现方式与Redisson优势
- 锁粒度设计与死锁预防

### 4. API设计
- RESTful接口设计规范
- JWT无状态认证原理
- 接口限流防刷的实现与优化

### 5. 订单自动取消
- Redis ZSet延迟队列原理
- 与定时任务全表扫描的对比

### 6. 其他
- Swagger自动文档的集成与使用
- 统一异常处理与友好提示
- 如何扩展为分布式高可用架构

---

## 快速启动
1. 安装并启动 MySQL、Redis
2. 创建数据库并导入表结构（见`/doc/sql/`）
3. 配置`application.properties`中的数据库和Redis连接
4. 启动Spring Boot主程序
5. 访问 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) 查看接口文档

---

## 主要接口文档（部分）
- `POST /api/user/login` 用户登录，返回JWT
- `POST /api/user/register` 用户注册
- `GET /api/product/page` 商品分页查询
- `POST /api/product/add` 新增商品
- `PUT /api/product/update` 修改商品
- `DELETE /api/product/delete/{id}` 删除商品
- `POST /api/order/create` 创建订单（扣库存）
- `GET /api/order/user/{userId}` 查询用户订单
- `POST /api/order/cancel/{orderId}` 取消订单

> 更多接口详见Swagger文档

---

## 预留扩展点
- Redisson分布式锁（已预留代码注入点，便于后续集成）
- 消息队列异步处理（如RabbitMQ/Kafka）
- Spring Boot Actuator健康监控
- 统一异常处理与日志

---

## 联系方式
如需技术交流或面试经验分享，欢迎联系作者。 
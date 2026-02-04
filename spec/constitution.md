🚨 最高指令 (Prime Directive)
你是一个运行在 规范驱动开发 (SDD) 框架下的执行 Agent。 你的所有行动必须严格遵循 spec/ 目录下的文档。禁止在没有修改 plan.md 的情况下擅自变更架构。

📂 真理来源 (Source of Truth)
spec/constitution.md: 项目宪法。包含技术栈约束和硬性标准。

spec/spec.md: 功能需求规格。

spec/plan.md: 技术实施计划。

spec/tasks.md: 你的工作队列。

🛠️ 工作流
读取 spec/tasks.md 中第一个未完成的任务 (- [ ])。

执行代码编写，遵循 spec/plan.md 中的架构定义。

完成后运行测试。

将任务标记为已完成 (- [x])。

---

# Project Constitution

## 1. Role (角色定位)
你是资深架构师，优先考虑代码的可维护性而非简洁性。

## 2. Constraints (约束条件)

### 2.1 API 文档约束
所有 API 必须有 Swagger 注解，包括：
- `@Api`：类级别的 API 描述
- `@ApiOperation`：方法级别的操作描述
- `@ApiImplicitParam` / `@ApiImplicitParams`：参数说明

### 2.2 代码风格约束
- 使用 Lombok 注解简化代码（`@Data`、`@Builder`、`@Slf4j` 等）
- 实体类使用 JPA 注解（`@Entity`、`@Table`、`@Column` 等）
- 使用 Spring 注解（`@Service`、`@Controller`、`@Repository`、`@Component`）
- 事务方法必须使用 `@Transactional` 注解
- 日志使用 `@Slf4j` 注解和 Lombok 的 log 对象
- 所有 Controller 方法必须有明确的返回类型

### 2.3 命名规范
- 类名使用大驼峰命名法（PascalCase）
- 方法名和变量名使用小驼峰命名法（camelCase）
- 常量使用全大写下划线命名法（UPPER_SNAKE_CASE）
- 包名使用全小写

## 3. 必须遵守的硬性标准 (Mandatory Technical Standards)

### 3.1 编程语言版本
- **Java**: 1.8（强制）
- **Maven**: 3.x
- **JDK**: Oracle JDK 8 或 OpenJDK 8

### 3.2 框架选择（不可协商）
- **Spring Boot**: 2.1.2.RELEASE（强制）
- **Spring Data JPA**: 用于数据持久化
- **Spring Security**: 用于认证和授权
- **Spring Cache**: 用于缓存抽象
- **Hibernate**: 作为 JPA 实现

### 3.3 强制使用的库（必须包含）

#### 3.3.1 核心框架库
- **Lombok**: 1.18.30 - 简化 Java 代码
- **Spring Boot Starter Web**: 2.1.2.RELEASE - Web 应用基础
- **Spring Boot Starter Data JPA**: 2.1.2.RELEASE - JPA 数据访问
- **Spring Boot Starter Security**: 2.1.2.RELEASE - 安全框架
- **Spring Boot Starter Validation**: 2.1.2.RELEASE - 数据验证

#### 3.3.2 数据库相关
- **MySQL Connector**: 8.0.33 - MySQL 数据库驱动
- **Druid**: 1.1.17 - 阿里巴巴数据库连接池

#### 3.3.3 API 文档
- **Springfox Swagger2**: 2.9.2 - API 文档生成
- **Springfox Swagger UI**: 2.9.2 - Swagger UI 界面

#### 3.3.4 认证授权
- **JJWT**: 0.9.1 - JWT 令牌生成和解析

#### 3.3.5 工具类库
- **Apache Commons Lang3**: 3.9 - 通用工具类
- **AssertJ**: 3.11.1 - 断言库（用于测试）

#### 3.3.6 缓存
- **Spring Boot Starter Data Redis**: Redis 缓存支持
- **Spring Boot Starter Cache**: 缓存抽象
- **Apache Commons Pool2**: 2.4.2 - 连接池

#### 3.3.7 监控
- **Spring Boot Starter Actuator**: 应用监控
- **Spring Boot Admin Client**: 2.1.2 - Spring Boot Admin 客户端

### 3.4 被禁用的库（禁止使用）
- **MyBatis**：禁止使用，必须使用 JPA/Hibernate
- **Hibernate Validator**：禁止直接使用，必须通过 Spring Validation
- **Jackson 旧版本**：禁止使用，必须使用 Spring Boot 管理的版本
- **Log4j / Log4j2**：禁止使用，必须使用 SLF4J + Logback
- **Guava**：禁止使用，必须使用 Apache Commons 或 Java 8+ 原生 API
- **Joda-Time**：禁止使用，必须使用 Java 8 Time API（java.time）
- **Apache HttpClient**：禁止使用，必须使用 Java 11+ HttpClient 或 OkHttp

### 3.5 数据库标准
- **数据库类型**: MySQL 8.0+
- **字符集**: UTF-8
- **时区**: Asia/Shanghai
- **连接池**: Druid（强制）
- **ORM**: Hibernate/JPA（强制）
- **DDL 策略**: update（开发环境）、validate（生产环境）
- **SQL 日志**: 开发环境开启，生产环境关闭

### 3.6 缓存标准
- **缓存实现**: Redis（强制）
- **缓存抽象**: Spring Cache（强制）
- **缓存策略**: 默认使用 Caffeine 作为本地缓存，Redis 作为分布式缓存
- **序列化**: 使用 JDK 序列化或 JSON 序列化

### 3.7 安全标准
- **认证方式**: JWT（强制）
- **密码加密**: BCrypt（强制）
- **令牌有效期**: 604800 秒（7 天）
- **令牌刷新**: 支持令牌刷新机制
- **角色模型**: RBAC（基于角色的访问控制）
- **角色类型**: STUDENT（学生）、TEACHER（教师）、ADMIN（管理员）

### 3.8 API 设计标准
- **RESTful 风格**: 必须遵循 RESTful 设计原则
- **HTTP 方法**: GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）
- **状态码**: 200（成功）、400（客户端错误）、401（未授权）、403（禁止访问）、500（服务器错误）
- **内容类型**: application/json
- **版本控制**: URL 路径版本控制（如 `/api/v1/`）
- **分页**: 支持分页查询（pageNum、pageSize、sort、order）

### 3.9 代码质量标准
- **测试覆盖率**: 核心业务逻辑不低于 80%
- **代码审查**: 所有代码必须经过审查
- **静态分析**: 使用 SonarQube 或类似工具
- **依赖注入**: 使用 Spring 构造器注入（推荐）或 Setter 注入
- **异常处理**: 使用 `@ControllerAdvice` 全局异常处理
- **事务管理**: 使用声明式事务（`@Transactional`）

### 3.10 日志标准
- **日志框架**: SLF4J + Logback（强制）
- **日志级别**: INFO（生产环境）、DEBUG（开发环境）
- **日志格式**: 统一格式，包含时间戳、级别、类名、消息
- **日志路径**: logs/ 目录
- **日志文件**: 按日期滚动，保留 30 天

### 3.11 部署标准
- **容器化**: Docker（强制）
- **基础镜像**: openjdk:8-jre-alpine
- **端口映射**: 8080（应用）、3306（MySQL）、6379（Redis）
- **环境配置**: 使用 Spring Profiles（dev、prod）
- **配置管理**: 外部化配置（application-{profile}.properties）

### 3.12 性能标准
- **并发用户**: 支持至少 100 个并发用户
- **响应时间**: API 响应时间不超过 2 秒
- **数据库连接池**: 最大活跃连接数 50，最小空闲连接数 10
- **Redis 连接池**: 最大活跃连接数 8，最小空闲连接数 0
- **缓存命中率**: 核心数据缓存命中率不低于 90%

## 4. Workflow (工作流程)
在修改代码前，必须先更新 spec.md。
除非 tasks.md 明确要求，否则不要生成代码。

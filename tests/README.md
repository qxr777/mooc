# MOOC API 智能化测试框架

基于Postman Collection的自动化测试框架，支持代码重构后自动测试验证。

## 📋 功能特性

- ✅ 自动创建测试用户（学生/教师）
- ✅ 自动登录并获取JWT令牌
- ✅ 执行Postman Collection中的所有API测试
- ✅ 生成详细的测试报告（JSON + HTML）
- ✅ 支持代码重构后回归测试
- ✅ 实时测试结果展示
- ✅ 无需外部依赖，纯Node.js实现

## 🚀 快速开始

### 1. 启动应用

确保Spring Boot应用在 `http://localhost:8080` 运行：

```bash
cd mooc-class
mvn spring-boot:run
```

### 2. 运行基础测试

```bash
cd tests
node basic-test-runner.js
```

### 3. 运行完整测试

```bash
cd tests
node simple-test-runner.js
```

## 📁 项目结构

```
tests/
├── basic-test-runner.js         # 基础测试框架（推荐使用）
├── simple-test-runner.js        # 完整测试框架
├── smart-test-runner.js         # 智能测试框架（需要newman依赖）
├── postman-test-runner.js       # Postman Collection运行器
├── package.json                # NPM配置文件
├── run-tests.sh               # 快速启动脚本
├── environments/
│   └── test.env.json          # 测试环境配置
├── reports/
│   ├── test-report.json        # JSON格式测试报告
│   └── test-report.html        # HTML格式测试报告
└── README.md                  # 本文档
```

## 🔧 配置说明

### 环境变量配置 (test.env.json)

```json
{
  "host": "localhost",
  "port": "8080",
  "token": "",
  "userId": "2",
  "teacherId": "1",
  "moocClassId": "1",
  "courseId": "1",
  "exerciseId": "1",
  "lessonId": "1",
  "examinationId": "1",
  "subjectId": "1"
}
```

## 📊 测试报告

测试完成后会生成两种格式的报告：

### 1. JSON报告 (test-report.json)

```json
{
  "timestamp": "2024-01-30T12:00:00.000Z",
  "summary": {
    "total": 50,
    "passed": 48,
    "failed": 2,
    "passRate": "96.00%"
  },
  "errors": [...]
}
```

### 2. HTML报告 (test-report.html)

可视化测试报告，包含：
- 测试概览统计
- 成功/失败测试列表
- 错误详情
- 响应数据展示

## 🎯 测试框架对比

| 框架 | 依赖 | 功能 | 推荐度 |
|-------|------|------|--------|
| basic-test-runner.js | 无 | 基础API测试 | ⭐⭐⭐⭐⭐ |
| simple-test-runner.js | 无 | 完整API测试 | ⭐⭐⭐⭐ |
| smart-test-runner.js | newman | Postman Collection | ⭐⭐⭐ |

## 🔄 代码重构后测试

当代码重构完成后，运行以下命令进行回归测试：

```bash
cd tests
node basic-test-runner.js
```

测试框架会：
- 自动创建新的测试用户
- 执行所有API测试
- 验证重构后的功能是否正常
- 生成测试报告

## 📝 测试用例

### 基础测试用例（basic-test-runner.js）

1. **健康检查** - 验证应用是否正常运行
2. **Swagger文档访问** - 验证API文档可访问
3. **学生注册测试** - 验证学生账号注册功能
4. **教师注册测试** - 验证教师账号注册功能
5. **学生登录测试** - 验证学生账号登录功能

6. **教师登录测试** - 验证教师账号登录功能

### 完整测试用例（simple-test-runner.js）

包含所有基础测试用例，外加：
- 获取所有用户列表
- 获取用户详细信息
- 分页获取用户列表
- 获取教师的慕课堂列表
- 获取学生的慕课堂列表
- 获取慕课堂详细信息
- 获取慕课堂的学生
- 获取慕课堂的上课记录
- 获取课程的所有练习

## 🛠️ 故障排查

### 端口被占用

```bash
lsof -ti:8080 | xargs kill -9
```

### 应用未启动

```bash
cd mooc-class
mvn spring-boot:run
```

### 测试失败

1. 检查应用是否正常运行
2. 检查数据库连接是否正常
3. 查看测试报告获取详细错误信息
4. 检查环境变量配置是否正确

## 📈 持续集成

在CI/CD流程中集成测试：

```yaml
# .github/workflows/api-test.yml
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Start application
        run: |
          cd mooc-class
          mvn spring-boot:run &
          sleep 30
      - name: Run tests
        run: |
          cd tests
          node basic-test-runner.js
```

## 🎯 使用示例

### 快速启动测试

```bash
# 使用脚本启动
./tests/run-tests.sh

# 或直接运行
cd tests
node basic-test-runner.js
```

### 查看测试报告

测试完成后，打开HTML报告：

```bash
open tests/reports/test-report.html
```

### 集成到项目构建

在项目的 `pom.xml` 中添加测试执行：

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.0.0</version>
  <executions>
    <execution>
      <phase>integration-test</phase>
      <goals>
        <goal>exec</goal>
      </goals>
      <configuration>
        <executable>node</executable>
        <workingDirectory>tests</workingDirectory>
        <arguments>
          <argument>basic-test-runner.js</argument>
        </arguments>
      </configuration>
    </execution>
  </executions>
</plugin>
```

## 🤝 贡献指南

1. 添加新的测试用例到测试框架
2. 更新环境变量配置
3. 运行测试验证
4. 提交代码

## 📄 许可证

MIT License

## 📞 联系方式

如有问题，请联系开发团队或提交Issue。

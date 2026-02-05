# 课后作业：Spring Boot 项目代码重构实践

## 作业概述

**课程**：企业级应用软件设计与开发  
**主题**：代码重构与质量提升  
**难度**：⭐⭐⭐⭐  
**预计时间**：8-10 小时  
**截止日期**：2 周后

---

## 学习目标

通过本次作业，您将掌握：

1. ✅ 识别常见的代码坏味道（Code Smells）
2. ✅ 应用重构模式改进代码质量
3. ✅ 使用集成测试验证重构正确性
4. ✅ 遵循 DRY 原则和 SOLID 原则
5. ✅ 掌握 Git 分支管理和 Pull Request 流程

---

## 背景说明

您将对一个真实的慕课堂管理系统（MOOC Class）进行代码重构。该系统包含以下核心功能：

- 🔐 用户认证与权限管理
- 🏫 慕课堂创建与管理
- 🤝 课堂签到功能
- 📝 随堂测试与自动批改

当前代码存在以下典型问题：
- 硬编码字符串
- 重复的 DTO 转换逻辑
- 类型不安全的 Swagger 注解
- 注释的死代码

---

## 任务要求

### 阶段 1：环境准备（1 小时）

#### 1.1 克隆项目
```bash
git clone <项目仓库地址>
cd mooc
```

#### 1.2 创建重构分支
```bash
# 创建基线标签（安全快照）
git tag baseline-stable-v3.9

# 创建重构分支
git checkout -b refactor-<你的学号>
```

#### 1.3 启动项目
```bash
cd mooc-class
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### 1.4 运行测试基线
```bash
cd ../tests
node comprehensive-test-runner.js
```

**验收标准**：
- ✅ 项目成功启动（端口 8080）
- ✅ 测试全部通过（15/15）

---

### 阶段 2：代码审查与规划（2 小时）

#### 2.1 识别代码坏味道

分析以下 Controller 文件，识别至少 **5 类** 代码质量问题：

- `SubjectController.java`
- `ExerciseController.java`
- `UserController.java`

**提示**：关注以下方面
- 硬编码的魔法值（Magic Values）
- 重复代码（Duplicated Code）
- 长方法（Long Method）
- 类型不安全的注解

#### 2.2 制定重构计划

创建 `docs/refactoring/implementation-plan-<学号>.md`，包含：

1. **问题清单**：列出所有发现的代码坏味道
2. **优先级排序**：按照业务影响 × 严重程度排序
3. **重构策略**：说明每个问题的解决方案
4. **验证方案**：如何确保重构不破坏功能

**评分标准**：
- 问题识别准确性（30%）
- 优先级合理性（20%）
- 解决方案可行性（30%）
- 文档清晰度（20%）

---

### 阶段 3：执行重构（4-5 小时）

#### 3.1 必做任务：重构 SubjectController

**问题清单**：
1. `delete` 方法返回硬编码字符串 `"success"`
2. `getSubjectsForExamination` 方法中存在复杂的手动循环转换
3. 所有 `@ApiImplicitParam` 使用字符串类型而非 `dataTypeClass`

**重构要求**：

##### 3.1.1 消除硬编码字符串
```java
// 修改前
public String delete(@PathVariable Long id) {
    subjectService.removeSubject(id);
    return "success"; // ❌ 硬编码
}

// 修改后
public void delete(@PathVariable Long id) {
    subjectService.removeSubject(id);
    // ✅ 由全局 ControllerResponseAdvice 自动包装
}
```

##### 3.1.2 提取 DTO 转换辅助方法
```java
// 在类的末尾添加
private List<SubjectExaminationVo> toExaminationVoList(List<SubjectDto> dtos) {
    // 实现转换逻辑
}

private List<SubjectStatisticVo> toStatisticVoList(List<SubjectDto> dtos) {
    // 实现转换逻辑
}
```

##### 3.1.3 优化 Swagger 注解
```java
// 修改前
@ApiImplicitParam(name = "examinationId", value = "随堂测试ID", dataType = "Long")

// 修改后
@ApiImplicitParam(name = "examinationId", value = "随堂测试ID", dataTypeClass = Long.class)
```

**验收标准**：
- ✅ 所有硬编码字符串已消除
- ✅ 提取至少 2 个辅助方法
- ✅ 所有 Swagger 注解已优化
- ✅ 测试通过（15/15）

---

#### 3.2 选做任务：重构 ExerciseController 或 UserController

选择其中一个 Controller 进行类似的重构，要求：
- 应用与 SubjectController 相同的重构模式
- 提交前运行测试验证

**加分项**：
- 使用 Java 8 Stream API 简化循环逻辑
- 提取通用的转换工具类

---

### 阶段 4：测试验证（1 小时）

#### 4.1 运行集成测试
```bash
cd tests
node comprehensive-test-runner.js
```

**要求**：所有 15 个测试用例必须通过

#### 4.2 运行单元测试
```bash
cd mooc-class
mvn test -Dtest=SubjectServiceTest
```

#### 4.3 手动验证

启动项目后，访问 Swagger UI：
```
http://localhost:8080/swagger-ui.html
```

验证以下 API：
- `POST /subject/saveChoice` - 创建选择题
- `DELETE /subject/{id}` - 删除习题
- `GET /subject/examination` - 获取测试题目

---

### 阶段 5：文档与提交（1 小时）

#### 5.1 生成重构报告

创建 `docs/refactoring/walkthrough-<学号>.md`，包含：

1. **执行摘要**：简述完成的重构内容
2. **修改详情**：每个重构点的前后对比
3. **测试结果**：截图或文本记录
4. **改进统计**：量化代码质量提升

**参考模板**：
```markdown
# 重构完成报告

## 执行摘要
- 重构文件：SubjectController.java
- 消除硬编码：X 处
- 提取辅助方法：X 个
- 测试状态：15/15 通过

## 修改详情
### 1. 消除硬编码字符串
**修改前**：
\`\`\`java
// 粘贴原代码
\`\`\`

**修改后**：
\`\`\`java
// 粘贴新代码
\`\`\`

...
```

#### 5.2 提交代码

```bash
# 添加修改的文件
git add mooc-class/src/main/java/edu/whut/cs/jee/mooc/mclass/controller/SubjectController.java
git add docs/refactoring/

# 提交
git commit -m "refactor(controller): 优化 SubjectController 代码质量

- 消除硬编码字符串
- 提取 DTO 转换辅助方法
- 优化 Swagger 注解

测试验证：15/15 通过
学号：<你的学号>"

# 推送到远程
git push origin refactor-<你的学号>
```

#### 5.3 创建 Pull Request

1. 访问 GitHub 仓库
2. 点击 "Compare & pull request"
3. 填写 PR 信息：
   - **标题**：`[作业提交] <学号> - SubjectController 重构`
   - **描述**：粘贴重构报告的执行摘要
4. 提交 PR 等待审查

---

## 评分标准

| 项目 | 分值 | 评分细则 |
|------|------|----------|
| **环境搭建** | 10 分 | 项目成功运行，测试基线通过 |
| **代码审查** | 20 分 | 准确识别代码坏味道，重构计划合理 |
| **重构实现** | 40 分 | 代码质量提升明显，遵循最佳实践 |
| **测试验证** | 15 分 | 所有测试通过，无功能回归 |
| **文档质量** | 10 分 | 重构报告清晰完整 |
| **Git 规范** | 5 分 | Commit 信息规范，分支管理正确 |
| **选做加分** | +10 分 | 完成额外的 Controller 重构 |

**总分**：100 分（+10 分加分项）

---

## 常见问题 FAQ

### Q1: 测试失败怎么办？
**A**: 检查以下几点：
1. 是否修改了方法签名（参数或返回类型）？
2. 是否意外删除了必要的业务逻辑？
3. 使用 `git diff` 查看具体修改内容
4. 如果无法解决，回退到上一个 commit：`git reset --hard HEAD~1`

### Q2: 如何验证 Swagger 注解是否正确？
**A**: 访问 `http://localhost:8080/swagger-ui.html`，查看 API 文档中的参数类型是否正确显示。

### Q3: 可以修改 Service 层代码吗？
**A**: 本次作业仅要求重构 Controller 层。如果发现 Service 层问题，可以在重构计划中提出，但不要求实现。

### Q4: 重构后代码行数增加了，是否正常？
**A**: 正常。提取辅助方法会增加代码行数，但会提升可读性和可维护性。重点关注代码质量而非行数。

---

## 参考资源

### 推荐阅读
- 《重构：改善既有代码的设计》（Martin Fowler）
- [阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

### 工具推荐
- **IDEA 重构快捷键**：
  - `Ctrl + Alt + M` - 提取方法
  - `Ctrl + Alt + V` - 提取变量
  - `Ctrl + Alt + C` - 提取常量

### 示例代码
参考已完成的重构案例：
- `docs/refactoring/2026-02-05-moocclass-controller.md`
- `docs/refactoring/2026-02-05-p0-controllers.md`

---

## 提交清单

在提交作业前，请确认：

- [ ] 代码已推送到远程分支 `refactor-<学号>`
- [ ] 创建了 Pull Request
- [ ] 重构计划文档已完成
- [ ] 重构报告已完成
- [ ] 所有测试通过（15/15）
- [ ] Commit 信息符合规范
- [ ] 代码通过 IDEA 代码检查（无红色警告）

---

## 联系方式

**答疑时间**：每周三、五 14:00-16:00  
**答疑地点**：实验楼 A301  
**在线答疑**：课程讨论区

**祝您重构愉快！记住：优雅的代码是工程师的艺术品。** 🎨

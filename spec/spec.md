# Feature Specification: MOOC在线教学平台

**Feature Branch**: `main`  
**Created**: 2026-01-30  
**Status**: Draft  
**Input**: 基于现有MOOC教学平台代码库，整理功能需求规格

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 教师创建和管理慕课堂 (Priority: P1)

教师可以创建新的慕课堂，设置课程信息、学年学期、上课时间等基本信息，并管理课堂中的学生。教师可以开始上课、结束上课，并查看上课记录。

**Why this priority**: 这是平台的核心功能，没有慕课堂就无法进行任何教学活动，是整个系统的基础。

**Independent Test**: 教师可以独立创建慕课堂、添加课程信息、管理学生，无需其他功能模块即可验证慕课堂的完整生命周期。

**Acceptance Scenarios**:

1. **Given** 教师已登录，**When** 创建新慕课堂并填写课程信息，**Then** 系统返回慕课堂ID和6位课堂码
2. **Given** 教师已创建慕课堂，**When** 学生通过课堂码加入课堂，**Then** 学生成功加入并出现在课堂学生列表中
3. **Given** 教师已创建慕课堂，**When** 教师开始上课，**Then** 系统创建上课记录并返回上课ID
4. **Given** 上课进行中，**When** 教师结束上课，**Then** 上课记录状态更新为已结束

---

### User Story 2 - 教师管理练习库和习题 (Priority: P2)

教师可以创建练习库，向练习库中添加判断题、填空题、选择题等不同类型的习题，并设置习题的正确答案和分数。

**Why this priority**: 练习库是随堂测试的基础，教师需要先创建习题才能发布随堂测试。

**Independent Test**: 教师可以独立创建练习库、添加各种类型的习题、编辑和删除习题，验证习题管理的完整功能。

**Acceptance Scenarios**:

1. **Given** 教师已登录并创建练习库，**When** 添加判断题并设置正确答案，**Then** 习题保存成功并返回习题ID
2. **Given** 练习库已创建，**When** 添加选择题并设置多个选项和正确答案，**Then** 选择题保存成功
3. **Given** 练习库已创建，**When** 添加填空题并设置答案类型和匹配规则，**Then** 填空题保存成功
4. **Given** 练习库中有习题，**When** 教师删除习题，**Then** 习题被移除且不可再访问

---

### User Story 3 - 教师发布和管理随堂测试 (Priority: P2)

教师可以从练习库导入习题创建随堂测试，发布测试给学生，学生提交答案后教师可以查看答题统计和结果。

**Why this priority**: 随堂测试是教学互动的重要环节，能够实时了解学生对知识点的掌握情况。

**Independent Test**: 教师可以独立创建随堂测试、从练习库导入习题、发布测试、查看统计，验证随堂测试的完整流程。

**Acceptance Scenarios**:

1. **Given** 上课进行中且练习库有习题，**When** 教师从练习库导入习题创建随堂测试，**Then** 随堂测试创建成功
2. **Given** 随堂测试已创建，**When** 教师发布测试，**Then** 学生可以开始答题
3. **Given** 随堂测试已发布，**When** 教师查看答题统计，**Then** 系统返回每道题的正确率、错误率等统计数据
4. **Given** 随堂测试已创建，**When** 教师删除测试，**Then** 测试被移除且学生无法再访问

---

### User Story 4 - 教师管理签到活动 (Priority: P2)

教师可以在上课过程中创建签到活动，设置签到截止时间和GPS定位要求，学生可以通过手机签到。

**Why this priority**: 签到功能是课堂管理的重要工具，能够快速统计学生出勤情况。

**Independent Test**: 教师可以独立创建签到活动、设置签到参数、关闭签到、查看签到结果，验证签到管理的完整流程。

**Acceptance Scenarios**:

1. **Given** 上课进行中，**When** 教师创建签到活动并设置截止时间，**Then** 签到活动创建成功
2. **Given** 签到活动已创建，**When** 学生在截止时间前签到，**Then** 签到记录状态为已签到
3. **Given** 签到活动已创建且启用GPS定位，**When** 学生在指定位置范围外签到，**Then** 签到失败或标记为异常
4. **Given** 签到活动进行中，**When** 教师关闭签到，**Then** 未签到学生状态更新为缺课

---

### User Story 5 - 学生参加随堂测试 (Priority: P2)

学生可以查看教师发布的随堂测试，在线答题并提交答案，系统自动判断答案正确性并计算得分。

**Why this priority**: 学生答题是随堂测试的核心交互，需要确保答题体验流畅且结果准确。

**Independent Test**: 学生可以独立查看测试、答题、提交答案、查看结果，验证学生端答题流程。

**Acceptance Scenarios**:

1. **Given** 随堂测试已发布，**When** 学生查看测试，**Then** 显示所有习题和选项
2. **Given** 学生正在答题，**When** 学生提交判断题答案，**Then** 系统判断答案正确性并更新统计
3. **Given** 学生正在答题，**When** 学生提交选择题答案，**Then** 系统判断答案正确性并更新选项统计
4. **Given** 学生已提交答案，**When** 学生查看答题结果，**Then** 显示得分和每道题的答题情况

---

### User Story 6 - 学生签到 (Priority: P2)

学生可以通过手机或电脑在上课过程中签到，系统记录签到时间和位置信息。

**Why this priority**: 签到是学生参与课堂的基本要求，需要确保签到功能稳定可靠。

**Independent Test**: 学生可以独立查看签到活动、提交签到、查看签到状态，验证学生端签到流程。

**Acceptance Scenarios**:

1. **Given** 签到活动已发布，**When** 学生查看签到信息，**Then** 显示截止时间和GPS要求
2. **Given** 签到活动进行中，**When** 学生在截止时间前签到，**Then** 签到成功并记录时间
3. **Given** 签到活动启用GPS定位，**When** 学生在指定位置范围内签到，**Then** 签到成功并记录位置
4. **Given** 学生已签到，**When** 学生再次签到，**Then** 系统提示已签到或更新签到信息

---

### User Story 7 - 用户注册和登录 (Priority: P1)

学生和教师可以注册账号，通过用户名和密码登录系统，系统返回JWT令牌用于后续API调用。

**Why this priority**: 用户认证是所有功能的前提，没有有效的用户身份无法使用任何功能。

**Independent Test**: 用户可以独立注册、登录、刷新令牌，验证认证流程的完整性。

**Acceptance Scenarios**:

1. **Given** 新用户访问系统，**When** 填写注册信息并选择角色（学生/教师），**Then** 账号创建成功并返回用户ID
2. **Given** 用户已注册，**When** 使用正确的用户名和密码登录，**Then** 系统返回JWT访问令牌
3. **Given** 用户已登录，**When** 使用错误的密码登录，**Then** 登录失败并返回错误信息
4. **Given** 令牌即将过期，**When** 用户刷新令牌，**Then** 系统返回新的有效令牌

---

### User Story 8 - 管理员管理用户 (Priority: P3)

管理员可以查看所有用户列表，创建和删除用户账号，管理用户角色和权限。

**Why this priority**: 用户管理是系统运维的重要功能，但日常教学活动中使用频率较低。

**Independent Test**: 管理员可以独立查看用户列表、创建用户、删除用户，验证用户管理功能。

**Acceptance Scenarios**:

1. **Given** 管理员已登录，**When** 查看所有用户列表，**Then** 系统返回分页的用户信息
2. **Given** 管理员已登录，**When** 创建新用户并分配角色，**Then** 用户创建成功并可以登录
3. **Given** 管理员已登录，**When** 删除用户账号，**Then** 用户被移除且无法再登录
4. **Given** 用户列表较多，**When** 管理员使用分页查询，**Then** 系统返回指定页的用户信息

---

### Edge Cases

- 当慕课堂码重复时，系统如何处理？
- 当学生尝试加入已满员的慕课堂时，如何处理？
- 当签到截止时间已过时，学生还能签到吗？
- 当随堂测试已关闭时，学生还能提交答案吗？
- 当网络中断时，已提交的签到或答题会丢失吗？
- 当教师删除练习库时，相关的随堂测试如何处理？
- 当用户密码忘记时，如何重置密码？
- 当并发用户过多时，系统性能如何保障？
- 当GPS定位失败时，签到功能是否降级处理？
- 当习题答案格式错误时，如何提示学生？

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 系统必须允许教师创建和管理慕课堂，包括课程信息、学期和上课时间
- **FR-002**: 系统必须为每个慕课堂生成唯一的6位课堂码
- **FR-003**: 学生必须能够使用课堂码加入慕课堂
- **FR-004**: 系统必须支持上课生命周期：备课、开始上课、结束上课
- **FR-005**: 系统必须允许教师创建和管理练习库
- **FR-006**: 系统必须支持多种习题类型：判断题、填空题、选择题
- **FR-007**: 系统必须允许教师从练习库导入习题创建随堂测试
- **FR-008**: 系统必须允许教师在上课期间发布随堂测试给学生
- **FR-009**: 学生必须能够向随堂测试提交答案
- **FR-010**: 系统必须自动批改答案并计算得分
- **FR-011**: 系统必须提供每道题的答题统计和正确率、错误率
- **FR-012**: 系统必须允许教师在上课期间创建签到活动
- **FR-013**: 系统必须支持基于GPS的签到和位置验证
- **FR-014**: 学生必须能够签到并记录位置信息
- **FR-015**: 系统必须跟踪签到状态：已签到、迟到、缺课
- **FR-016**: 系统必须支持用户注册并选择角色（学生/教师）
- **FR-017**: 系统必须通过用户名和密码验证用户身份
- **FR-018**: 系统必须向已验证用户签发JWT令牌
- **FR-019**: 系统必须支持令牌刷新机制
- **FR-020**: 系统必须允许管理员管理用户账号
- **FR-021**: 所有API端点必须有Swagger注解用于文档生成
- **FR-022**: 系统必须强制执行基于角色的访问控制（RBAC）
- **FR-023**: 系统必须将所有数据持久化到MySQL数据库
- **FR-024**: Redis缓存层必须缓存频繁访问的数据
- **FR-025**: 系统必须支持大数据集的分页查询

### Key Entities

- **User**: 表示系统用户，属性包括：id、username、nickname、password、email、roles。支持继承：Student、Teacher
- **Role**: 表示用户角色：STUDENT（学生）、TEACHER（教师）、ADMIN（管理员）
- **Course**: 表示课程信息：id、name、type、teacher
- **MoocClass**: 表示慕课堂：id、name、year、semester、code、course、users
- **Lesson**: 表示上课记录：id、status、serviceDate、startTime、endTime、moocClass
- **Exercise**: 表示练习库：id、courseId、name、subjects
- **Subject**: 习题的基类实体：id、content、score、rightCount、errorCount。支持继承：Choice（选择题）、Fill（填空题）、Judgment（判断题）
- **Option**: 表示选择题选项：id、name、content、count、correct
- **Examination**: 表示随堂测试：id、lesson、name、status、subjects
- **ExaminationRecord**: 表示学生随堂测试记录：id、score、correctCount、submitTime、user、answers
- **Answer**: 表示学生答案：id、examinationRecordId、userId、right、status、answer、subject
- **CheckIn**: 表示签到活动：id、lesson、deadline、gps、longitude、latitude、attendances
- **Attendance**: 表示学生签到记录：id、checkInId、status、longitude、latitude、user

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 教师能够在3分钟内创建完整的慕课堂
- **SC-002**: 学生能够在10秒内使用课堂码加入慕课堂
- **SC-003**: 系统支持至少100个并发用户而不出现性能下降
- **SC-004**: 随堂测试批改在每次提交后1秒内完成
- **SC-005**: 签到位置验证在2秒内完成
- **SC-006**: 95%的API端点拥有完整的Swagger文档
- **SC-007**: 系统在高峰使用时段保持99.9%的运行时间
- **SC-008**: 所有用户故事都可以使用有效测试数据独立测试

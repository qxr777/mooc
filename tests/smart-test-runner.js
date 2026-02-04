const newman = require('newman');
const fs = require('fs');
const path = require('path');
const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

const testUsers = {
  student: {
    name: 'student_test_' + Date.now(),
    nickname: '测试学生',
    password: '123456',
    email: 'student_test_' + Date.now() + '@test.com',
    studentNo: '2024' + Math.floor(Math.random() * 10000),
    role: 2
  },
  teacher: {
    name: 'teacher_test_' + Date.now(),
    nickname: '测试教师',
    password: '123456',
    email: 'teacher_test_' + Date.now() + '@test.com',
    salaryNo: '9' + Math.floor(Math.random() * 1000000),
    role: 1
  }
};

let tokens = {
  student: null,
  teacher: null
};

let createdUserIds = {
  student: null,
  teacher: null
};

async function registerUser(userType) {
  const user = testUsers[userType];
  console.log(`\n📝 注册${userType === 'student' ? '学生' : '教师'}账号...`);
  
  try {
    const response = await axios.post(`${BASE_URL}/auth/register`, user);
    const userId = response.data;
    createdUserIds[userType] = userId;
    console.log(`✅ 注册成功，用户ID: ${userId}`);
    return userId;
  } catch (error) {
    console.error(`❌ 注册失败:`, error.response?.data || error.message);
    throw error;
  }
}

async function login(userType) {
  const user = testUsers[userType];
  console.log(`\n🔐 登录${userType === 'student' ? '学生' : '教师'}账号...`);
  
  try {
    const response = await axios.post(`${BASE_URL}/auth`, {
      username: user.name,
      password: user.password
    });
    const token = response.data.substring(7);
    tokens[userType] = token;
    console.log(`✅ 登录成功，令牌: ${token.substring(0, 20)}...`);
    return token;
  } catch (error) {
    console.error(`❌ 登录失败:`, error.response?.data || error.message);
    throw error;
  }
}

async function refreshToken(userType) {
  const token = tokens[userType];
  console.log(`\n🔄 刷新${userType === 'student' ? '学生' : '教师'}令牌...`);
  
  try {
    const response = await axios.get(`${BASE_URL}/auth/refresh`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    const newToken = response.data.substring(7);
    tokens[userType] = newToken;
    console.log(`✅ 令牌刷新成功`);
    return newToken;
  } catch (error) {
    console.error(`❌ 令牌刷新失败:`, error.response?.data || error.message);
    throw error;
  }
}

async function runPostmanCollection() {
  const collectionPath = path.join(__dirname, '../postman/mooc.postman_collection.json');
  const environmentPath = path.join(__dirname, 'environments/test.env.json');

  const collection = newman.Collection.Collection.fromFilePath(collectionPath);
  const environment = newman.Environment.fromFilePath(environmentPath);

  console.log('\n🚀 开始执行Postman测试集合...');
  console.log(`📋 总请求数: ${collection.items.length}`);

  const results = {
    passed: 0,
    failed: 0,
    skipped: 0,
    errors: []
  };

  for (const item of collection.items) {
    const itemName = item.name;
    console.log(`\n\n▶️  执行: ${itemName}`);

    try {
      if (item.request) {
        const response = await executeRequest(item.request, environment);
        if (item.event) {
          await executeTests(item.event, response);
        }
        results.passed++;
        console.log(`✅ ${itemName} - 通过`);
      }
    } catch (error) {
      results.failed++;
      results.errors.push({
        item: itemName,
        error: error.message
      });
      console.error(`❌ ${itemName} - 失败: ${error.message}`);
    }
  }

  return results;
}

async function executeRequest(request, environment) {
  const url = request.url.raw.replace(/\{\{(\w+)\}\}/g, (match, key) => {
    const variable = environment.variables.find(v => v.key === key);
    return variable ? variable.value : match;
  });

  const headers = {};
  if (request.header) {
    request.header.forEach(header => {
      const key = header.key.replace(/\{\{(\w+)\}\}/g, (match, varKey) => {
        const variable = environment.variables.find(v => v.key === varKey);
        return variable ? variable.value : match;
      });
      const value = header.value.replace(/\{\{(\w+)\}\}/g, (match, varKey) => {
        const variable = environment.variables.find(v => v.key === varKey);
        return variable ? variable.value : match;
      });
      headers[key] = value;
    });
  }

  const config = {
    method: request.method.toLowerCase(),
    url: url,
    headers: headers,
    timeout: 10000
  };

  if (request.body && request.body.mode === 'raw') {
    config.data = JSON.parse(request.body.raw);
  }

  const response = await axios(config);
  return {
    status: response.status,
    data: response.data,
    headers: response.headers
  };
}

async function executeTests(events, response) {
  const testEvent = events.find(e => e.listen === 'test');
  if (testEvent && testEvent.script) {
    const script = testEvent.script.exec.join('\n');
    
    const responseBody = JSON.stringify(response.data);
    const pm = {
      test: (name, fn) => {
        try {
          fn();
          console.log(`   ✓ ${name}`);
        } catch (error) {
          console.error(`   ✗ ${name}: ${error.message}`);
          throw error;
        }
      },
      expect: (actual) => ({
        to: {
          include: (expected) => {
            if (!actual.includes(expected)) {
              throw new Error(`期望包含 "${expected}"`);
            }
          },
          equal: (expected) => {
            if (actual !== expected) {
              throw new Error(`期望 ${expected}，实际 ${actual}`);
            }
          }
        }
      }),
      environment: {
        set: (key, value) => {
          console.log(`   📝 设置环境变量: ${key}`);
        }
      }
    };

    eval(script);
  }
}

async function generateTestReport(results) {
  const reportPath = path.join(__dirname, 'reports/test-report.json');
  const htmlReportPath = path.join(__dirname, 'reports/test-report.html');

  const report = {
    timestamp: new Date().toISOString(),
    summary: {
      total: results.passed + results.failed,
      passed: results.passed,
      failed: results.failed,
      passRate: ((results.passed / (results.passed + results.failed)) * 100).toFixed(2) + '%'
    },
    errors: results.errors,
    users: {
      student: {
        id: createdUserIds.student,
        name: testUsers.student.name,
        token: tokens.student ? tokens.student.substring(0, 20) + '...' : null
      },
      teacher: {
        id: createdUserIds.teacher,
        name: testUsers.teacher.name,
        token: tokens.teacher ? tokens.teacher.substring(0, 20) + '...' : null
      }
    }
  };

  fs.writeFileSync(reportPath, JSON.stringify(report, null, 2));
  console.log(`\n📄 测试报告已保存: ${reportPath}`);

  const htmlReport = generateHtmlReport(report);
  fs.writeFileSync(htmlReportPath, htmlReport);
  console.log(`📄 HTML报告已保存: ${htmlReportPath}`);

  return report;
}

function generateHtmlReport(report) {
  return `
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>MOOC API 测试报告</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .header { background: #4CAF50; color: white; padding: 20px; border-radius: 5px; }
    .summary { display: flex; gap: 20px; margin: 20px 0; }
    .summary-card { flex: 1; background: #f5f5f5; padding: 15px; border-radius: 5px; }
    .summary-card h3 { margin: 0 0 10px; }
    .summary-card .value { font-size: 24px; font-weight: bold; color: #4CAF50; }
    .errors { background: #fff3cd; padding: 20px; border-radius: 5px; margin-top: 20px; }
    .error-item { background: white; padding: 10px; margin: 5px 0; border-left: 4px solid #f44336; }
    .users { background: #e3f2fd; padding: 20px; border-radius: 5px; margin-top: 20px; }
    .user-card { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; }
    .success { color: #4CAF50; }
    .failure { color: #f44336; }
  </style>
</head>
<body>
  <div class="header">
    <h1>🧪 MOOC API 自动化测试报告</h1>
    <p>测试时间: ${report.timestamp}</p>
  </div>

  <div class="summary">
    <div class="summary-card">
      <h3>总请求数</h3>
      <div class="value">${report.summary.total}</div>
    </div>
    <div class="summary-card">
      <h3>成功请求数</h3>
      <div class="value success">${report.summary.passed}</div>
    </div>
    <div class="summary-card">
      <h3>失败请求数</h3>
      <div class="value failure">${report.summary.failed}</div>
    </div>
    <div class="summary-card">
      <h3>测试通过率</h3>
      <div class="value">${report.summary.passRate}</div>
    </div>
  </div>

  ${report.errors.length > 0 ? `
  <div class="errors">
    <h2>❌ 失败的测试</h2>
    ${report.errors.map(error => `
      <div class="error-item">
        <strong>${error.item}</strong>
        <p>${error.error}</p>
      </div>
    `).join('')}
  </div>
  ` : ''}

  <div class="users">
    <h2>👤 测试用户信息</h2>
    <div class="user-card">
      <h3>学生</h3>
      <p><strong>用户名:</strong> ${report.users.student.name}</p>
      <p><strong>用户ID:</strong> ${report.users.student.id || '未创建'}</p>
      <p><strong>令牌:</strong> ${report.users.student.token || '未获取'}</p>
    </div>
    <div class="user-card">
      <h3>教师</h3>
      <p><strong>用户名:</strong> ${report.users.teacher.name}</p>
      <p><strong>用户ID:</strong> ${report.users.teacher.id || '未创建'}</p>
      <p><strong>令牌:</strong> ${report.users.teacher.token || '未获取'}</p>
    </div>
  </div>

  <script>
    setTimeout(() => {
      if (${report.summary.failed} > 0) {
        alert('存在失败的测试，请查看报告详情！');
      }
    }, 1000);
  </script>
</body>
</html>
  `;
}

async function main() {
  console.log('\n╔════════════════════════════════════════════════════════════════╗');
  console.log('║        🧪 MOOC API 智能化测试框架 v1.0                    ║');
  console.log('╚════════════════════════════════════════════════════════════════╝');

  try {
    console.log('\n📋 步骤 1: 创建测试用户');
    await registerUser('student');
    await registerUser('teacher');

    console.log('\n📋 步骤 2: 登录并获取令牌');
    await login('student');
    await login('teacher');

    console.log('\n📋 步骤 3: 执行Postman测试集合');
    const results = await runPostmanCollection();

    console.log('\n📋 步骤 4: 生成测试报告');
    const report = await generateTestReport(results);

    console.log('\n╔════════════════════════════════════════════════════════════════╗');
    console.log('║                      测试完成                               ║');
    console.log('╚════════════════════════════════════════════════════════════════╝');
    console.log(`\n✅ 测试通过率: ${report.summary.passRate}`);
    console.log(`✅ 成功请求数: ${report.summary.passed}/${report.summary.total}`);

    if (results.failed > 0) {
      console.error('\n❌ 存在失败的测试，请检查测试报告！');
      process.exit(1);
    } else {
      console.log('\n🎉 所有测试通过！');
      process.exit(0);
    }
  } catch (error) {
    console.error('\n❌ 测试执行失败:', error.message);
    console.error(error.stack);
    process.exit(1);
  }
}

main();

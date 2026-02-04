const http = require('http');
const https = require('https');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://localhost:8080';

function httpRequest(options) {
  return new Promise((resolve, reject) => {
    const protocol = options.url.startsWith('https') ? https : http;
    const url = new URL(options.url);
    
    const requestOptions = {
      hostname: url.hostname,
      port: url.port || (options.url.startsWith('https') ? 443 : 80),
      path: url.pathname + url.search,
      method: options.method || 'GET',
      headers: options.headers || {}
    };

    const req = protocol.request(requestOptions, (res) => {
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => {
        try {
          if (data.trim()) {
            const jsonData = JSON.parse(data);
            resolve({
              status: res.statusCode,
              data: jsonData,
              headers: res.headers
            });
          } else {
            resolve({
              status: res.statusCode,
              data: null,
              headers: res.headers
            });
          }
        } catch (error) {
          resolve({
            status: res.statusCode,
            data: data,
            headers: res.headers
          });
        }
      });
    });

    req.on('error', reject);

    if (options.body) {
      req.write(JSON.stringify(options.body));
    }

    req.end();
  });
}

const testUsers = {
  student: {
    name: 'st' + Math.floor(Math.random() * 10000000),
    nickname: '测试学生',
    password: '123456',
    email: 'student_test_' + Date.now() + '@test.com',
    studentNo: '2024' + Math.floor(Math.random() * 10000),
    role: 2
  },
  teacher: {
    name: 'te' + Math.floor(Math.random() * 10000000),
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
    const response = await httpRequest({
      url: `${BASE_URL}/auth/register`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: user
    });
    
    let userId;
    if (typeof response.data === 'object') {
      userId = response.data.id || response.data;
    } else {
      userId = response.data;
    }
    
    createdUserIds[userType] = userId;
    console.log(`✅ 注册成功，用户ID: ${userId}`);
    return userId;
  } catch (error) {
    console.error(`❌ 注册失败:`, error.message);
    throw error;
  }
}

async function login(userType) {
  const user = testUsers[userType];
  console.log(`\n🔐 登录${userType === 'student' ? '学生' : '教师'}账号...`);
  
  try {
    const response = await httpRequest({
      url: `${BASE_URL}/auth`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: user.name,
        password: user.password
      }
    });
    
    let tokenData;
    if (typeof response.data === 'object') {
      tokenData = response.data.data || response.data.token || response.data;
    } else {
      tokenData = response.data;
    }
    
    let token;
    if (typeof tokenData === 'string' && tokenData.startsWith('Bearer ')) {
      token = tokenData.substring(7);
    } else {
      token = tokenData;
    }
    
    tokens[userType] = token;
    console.log(`✅ 登录成功，令牌: ${token.substring(0, 20)}...`);
    return token;
  } catch (error) {
    console.error(`❌ 登录失败:`, error.message);
    throw error;
  }
}

async function refreshToken(userType) {
    const token = tokens[userType];
    console.log(`\n🔄 刷新${userType === 'student' ? '学生' : '教师'}令牌...`);
    
    try {
      const response = await httpRequest({
        url: `${BASE_URL}/auth/refresh`,
        method: 'GET',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      let tokenData;
      if (typeof response.data === 'object') {
        tokenData = response.data.data || response.data.token || response.data;
      } else {
        tokenData = response.data;
      }
      
      let newToken;
      if (typeof tokenData === 'string' && tokenData.startsWith('Bearer ')) {
        newToken = tokenData.substring(7);
      } else {
        newToken = tokenData;
      }
      
      tokens[userType] = newToken;
      console.log(`✅ 令牌刷新成功`);
      return newToken;
    } catch (error) {
      console.error(`❌ 令牌刷新失败:`, error.message);
      throw error;
    }
  }

async function runApiTests() {
  console.log('\n🚀 开始执行API测试...');

  const tests = [
    {
      name: '健康检查',
      method: 'GET',
      url: `${BASE_URL}/health`
    },
    {
      name: '获取所有用户列表',
      method: 'GET',
      url: `${BASE_URL}/user`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '获取用户详细信息',
      method: 'GET',
      url: `${BASE_URL}/user/1`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '分页获取用户列表',
      method: 'GET',
      url: `${BASE_URL}/user/page?pageNum=0&pageSize=10`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '获取教师的慕课堂列表',
      method: 'GET',
      url: `${BASE_URL}/mclass/own?teacherId=1`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '获取学生的慕课堂列表',
      method: 'GET',
      url: `${BASE_URL}/mclass/join?userId=${createdUserIds.student}`,
      headers: { 'Authorization': `Bearer ${tokens.student}` }
    },
    {
      name: '获取慕课堂详细信息',
      method: 'GET',
      url: `${BASE_URL}/mclass/1`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
    name: '获取慕课堂的学生',
      method: 'GET',
      url: `${BASE_URL}/mclass/1/users`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '获取慕课堂的上课记录',
      method: 'GET',
      url: `${BASE_URL}/mclass/1/lessons`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    },
    {
      name: '获取课程的所有练习',
      method: 'GET',
      url: `${BASE_URL}/exercise?courseId=1`,
      headers: { 'Authorization': `Bearer ${tokens.teacher}` }
    }
  ];

  const results = {
    passed: 0,
    failed: 0,
    errors: []
  };

  for (const test of tests) {
    console.log(`\n▶️  执行: ${test.name}`);
    
    try {
      const response = await httpRequest(test);
      
      if (response.status >= 200 && response.status < 300) {
        results.passed++;
        console.log(`✅ ${test.name} - 通过 (状态码: ${response.status})`);
      } else {
        results.failed++;
        results.errors.push({
          test: test.name,
          error: `状态码: ${response.status}, 响应: ${JSON.stringify(response.data)}`
        });
        console.error(`❌ ${test.name} - 失败 (状态码: ${response.status})`);
      }
    } catch (error) {
      results.failed++;
      results.errors.push({
        test: test.name,
        error: error.message
      });
      console.error(`❌ ${test.name} - 失败: ${error.message}`);
    }
  }

  return results;
}

async function generateTestReport(results) {
  const reportDir = path.join(__dirname, 'reports');
  if (!fs.existsSync(reportDir)) {
    fs.mkdirSync(reportDir, { recursive: true });
  }

  const reportPath = path.join(reportDir, 'test-report.json');
  const htmlReportPath = path.join(reportDir, 'test-report.html');

  const report = {
    timestamp: new Date().toISOString(),
    summary: {
      total: results.passed + results.failed,
      passed: results.passed,
      failed: results.failed,
      passRate: results.passed + results.failed > 0 ? ((results.passed / (results.passed + results.failed)) * 100).toFixed(2) + '%' : '0%'
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
  console.log(`\n📄 �JSON报告已保存: ${reportPath}`);

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
    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }
    .header { background: #4CAF50; color: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; }
    .summary { display: flex; gap: 20px; margin: 20px 0; }
    .summary-card { flex: 1; background: white; padding: 15px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
    .summary-card h3 { margin: 0 0 10px; color: #333; }
    .summary-card .value { font-size: 24px; font-weight: bold; color: #4CAF50; }
    .errors { background: #fff3cd; padding: 20px; border-radius: 5px; margin-top: 20px; }
    .error-item { background: white; padding: 10px; margin: 5px 0; border-left: 4px solid #f44336; border-radius: 3px; }
    .users { background: #e3f2fd; padding: 20px; border-radius: 5px; margin-top: 20px; }
    .user-card { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
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
        <strong>${error.test}</strong>
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
    console.log('\n📋 步骤 1: 创建测试用户' );
    await registerUser('student');
    await registerUser('teacher');

    console.log('\n📋 步骤 2: 登录并获取令牌');
    await login('student');
    await login('teacher');

    console.log('\n📋 步骤 3: 执行API测试');
    const results = await runApiTests();

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

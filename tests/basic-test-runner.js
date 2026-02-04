const http = require('http');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://localhost:8080';

function httpRequest(options) {
  return new Promise((resolve, reject) => {
    const url = new URL(options.url);
    
    const requestOptions = {
      hostname: url.hostname,
      port: url.port || 80,
      path: url.pathname + url.search,
      method: options.method || 'GET',
      headers: options.headers || {}
    };

    const req = http.request(requestOptions, (res) => {
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

async function runBasicTests() {
  console.log('\n╔════════════════════════════════════════════════════════════════╗');
  console.log('║        🧪 MOOC API 基础测试框架 v1.0                    ║');
  console.log('╚════════════════════════════════════════════════════════════════╝');

  console.log('\n🚀 开始执行基础API测试...');

  const tests = [
    {
      name: '健康检查',
      method: 'GET',
      url: `${BASE_URL}/health`,
      expectedStatus: 200
    },
    {
      name: 'Swagger文档访问',
      method: 'GET',
      url: `${BASE_URL}/swagger-ui.html`,
      expectedStatus: 200
    },
    {
      name: '学生注册测试',
      method: 'POST',
      url: `${BASE_URL}/auth/register`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        role: 2,
        email: 'test_student@test.com',
        name: 'testst001',
        nickname: '测试学生',
        password: '123456',
        studentNo: '20240001'
      },
      expectedStatus: 200
    },
    {
      name: '教师注册测试',
      method: 'POST',
      url: `${BASE_URL}/auth/register`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        role: 1,
        email: 'test_teacher@test.com',
        name: 'testte001',
        nickname: '测试教师',
        password: '123456',
        salaryNo: '9123456'
      },
      expectedStatus: 200
    },
    {
      name: '学生登录测试',
      method: 'POST',
      url: `${BASE_URL}/auth`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: 'testst001',
        password: '123456'
      },
      expectedStatus: 200
    },
    {
      name: '教师登录测试',
      method: 'POST',
      url: `${BASE_URL}/auth`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: 'testte001',
        password: '123456'
      },
      expectedStatus: 200
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
      
      const isPass = response.status === test.expectedStatus;
      
      if (isPass) {
        results.passed++;
        console.log(`✅ ${test.name} - 通过 (状态码: ${response.status})`);
        if (response.data) {
          console.log(`   响应: ${JSON.stringify(response.data).substring(0, 100)}...`);
        }
      } else {
        results.failed++;
        const errorDetails = `期望状态码: ${test.expectedStatus}, 实际状态码: ${response.status}`;
        results.errors.push({
          test: test.name,
          error: errorDetails,
          response: response.data
        });
        console.error(`❌ ${test.name} - 失败 (${errorDetails})`);
        if (response.data) {
          console.error(`   响应: ${JSON.stringify(response.data)}`);
        }
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

  await generateTestReport(results);

  console.log('\n╔════════════════════════════════════════════════════════════════╗');
  console.log('║                      测试完成                               ║');
  console.log('╚════════════════════════════════════════════════════════════════╝');
  console.log(`\n✅ 测试通过率: ${((results.passed / (results.passed + results.failed)) * 100).toFixed(2)}%`);
  console.log(`✅ 成功请求数: ${results.passed}/${results.passed + results.failed}`);

  if (results.failed > 0) {
    console.error('\n❌ 存在失败的测试，请检查测试报告！');
    process.exit(1);
  } else {
    console.log('\n🎉 所有测试通过！');
    process.exit(0);
  }
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
    errors: results.errors
  };

  fs.writeFileSync(reportPath, JSON.stringify(report, null, 2));
  console.log(`📄 JSON报告已保存: ${reportPath}`);

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
        ${error.response ? `<p><strong>响应:</strong> <pre>${JSON.stringify(error.response, null, 2)}</pre></p>` : ''}
      </div>
    `).join('')}
  </div>
  ` : '<div style="background: #d4edda; padding: 20px; border-radius: 5px; margin-top: 20px;"><h2>✅ 所有测试通过！</h2></div>'}

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

runBasicTests();

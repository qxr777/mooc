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
              headers: res.headers,
              rawData: data
            });
          } else {
            resolve({
              status: res.statusCode,
              data: null,
              headers: res.headers,
              rawData: data
            });
          }
        } catch (error) {
          resolve({
            status: res.statusCode,
            data: data,
            headers: res.headers,
            rawData: data
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

let studentToken = null;
let teacherToken = null;

async function runComprehensiveTests() {
  console.log('\n╔════════════════════════════════════════════════════════════════╗');
  console.log('║        🧪 MOOC API 综合测试框架 v2.0                    ║');
  console.log('╚════════════════════════════════════════════════════════════════╝');

  console.log('\n🚀 开始执行综合API测试...');

  const results = {
    passed: 0,
    failed: 0,
    errors: [],
    suites: []
  };

  console.log('\n📋 基础功能测试');
  console.log('─'.repeat(60));
  
  const basicSuiteResult = {
    name: '📋 基础功能测试',
    passed: 0,
    failed: 0,
    errors: []
  };

  for (const test of getBasicTests()) {
    console.log(`\n▶️  执行: ${test.name}`);
    
    try {
      const response = await httpRequest(test);
      
      const isPass = response.status === test.expectedStatus;
      
      if (isPass) {
        results.passed++;
        basicSuiteResult.passed++;
        console.log(`✅ ${test.name} - 通过 (状态码: ${response.status})`);
        
        if (test.onSuccess) {
          test.onSuccess(response);
        }
        
        if (response.data && response.status === 200) {
          const dataStr = JSON.stringify(response.data);
          if (dataStr.length < 200) {
            console.log(`   响应: ${dataStr}`);
          } else {
            console.log(`   响应: ${dataStr.substring(0, 200)}...`);
          }
        }
      } else {
        results.failed++;
        basicSuiteResult.failed++;
        const errorDetails = `期望状态码: ${test.expectedStatus}, 实际状态码: ${response.status}`;
        const error = {
          test: test.name,
          error: errorDetails,
          response: response.data
        };
        results.errors.push(error);
        basicSuiteResult.errors.push(error);
        console.error(`❌ ${test.name} - 失败 (${errorDetails})`);
        if (response.data) {
          console.error(`   响应: ${JSON.stringify(response.data).substring(0, 200)}...`);
        }
      }
    } catch (error) {
      results.failed++;
      basicSuiteResult.failed++;
      const errorObj = {
        test: test.name,
        error: error.message
      };
      results.errors.push(errorObj);
      basicSuiteResult.errors.push(errorObj);
      console.error(`❌ ${test.name} - 失败: ${error.message}`);
    }
  }
  
  results.suites.push(basicSuiteResult);

  const testSuites = [
    {
      name: '🏫 慕课堂管理测试',
      tests: getMoocClassTests(teacherToken)
    },
    {
      name: '📚 习题管理测试',
      tests: getSubjectTests(teacherToken, studentToken)
    },
    {
      name: '📝 考试管理测试',
      tests: getExaminationTests(teacherToken)
    },
    {
      name: '✅ 签到管理测试',
      tests: getCheckinTests(teacherToken)
    },
    {
      name: '📖 练习库管理测试',
      tests: getExerciseTests(teacherToken)
    },
    {
      name: '🔐 认证管理测试',
      tests: getAuthTests(teacherToken)
    },
    {
      name: '👥 用户管理测试（需要ADMIN权限）',
      tests: getUserTests(teacherToken)
    }
  ];

  for (const suite of testSuites) {
    console.log(`\n${suite.name}`);
    console.log('─'.repeat(60));
    
    const suiteResult = {
      name: suite.name,
      passed: 0,
      failed: 0,
      errors: []
    };

    for (const test of suite.tests) {
      console.log(`\n▶️  执行: ${test.name}`);
      
      try {
        const response = await httpRequest(test);
        
        const isPass = response.status === test.expectedStatus;
        
        if (isPass) {
          results.passed++;
          suiteResult.passed++;
          console.log(`✅ ${test.name} - 通过 (状态码: ${response.status})`);
          
          if (test.onSuccess) {
            test.onSuccess(response);
          }
          
          if (response.data && response.status === 200) {
            const dataStr = JSON.stringify(response.data);
            if (dataStr.length < 200) {
              console.log(`   响应: ${dataStr}`);
            } else {
              console.log(`   响应: ${dataStr.substring(0, 200)}...`);
            }
          }
        } else {
          results.failed++;
          suiteResult.failed++;
          const errorDetails = `期望状态码: ${test.expectedStatus}, 实际状态码: ${response.status}`;
          const error = {
            test: test.name,
            error: errorDetails,
            response: response.data
          };
          results.errors.push(error);
          suiteResult.errors.push(error);
          console.error(`❌ ${test.name} - 失败 (${errorDetails})`);
          if (response.data) {
            console.error(`   响应: ${JSON.stringify(response.data).substring(0, 200)}...`);
          }
        }
      } catch (error) {
        results.failed++;
        suiteResult.failed++;
        const errorObj = {
          test: test.name,
          error: error.message
        };
        results.errors.push(errorObj);
        suiteResult.errors.push(errorObj);
        console.error(`❌ ${test.name} - 失败: ${error.message}`);
      }
    }
    
    results.suites.push(suiteResult);
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

function getBasicTests() {
  return [
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
      name: '教师登录',
      method: 'POST',
      url: `${BASE_URL}/auth`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: 'whut',
        password: '123456'
      },
      expectedStatus: 200,
      onSuccess: (response) => {
        if (response.data && response.data.data) {
          teacherToken = response.data.data;
          console.log(`   教师Token: ${teacherToken.substring(0, 30)}...`);
        }
      }
    },
    {
      name: '学生登录',
      method: 'POST',
      url: `${BASE_URL}/auth`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: 'xes1',
        password: '123456'
      },
      expectedStatus: 200,
      onSuccess: (response) => {
        if (response.data && response.data.data) {
          studentToken = response.data.data;
          console.log(`   学生Token: ${studentToken.substring(0, 30)}...`);
        }
      }
    }
  ];
}

function getMoocClassTests(token) {
  return [
    {
      name: '获取慕课堂详细信息',
      method: 'GET',
      url: `${BASE_URL}/mclass/1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '获取慕课堂的上课记录',
      method: 'GET',
      url: `${BASE_URL}/mclass/1/lessons`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '获取教师的慕课堂列表',
      method: 'GET',
      url: `${BASE_URL}/mclass/own?teacherId=1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '获取慕课堂的学生',
      method: 'GET',
      url: `${BASE_URL}/mclass/1/users`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '新增慕课堂',
      method: 'POST',
      url: `${BASE_URL}/mclass`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      body: {
        name: '测试慕课堂课堂' + Date.now(),
        year: 2026,
        semester: '春季',
        courseId: 1
      },
      expectedStatus: 200
    },
    {
      name: '更新慕课堂',
      method: 'PUT',
      url: `${BASE_URL}/mclass/1`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      body: {
        id: 1,
        name: '更新后的慕课堂',
        year: 2026,
        semester: '春季',
        courseId: 1
      },
      expectedStatus: 200
    },
    {
      name: '开始上课',
      method: 'POST',
      url: `${BASE_URL}/mclass/1/start`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '结束上课',
      method: 'PUT',
      url: `${BASE_URL}/mclass/lesson/1/end`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    }
  ];
}

function getSubjectTests(teacherToken, studentToken) {
  return [
    {
      name: '获取指定练习库的所有习题',
      method: 'GET',
      url: `${BASE_URL}/subject/exercise?exerciseId=1`,
      headers: { 'Authorization': teacherToken },
      expectedStatus: 200
    },
    {
      name: '获取指定随堂测试的所有习题',
      method: 'GET',
      url: `${BASE_URL}/subject/examination?examinationId=1`,
      headers: { 'Authorization': teacherToken },
      expectedStatus: 200
    },
    {
      name: '获取考试题目',
      method: 'GET',
      url: `${BASE_URL}/subject/exam?examinationId=1`,
      headers: { 'Authorization': studentToken },
      expectedStatus: 200
    },
    {
      name: '获取考试统计信息',
      method: 'GET',
      url: `${BASE_URL}/subject/statistic?examinationId=1`,
      headers: { 'Authorization': teacherToken },
      expectedStatus: 200
    },
    {
      name: '新增判断题',
      method: 'POST',
      url: `${BASE_URL}/subject/saveJudgment`,
      headers: { 
        'Authorization': teacherToken,
        'Content-Type': 'application/json'
      },
      body: {
        content: '测试判断题' + Date.now(),
        score: 10,
        correct: true,
        exerciseId: 1
      },
      expectedStatus: 200
    },
    {
      name: '新增选择题',
      method: 'POST',
      url: `${BASE_URL}/subject/saveChoice`,
      headers: { 
        'Authorization': teacherToken,
        'Content-Type': 'application/json'
      },
      body: {
        content: '测试选择题' + Date.now(),
        score: 10,
        exerciseId: 1,
        options: [
          { name: 'A', content: '选项A', correct: true },
          { name: 'B', content: '选项B', correct: false },
          { name: 'C', content: '选项C', correct: false },
          { name: 'D', content: '选项D', correct: false }
        ]
      },
      expectedStatus: 200
    },
    {
      name: '新增填空题',
      method: 'POST',
      url: `${BASE_URL}/subject/saveFill`,
      headers: { 
        'Authorization': teacherToken,
        'Content-Type': 'application/json'
      },
      body: {
        content: '测试填空题' + Date.now(),
        score: 10,
        exerciseId: 1,
        keyType: 2,
        textKey: '测试答案'
      },
      expectedStatus: 200
    }
  ];
}

function getExaminationTests(token) {
  return [
    {
      name: '获取考试记录',
      method: 'GET',
      url: `${BASE_URL}/examination/1/record`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '获取私有考试列表',
      method: 'GET',
      url: `${BASE_URL}/examination/privates?moocClassId=1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '从练习库导入随堂测试',
      method: 'POST',
      url: `${BASE_URL}/examination/importFromExercise?lessonId=1&exerciseId=1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    }
  ];
}

function getCheckinTests(token) {
  return [
    {
      name: '获取签到信息',
      method: 'GET',
      url: `${BASE_URL}/checkin/1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '关闭签到活动',
      method: 'POST',
      url: `${BASE_URL}/checkin/1/close`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    }
  ];
}

function getExerciseTests(token) {
  return [
    {
      name: '获取课程的所有练习练习',
      method: 'GET',
      url: `${BASE_URL}/exercise?courseId=1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '新增练习',
      method: 'POST',
      url: `${BASE_URL}/exercise`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      body: {
        courseId: 1,
        name: '测试练习' + Date.now()
      },
      expectedStatus: 200
    }
  ];
}

function getAuthTests(token) {
  return [
    {
      name: '刷新令牌',
      method: 'POST',
      url: `${BASE_URL}/auth/refresh`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      expectedStatus: 200
    },
    {
      name: '注册新用户',
      method: 'POST',
      url: `${BASE_URL}/auth/register`,
      headers: { 'Content-Type': 'application/json' },
      body: {
        username: 'testuser' + Date.now(),
        password: '123456',
        nickname: '测试用户',
        email: 'test@test.com',
        role: 'STUDENT'
      },
      expectedStatus: 200
    }
  ];
}

function getUserTests(token) {
  return [
    {
      name: '获取所有用户列表',
      method: 'GET',
      url: `${BASE_URL}/user`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '获取用户详细信息',
      method: 'GET',
      url: `${BASE_URL}/user/1`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '分页获取用户列表',
      method: 'GET',
      url: `${BASE_URL}/user/page`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    },
    {
      name: '新增学生用户',
      method: 'POST',
      url: `${BASE_URL}/user/saveStudent`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      body: {
        name: 'teststudent' + Date.now(),
        password: '123456',
        nickname: '测试学生',
        email: 'student@test.com'
      },
      expectedStatus: 200
    },
    {
      name: '新增教师用户',
      method: 'POST',
      url: `${BASE_URL}/user/saveTeacher`,
      headers: { 
        'Authorization': token,
        'Content-Type': 'application/json'
      },
      body: {
        name: 'testteacher' + Date.now(),
        password: '123456',
        nickname: '测试教师',
        email: 'teacher@test.com',
        salaryNo: 'T123456'
      },
      expectedStatus: 200
    },
    {
      name: '删除用户',
      method: 'DELETE',
      url: `${BASE_URL}/user/999`,
      headers: { 'Authorization': token },
      expectedStatus: 200
    }
  ];
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
    suites: results.suites,
    errors: results.errors
  };

  fs.writeFileSync(reportPath, JSON.stringify(report, null, 2));
  console.log(`\n📄 JSON报告已保存: ${reportPath}`);

  const htmlReport = generateHtmlReport(report);
  fs.writeFileSync(htmlReportPath, htmlReport);
  console.log(`📄 HTML报告已保存: ${htmlReportPath}`);

  return report;
}

function generateHtmlReport(report) {
  const suitesHtml = report.suites.map(suite => `
    <div class="suite">
      <h3>${suite.name}</h3>
      <div class="suite-summary">
        <span class="success">✅ 通过: ${suite.passed}</span>
        <span class="failure">❌ 失败: ${suite.failed}</span>
      </div>
      
      ${suite.errors.length > 0 ? `
        <div class="suite-errors">
          ${suite.errors.map(error => `
            <div class="error-item">
              <strong>${error.test}</strong>
              <p>${error.error}</p>
              ${error.response ? `<p><strong>响应:</strong> <pre>${JSON.stringify(error.response, null, 2)}</pre></p>` : ''}
            </div>
          `).join('')}
        </div>
      ` : ''}
    </div>
  `).join('');

  return `
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>MOOC API 综合测试报告</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }
    .header { background: #4CAF50; color: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; }
    .summary { display: flex; gap: 20px; margin: 20px 0; }
    .summary-card { flex: 1; background: white; padding: 15px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
    .summary-card h3 { margin: 0 0 10px; color: #333; }
    .summary-card .value { font-size: 24px; font-weight: bold; color: #4CAF50; }
    .suites { margin-top: 20px; }
    .suite { background: white; padding: 20px; margin: 15px 0; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
    .suite h3 { margin: 0 0 15px; color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
    .suite-summary { margin: 10px 0; }
    .suite-errors { margin-top: 15px; }
    .error-item { background: #fff3cd; padding: 10px; margin: 5px 0; border-left: 4px solid #f44336; border-radius: 3px; }
    .error-item strong { color: #f44336; }
    .error-item pre { background: #f8f9fa; padding: 10px; border-radius: 3px; overflow-x: auto; }
    .success { color: #4CAF50; }
    .failure { color: #f44336; }
  </style>
</head>
<body>
  <div class="header">
    <h1>🧪 MOOC API 综合测试报告</h1>
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

  <div class="suites">
    <h2>📋 测试套件详情</h2>
    ${suitesHtml}
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

runComprehensiveTests();

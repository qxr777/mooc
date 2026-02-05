const http = require('http');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://localhost:8080';

/**
 * 通用HTTP请求函数
 */
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
    if (options.body) requestOptions.headers['Content-Type'] = 'application/json';

    const req = http.request(requestOptions, (res) => {
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => {
        try {
          const jsonData = data.trim() ? JSON.parse(data) : null;
          resolve({ status: res.statusCode, data: jsonData, rawData: data });
        } catch (error) {
          resolve({ status: res.statusCode, data: data, rawData: data });
        }
      });
    });
    req.on('error', reject);
    if (options.body) req.write(JSON.stringify(options.body));
    req.end();
  });
}

const context = {
  student: { username: 'st' + Math.floor(Math.random() * 1000000), password: '123456', token: null, id: null },
  teacher: { username: 'te' + Math.floor(Math.random() * 1000000), password: '123456', token: null, id: null },
  createdIds: { moocClassId: null, moocClassCode: null, lessonId: null, checkInId: null, subjectId: null, examinationId: null }
};

const getHeaders = (token) => token ? { 'Authorization': `Bearer ${token}` } : {};

function extractData(response) {
  if (!response.data) return null;
  let payload = (typeof response.data === 'object' && response.data.data !== undefined) ? response.data.data : response.data;
  if (typeof payload === 'string' && payload.startsWith('Bearer ')) return payload.substring(7);
  return payload;
}

async function runAllTests() {
  console.log('\n🚀 Starting MOOC API Comprehensive Suite v3.9 (Stable Release)...');
  const results = { passed: 0, failed: 0 };

  try {
    // 1. Auth Suite
    await runSuite(results, '🔐 Authentication Suite', [
      {
        name: 'Register Student',
        method: 'POST',
        url: `${BASE_URL}/auth/register`,
        body: { role: 3, name: context.student.username, password: context.student.password, email: 'st@test.com', nickname: 'Student', studentNo: 'S' + Date.now() },
        expectedStatus: 200,
        onSuccess: (res) => { context.student.id = extractData(res); }
      },
      {
        name: 'Register Teacher',
        method: 'POST',
        url: `${BASE_URL}/auth/register`,
        body: { role: 2, name: context.teacher.username, password: context.teacher.password, email: 'te@test.com', nickname: 'Teacher', salaryNo: 'T' + Date.now() },
        expectedStatus: 200,
        onSuccess: (res) => { context.teacher.id = extractData(res); }
      },
      {
        name: 'Teacher Login',
        method: 'POST',
        url: `${BASE_URL}/auth`,
        body: { username: context.teacher.username, password: context.teacher.password },
        expectedStatus: 200,
        onSuccess: (res) => { context.teacher.token = extractData(res); }
      },
      {
        name: 'Student Login',
        method: 'POST',
        url: `${BASE_URL}/auth`,
        body: { username: context.student.username, password: context.student.password },
        expectedStatus: 200,
        onSuccess: (res) => { context.student.token = extractData(res); }
      }
    ]);

    // 2. Class Suite
    await runSuite(results, '🏫 Class Management', [
      {
        name: 'Create Class',
        method: 'POST',
        url: `${BASE_URL}/mclass`,
        headers: () => getHeaders(context.teacher.token),
        body: () => ({ name: 'Stable API Class', year: '2026', semester: 'Spring', offlineCourse: 'API Integration', teacherId: context.teacher.id }),
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.moocClassId = extractData(res); }
      },
      {
        name: 'Get Code',
        method: 'GET',
        url: () => `${BASE_URL}/mclass/${context.createdIds.moocClassId}`,
        headers: () => getHeaders(context.teacher.token),
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.moocClassCode = extractData(res).code; }
      },
      {
        name: 'Join Class',
        method: 'POST',
        url: `${BASE_URL}/mclass/join`,
        headers: () => getHeaders(context.student.token),
        body: () => ({ moocClassCode: context.createdIds.moocClassCode, userId: context.student.id }),
        expectedStatus: 200
      }
    ]);

    // 3. Check-in Suite
    await runSuite(results, '🤝 Interaction Flow', [
      {
        name: 'Start Lesson',
        method: 'POST',
        url: () => `${BASE_URL}/mclass/${context.createdIds.moocClassId}/start`,
        headers: () => getHeaders(context.teacher.token),
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.lessonId = extractData(res).id; }
      },
      {
        name: 'Open Check-in',
        method: 'POST',
        url: `${BASE_URL}/checkin`,
        headers: () => getHeaders(context.teacher.token),
        body: () => ({ lessonId: context.createdIds.lessonId, longitude: 114.1, latitude: 30.1, gps: true, deadline: new Date(Date.now() + 600000).toISOString() }),
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.checkInId = extractData(res); }
      },
      {
        name: 'Student Check-in',
        method: 'POST',
        url: `${BASE_URL}/checkin/attend`,
        headers: () => getHeaders(context.student.token),
        body: () => ({ checkInId: context.createdIds.checkInId, userId: context.student.id, longitude: 114.1, latitude: 30.1 }),
        expectedStatus: 200
      }
    ]);

    // 4. Examination Suite
    await runSuite(results, '📝 Examination Flow', [
      {
        name: 'Create Subject',
        method: 'POST',
        url: `${BASE_URL}/subject/saveChoice`,
        headers: () => getHeaders(context.teacher.token),
        body: { content: 'Is this v3.9?', score: 10, exerciseId: 1, options: [{ name: 'A', content: 'Yep', correct: true }] },
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.subjectId = extractData(res); }
      },
      {
        name: 'Import Exam',
        method: 'POST',
        url: () => `${BASE_URL}/examination/importFromExercise?lessonId=${context.createdIds.lessonId}&exerciseId=1`,
        headers: () => getHeaders(context.teacher.token),
        expectedStatus: 200,
        onSuccess: (res) => { context.createdIds.examinationId = extractData(res); }
      },
      {
        name: 'Publish Exam (Required for Open)',
        method: 'POST',
        url: () => `${BASE_URL}/examination/publish?lessonId=${context.createdIds.lessonId}&examinationId=${context.createdIds.examinationId}`,
        headers: () => getHeaders(context.teacher.token),
        expectedStatus: 200
      },
      {
        name: 'Submit Answer',
        method: 'POST',
        url: `${BASE_URL}/examination/submit`,
        headers: () => getHeaders(context.student.token),
        body: () => ({
          examinationId: context.createdIds.examinationId,
          userId: context.student.id,
          answerDtos: [{ subjectId: context.createdIds.subjectId, answer: 'A' }] // Note: DTO name is answerDtos internally in records
        }),
        expectedStatus: 200
      }
    ]);

    // 5. Explicit Negative Tests
    await runSuite(results, '🚫 Security & Validation', [
      {
        name: 'Unauthorized Create (Student)',
        method: 'POST',
        url: `${BASE_URL}/mclass`,
        headers: () => getHeaders(context.student.token),
        body: { name: 'Fail', year: 'x', semester: 'x', offlineCourse: 'x', teacherId: context.student.id },
        expectedStatus: 200, // Because of custom ExceptionHandler
        validate: (res) => res.data.code === 1005 || res.data.status === 403
      }
    ]);

  } catch (error) {
    console.error('Test Process Interrupted:', error);
  }

  console.log(`\n🏁 Summary: ${results.passed} Passed, ${results.failed} Failed`);
  process.exit(results.failed > 0 ? 1 : 0);
}

async function runSuite(globalResults, suiteName, tests) {
  console.log(`\n--- ${suiteName} ---`);
  for (const t of tests) {
    const url = typeof t.url === 'function' ? t.url() : t.url;
    const body = typeof t.body === 'function' ? t.body() : t.body;
    const headers = typeof t.headers === 'function' ? t.headers() : t.headers;
    const method = t.method || 'GET';

    process.stdout.write(`  ${t.name.padEnd(35)} ... `);
    const res = await httpRequest({ method, url, headers, body });

    // Check if validation function exists, otherwise default success logic
    const isSuccess = t.validate ? t.validate(res) : (res.status === t.expectedStatus && (res.data ? res.data.code === 1000 : true));

    if (isSuccess) {
      globalResults.passed++;
      console.log('✅');
      if (t.onSuccess) t.onSuccess(res);
    } else {
      globalResults.failed++;
      console.log(`❌ (HTTP: ${res.status}, Code: ${res.data ? res.data.code : 'N/A'}, Msg: ${res.data ? res.data.data || res.data.msg : 'N/A'})`);
    }
  }
}

runAllTests();

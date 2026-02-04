const newman = require('newman');
const fs = require('fs');
const path = require('path');

const collectionPath = path.join(__dirname, '../postman/mooc.postman_collection.json');
const environmentPath = path.join(__dirname, 'environments/test.env.json');

const options = {
  reporters: ['cli', 'html'],
  reporter: {
    html: {
      export: path.join(__dirname, 'reports/newman-report.html'),
    },
    cli: {
      noFail: false,
      noSummary: false,
      showAssertions: true,
    },
  },
  bail: true,
  stopOnError: true,
};

const collection = newman.Collection.Collection.fromFilePath(collectionPath);

const environment = newman.Environment.fromFilePath(environmentPath);

newman.run({
  collection: collection,
  environment: environment,
  iterationCount: 1,
  options: options,
}, (err, summary) => {
  if (err) {
    console.error('测试执行出错:', err);
    process.exit(1);
  }

  console.log('\n========== 测试报告 ==========');
  console.log('总请求数:', summary.run.stats.total);
  console.log('成功请求数:', summary.run.stats.passed);
  console.log('失败请求数:', summary.run.stats.failed);
  console.log('测试通过率: ' + ((summary.run.stats.passed / summary.run.stats.total) * 100).toFixed(2) + '%');
  console.log('总执行时间:', summary.run.timings.completed, 'ms');
  console.log('================================\n');

  if (summary.run.stats.failed > 0) {
    console.error('存在失败的测试，请检查测试报告！');
    process.exit(1);
  }

  console.log('所有测试通过！');
});

const mysql = require("mysql2");

// 注意使用 npm install mysql2 安装依赖
// npm install cors
const pool = mysql.createPool({
    host: "localhost",
    user: "root",
    password: "123456",
    database: "soa_exp1",
    waitForConnections: true,
    connectionLimit: 10,
});

module.exports = pool.promise();

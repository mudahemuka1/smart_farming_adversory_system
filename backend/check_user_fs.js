const mysql = require('mysql2/promise');
const fs = require('fs');

async function checkUser() {
  const conn = await mysql.createConnection({
    host: 'localhost', port: 3306,
    user: 'root', password: '',
    database: 'smartfarming'
  });

  const [rows] = await conn.execute('SELECT id, email, role, is_verified, password FROM User WHERE email LIKE "%mudahemu%"');
  fs.writeFileSync('result_utf8.json', JSON.stringify(rows, null, 2), 'utf-8');
  await conn.end();
}

checkUser().catch(console.error);

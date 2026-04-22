const mysql = require('mysql2/promise');

async function checkUser() {
  const conn = await mysql.createConnection({
    host: 'localhost', port: 3306,
    user: 'root', password: '',
    database: 'smartfarming'
  });

  const [rows] = await conn.execute('SELECT id, email, role, is_verified, password FROM User WHERE email LIKE "%mudahemu%"');
  console.log(JSON.stringify(rows, null, 2));
  await conn.end();
}

checkUser().catch(console.error);

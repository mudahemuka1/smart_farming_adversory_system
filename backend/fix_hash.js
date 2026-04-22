const bcrypt = require('bcryptjs');
const mysql = require('mysql2/promise');
async function run() {
  const connection = await mysql.createConnection({ host: 'localhost', user: 'root', password: '', database: 'smartfarming' });
  const passwords = ['admin12345', 'admin 12345'];
  for (let p of passwords) {
    const hash = await bcrypt.hash(p, 10);
    console.log(Password: "" -> Hash: );
  }
  // Let's force an update for 'admin12345' (no space) to be standard
  const standardHash = await bcrypt.hash('admin12345', 10);
  await connection.execute('UPDATE user SET password = ? WHERE email = ?', [standardHash, 'mudahemukafidela90@gmail.com']);
  console.log('SUCCESS: Updated user "mudahemukafidela90@gmail.com" to use password "admin12345"');
  process.exit(0);
}
run();

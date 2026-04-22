const mysql = require('mysql2');
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'smartfarming'
});
connection.query('ALTER TABLE user DROP COLUMN username', (err, results) => {
  if (err) {
    console.error('FAILED TO DROP COLUMN:', err.message);
    process.exit(1);
  }
  console.log('SUCCESS: Legact username column removed!');
  process.exit(0);
});

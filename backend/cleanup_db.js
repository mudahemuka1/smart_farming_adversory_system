const mysql = require('mysql2/promise');

async function cleanup() {
  const conn = await mysql.createConnection({
    host: 'localhost', port: 3306,
    user: 'root', password: '',
    database: 'smartfarming'
  });

  // Delete answers linked to orphaned questions
  const [r1] = await conn.execute(
    'DELETE a FROM Answer a INNER JOIN Question q ON a.question_id = q.id WHERE q.farmer_id NOT IN (SELECT id FROM Farmer)'
  );
  console.log('Orphaned answers deleted:', r1.affectedRows);

  // Delete orphaned questions
  const [r2] = await conn.execute(
    'DELETE FROM Question WHERE farmer_id NOT IN (SELECT id FROM Farmer)'
  );
  console.log('Orphaned questions deleted:', r2.affectedRows);

  await conn.end();
  console.log('Done! Database is clean.');
}

cleanup().catch(e => {
  // If mysql2 fails, try with mysql package
  console.error('Error:', e.message);
  process.exit(1);
});

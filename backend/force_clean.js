const mysql = require('mysql2');
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
});
async function run() {
  const promiseConnection = connection.promise();
  try {
    await promiseConnection.query('CREATE DATABASE IF NOT EXISTS smartfarming');
    await promiseConnection.query('USE smartfarming');
    
    // WIPE TABLES to be 100% sure we have a clean state
    await promiseConnection.query('SET FOREIGN_KEY_CHECKS = 0');
    const tables = ['answer', 'question', 'recommendation', 'farmer', 'agronomist', 'agro_dealer', 'user', 'crops', 'fertilizer', 'disease'];
    for (let table of tables) {
      await promiseConnection.query(DROP TABLE IF EXISTS );
    }
    await promiseConnection.query('SET FOREIGN_KEY_CHECKS = 1');
    
    console.log('CLEANUP SUCCESS: Tables dropped. Let Spring recreate them now.');
    process.exit(0);
  } catch (err) {
    console.error('REST ERROR:', err.message);
    process.exit(1);
  }
}
run();

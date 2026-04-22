const bcrypt = require('bcrypt');

const hash = '$2a$10$TZ1KkD2Fzm4VT21xC2.WV.XMikjYzRUbrySOR/lluCdtOfDIe868W';
const matches = bcrypt.compareSync('123456', hash);
console.log('Does 123456 match Mudahemukafifi hash?', matches);

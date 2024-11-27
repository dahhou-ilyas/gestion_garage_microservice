const path = require('path');
const fs = require('fs');

const {generateKeyPair} = require('./generateKeys')

function ensureKeyPairExists(){
  const rootDir = process.cwd(); 
    const keysDir = path.join(rootDir, 'keys');
  
    if (!fs.existsSync(keysDir)) {
      fs.mkdirSync(keysDir);
    }

    const privateKeyPath = path.join(keysDir, 'private.key');
    const publicKeyPath = path.join(keysDir, 'public.key');

    if (!fs.existsSync(privateKeyPath) || !fs.existsSync(publicKeyPath)) {
      try {
        const { privateKey, publicKey } = generateKeyPair();
        
        fs.writeFileSync(privateKeyPath, privateKey);
        fs.writeFileSync(publicKeyPath, publicKey);
        
        console.log('Nouvelles clés JWT générées');
      } catch (error) {
        console.error('Erreur lors de la génération des clés:', error);
        process.exit(1);
      }
    }
}

module.exports = {ensureKeyPairExists}
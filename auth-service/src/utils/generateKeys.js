const crypto = require('')
const fs = require('fs');
const path = require('path');

function generateKeyPair(){
    try {
        const { privateKey, publicKey } = crypto.generateKeyPairSync('rsa', {
            modulusLength: 2048,
            publicKeyEncoding: {
                type: 'spki',
                format: 'pem'
            },
            privateKeyEncoding: {
                type: 'pkcs8',
                format: 'pem'
            }
        });
        const rootDir = process.cwd(); // Obtient le dossier racine du projet
        
        fs.writeFileSync(path.join(rootDir, 'private.key'), privateKey);
        fs.writeFileSync(path.join(rootDir, 'public.key'), publicKey);

        console.log('Clés générées avec succès dans le dossier racine du projet');
        return { privateKey, publicKey };
    } catch (error) {
        console.error('Erreur lors de la génération des clés:', error);
        throw error;
    }

}

module.exports = { generateKeyPair };
const crypto = require('crypto')



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

        console.log('Clés générées avec succès dans le dossier racine du projet');
        return { privateKey, publicKey };
    } catch (error) {
        console.error('Erreur lors de la génération des clés:', error);
        throw error;
    }

}

module.exports = { generateKeyPair };
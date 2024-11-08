const jwt = require("jsonwebtoken");
const fs = require('fs');
const path = require('path');

class AuthService {
    constructor() {
        const keysDir = path.join(__dirname, '..', 'keys');

        this.privateKey = fs.readFileSync(
            path.join(keysDir, 'private.key')
        );
        this.publicKey = fs.readFileSync(
            path.join(keysDir, 'public.key')
        );
    }

    generateToken(payload) {
        return jwt.sign(payload, this.privateKey, {
            algorithm: 'RS256',
            expiresIn: process.env.JWT_EXPIRES_IN
        });
    }

    verifyToken(token) {
        try {
            return jwt.verify(token, this.publicKey, { algorithms: ['RS256'] });
        } catch (error) {
            throw new Error('Token invalide');
        }
    }

    getPublicKey() {
        return this.publicKey;
    }
}


module.exports = AuthService;
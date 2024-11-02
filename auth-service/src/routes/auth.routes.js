const express = require('express');
const router = express.Router();
const authController = require('../controllers/auth.controller');

const {authMiddleware,checkRole}=require('../middleware/auth.middleware');

router.post('/register-employee'
    ,authMiddleware
    ,checkRole('admin')
    ,authController.registerEmployee
);

router.post('/login',authController.login);

module.exports = router;
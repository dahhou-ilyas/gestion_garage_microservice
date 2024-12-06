const jwt = require('jsonwebtoken');
const Employee = require('../models/user.model');

const { validationResult } = require('express-validator');

const AuthService = require('../service/authService');
const authService = new AuthService();


const authController = {
    registerEmployee: async (req, res) => {
        try {
            const errors = validationResult(req);
            if (!errors.isEmpty()) {
                return res.status(400).json({ errors: errors.array() });
            }

            const {
                employeeId,
                email,
                password,
                firstName,
                lastName,
                role,
            } = req.body;

            const existingEmployee = await Employee.findOne({ 
                $or: [{ email }, { employeeId }] 
            });

            if (existingEmployee) {
                return res.status(400).json({ 
                message: 'Un employé avec cet email ou ID existe déjà' 
                });
            }

            const employee = new Employee({
                employeeId,
                email,
                password,
                firstName,
                lastName,
                role
            });

            await employee.save();

            res.status(201).json({ 
                message: 'Compte employé créé avec succès' 
            });
        } catch (error) {
            res.status(500).json({ 
                message: 'Erreur serveur', 
                error: error.message 
            });
        }
    },

    login: async (req, res) => {
        try {
            const { username, password } = req.body;
            console.log(username);
            const employee = await Employee.findOne({ email: username });
        
            if (!employee || !(await employee.isValidPassword(password))) {
                return res.status(401).json({ 
                    message: 'Email ou mot de passe incorrect' 
                });
            }
            if (!employee.active) {
                return res.status(401).json({ 
                  message: 'Compte employé désactivé' 
                });
            }
            employee.lastLogin = new Date();
            await employee.save();

            const payload = {
                id: employee._id,
                role: employee.role,
                employeeId: employee.employeeId
            };

            const token = authService.generateToken(payload);

            
            res.json({
                token,
                employee: {
                  id: employee._id,
                  employeeId: employee.employeeId,
                  email: employee.email,
                  role: employee.role,
                  firstName: employee.firstName,
                  lastName: employee.lastName
                }
            });
        } catch (error) {
            res.status(500).json({ 
                message: 'Erreur serveur', 
                error: error.message 
            });
        }
    },

    getPublicKey: (req, res) => {
        try {
            const publicKey = authService.getPublicKey();
            res.json({ publicKey: publicKey.toString() });
        } catch (error) {
            res.status(500).json({
                message: 'Erreur lors de la récupération de la clé publique',
                error: error.message
            });
        }
    }

}

module.exports = authController
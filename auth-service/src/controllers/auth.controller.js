const jwt = require('jsonwebtoken');
const Employee = require('../models/user.model');

const { validationResult } = require('express-validator');

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
            const { email, password } = req.body;
            const employee = await Employee.findOne({ email });
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
            const token = jwt.sign(
                { 
                  id: employee._id,
                  role: employee.role,
                  employeeId: employee.employeeId 
                },
                process.env.JWT_SECRET,
                { expiresIn: process.env.JWT_EXPIRES_IN }
            );
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
    }
}

module.exports = authController
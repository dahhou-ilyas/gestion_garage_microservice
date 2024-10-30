const request = require("supertest");

const chai = require('chai');
const { expect } = chai;
const app= require('../src/app');

const mongoose = require('mongoose');
const connectDB = require('../src/config/db.config'); 
const Employee = require('../src/models/user.model');

describe('Auth API', () => {

    let token;

    before(async () => {
        await connectDB();
    });

    beforeEach(async () => {
        // Nettoyer la base de données avant chaque test
        await Employee.deleteMany({});
    });

    after(async () => {
        // Déconnexion de la base de données après les tests
        await mongoose.connection.close();
    });

    it('should register an employee', async () => {
        const res = await request(app)
            .post('/api/auth/register-employee')
            .send({
                employeeId: '123',
                email: 'test@example.com',
                password: 'password',
                firstName: 'John',
                lastName: 'Doe',
                role: 'admin'
            });
        
        expect(res.status).to.equal(201);
        expect(res.body).to.have.property('message', 'Compte employé créé avec succès');
    });

})

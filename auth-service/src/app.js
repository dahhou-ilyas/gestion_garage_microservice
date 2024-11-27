const {ensureKeyPairExists} = require('./utils/ensureKeyPairExists')

ensureKeyPairExists();


const express= require('express');


const cors = require('cors');

const dotenv=require('dotenv');

const Employee=require('./models/user.model');

const connectDB=require('./config/db.config');

const authRoutes=require('./routes/auth.routes');

const eurekaClient = require('./eurekaConfig/eureka-client'); 

dotenv.config();

const app=express();

connectDB();

app.use(cors());

app.use(express.json());

app.use('/api/auth', authRoutes);


const createAdminIfNotExists = async () => {
  const adminExists = await Employee.findOne({ role: 'admin' });
  
  if (!adminExists) {
      const adminData = {
          employeeId: 'admin123',
          email: 'admin@example.com',
          password: 'password123',
          role: 'admin',
          firstName: 'Admin',
          lastName: 'User'
      };

      const admin = new Employee(adminData);
      await admin.save();
      console.log('Admin created:', admin);
  } else {
      console.log('Admin already exists:', adminExists);
  }
};

createAdminIfNotExists().catch(err => console.error('Error creating admin:', err));

app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something broke!' });
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`Auth service running on port ${PORT}`);
  
  eurekaClient.start(error => {
    if (error) {
      console.error('Eureka client start error:', error);
    } else {
      console.log('Eureka client started');
    }
  });
});

process.on('SIGINT', () => {
  eurekaClient.stop();
  process.exit();
});

module.exports = app;
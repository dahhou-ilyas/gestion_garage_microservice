const express= require('express');

const cors = require('cors');

const dotenv=require('dotenv');

const connectDB=require('./config/db.config');

const authRoutes=require('./routes/auth.routes');

dotenv.config();

const app=express();

connectDB();

app.use(cors());

app.use(express.json());

app.use('/api/auth', authRoutes);

app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something broke!' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Auth service running on port ${PORT}`);
});
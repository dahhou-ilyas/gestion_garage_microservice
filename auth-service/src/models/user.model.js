const mongoose=require("mongoose");

const bcrypt=require('bcryptjs');

const employeeSchema = new mongoose.Schema({
    employeeId: {
      type: String,
      required: true,
      unique: true
    },
    email: {
      type: String,
      required: true,
      unique: true,
      lowercase: true
    },
    password: {
      type: String,
      required: true,
      minlength: 6
    },
    role: {
      type: String,
      enum: ['admin', 'employee'],
      required: true
    },
    firstName: {
      type: String,
      required: true
    },
    lastName: {
      type: String,
      required: true
    },
    active: {
      type: Boolean,
      default: true
    },
    lastLogin: {
      type: Date
    },
    createdAt: {
      type: Date,
      default: Date.now
    }
});

employeeSchema.pre("save",async function (next) {
    if(!this.isModified('password')) return next();
    this.password=await bcrypt.hash(this.password,12);
    next();
});

employeeSchema.methods.isValidPassword = async function(candidatePassword) {
    return await bcrypt.compare(candidatePassword, this.password);
};

module.exports = mongoose.model('Employee', employeeSchema);
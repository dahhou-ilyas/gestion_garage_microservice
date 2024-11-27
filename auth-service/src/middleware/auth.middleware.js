const Employee=require('../models/user.model');
const AuthService = require('../service/authService');
const authService = new AuthService();

const authMiddleware =async (req,res,next)=>{
    try {
        const token=req.headers.authorization?.split(' ')[1];
        if(!token){
            return res.status(401).json({ message: 'Authentification requise' });
        }

        const decoded = authService.verifyToken(token);


        const employee = await Employee.findById(decoded.id);

        if (!employee || !employee.active) {
            return res.status(401).json({ message: 'Compte employé non valide ou inactif' });
        }

        req.employee = employee;
        next();

    } catch (error) {
        return res.status(401).json({ message: 'Token invalide' });
    }
}

const checkRole = (...roles) => {
    return (req,res,next)=>{
        if(!roles.includes(req.employee.role)){
            return res.status(403).json({ 
                message: 'Accès non autorisé pour votre rôle' 
            });
        }
        next();
    }
}

module.exports = { authMiddleware, checkRole };
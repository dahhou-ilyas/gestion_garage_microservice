# Garage Management Microservices System

A comprehensive garage management system built with microservices architecture to handle customer management, vehicle registration, maintenance scheduling, billing, and notifications.

## 🏗️ Architecture Overview

This system consists of 8 microservices communicating through Kafka message queues and managed by a Spring Cloud Gateway with Eureka service discovery.

### Services Overview

| Service | Port | Technology | Database | Description |
|---------|------|------------|----------|-------------|
| **Discovery Service** | 8761 | Spring Boot | - | Eureka service registry |
| **Gateway Service** | 8888 | Spring Cloud Gateway | - | API Gateway with JWT authentication |
| **Auth Service** | 3000 | Node.js/Express | MongoDB | Authentication & authorization |
| **Customer Service** | 8081 | Spring Boot | PostgreSQL | Customer management |
| **Cars Service** | 8082 | Spring Boot | PostgreSQL | Vehicle registration & management |
| **Notification Service** | 8083 | Spring Boot | Redis | Email notifications |
| **Workshop Service** | 8084 | Spring Boot | PostgreSQL | Maintenance scheduling |
| **Billing Service** | 8085 | Spring Boot | PostgreSQL | Invoice generation & billing |
| **Frontend** | 5173 | React/Vite | - | User interface |

## 🚀 Key Features

### Customer Management
- Customer registration and profile management
- Customer data validation and storage
- Integration with vehicle and billing systems

### Vehicle Management
- Vehicle registration with owner validation
- Duplicate registration number prevention
- Vehicle status tracking (available, in maintenance, etc.)
- Automatic email notifications on registration

### Maintenance Scheduling
- Schedule maintenance appointments
- Automatic completion based on estimated time
- Real-time status updates
- Integration with billing system

### Billing System
- Automatic invoice generation
- PDF invoice creation
- Email delivery with attachments
- Integration with maintenance completion

### Notification System
- Email notifications for vehicle registration
- Invoice delivery with PDF attachments
- Event-driven notifications via Kafka

### Authentication & Security
- JWT-based authentication with RSA keys
- Role-based access control (admin, employee)
- Public key distribution for token verification
- Gateway-level token validation

## 🛠️ Technology Stack

### Backend Services
- **Java 17** with Spring Boot 3.x
- **Node.js 18** with Express (Auth service)
- **Spring Cloud Gateway** for API routing
- **Eureka** for service discovery
- **Apache Kafka** for event-driven communication

### Databases
- **PostgreSQL** for relational data (customers, cars, billing, workshop)
- **MongoDB** for authentication data
- **Redis** for caching and session management

### Frontend
- **React 18** with Vite
- **Axios** for HTTP requests
- **Tailwind CSS** for styling

### Infrastructure
- **Docker** & **Docker Compose** for containerization
- **Maven** for Java dependency management
- **npm** for Node.js dependencies

## 📁 Project Structure

```
gestion_garage_microservice/
├── auth-service/                 # Node.js authentication service
│   ├── src/
│   │   ├── controllers/         # Authentication controllers
│   │   ├── middleware/          # JWT middleware
│   │   ├── models/              # User models
│   │   ├── service/             # Authentication logic
│   │   └── utils/               # Key generation utilities
│   └── tests/                   # Unit tests
├── customer-service/            # Spring Boot customer management
├── cars-service/                # Spring Boot vehicle management
├── workshop-service/            # Spring Boot maintenance scheduling
├── billing-service/             # Spring Boot billing system
├── notification-service/        # Spring Boot notification system
├── gateway-service/             # Spring Cloud Gateway
├── discovery-service/           # Eureka server
├── frontend/                    # React frontend application
├── k8s-config/                  # Kubernetes deployment files
├── istio-addons/               # Istio service mesh configuration
└── docker-compose.yml          # Docker orchestration
```

## 🔧 Setup and Installation

### Prerequisites
- Docker & Docker Compose
- Java 17+
- Node.js 18+
- Maven 3.8+

### Quick Start with Docker Compose

1. **Clone the repository**
```bash
git clone https://github.com/dahhou-ilyas/gestion_garage_microservice.git
cd gestion_garage_microservice
```

2. **Build all service images**
```bash
# Build each service
docker build -t discovery-service ./discovery-service
docker build -t gateway-service ./gateway-service
docker build -t auth-service ./auth-service
docker build -t customer-service ./customer-service
docker build -t cars-service ./cars-service
docker build -t workshop-service ./workshop-service
docker build -t billing-service ./billing-service
docker build -t notification-service ./notification-service
```

3. **Start all services**
```bash
docker-compose up -d
```

4. **Verify services are running**
```bash
docker-compose ps
```

### Manual Development Setup

#### 1. Start Infrastructure
```bash
# Start databases and message queue
docker-compose up -d postgres mongo redis kafka zookeeper
```

#### 2. Start Discovery Service
```bash
cd discovery-service
mvn spring-boot:run
```

#### 3. Start Gateway Service
```bash
cd gateway-service
mvn spring-boot:run
```

#### 4. Start Auth Service
```bash
cd auth-service
npm install
npm start
```

#### 5. Start Business Services
```bash
# In separate terminals
cd customer-service && mvn spring-boot:run
cd cars-service && mvn spring-boot:run
cd workshop-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

#### 6. Start Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🔌 API Endpoints

### Authentication Service (Port 3000)
- `POST /api/auth/register-employee` - Register employee
- `POST /api/auth/login` - Login
- `GET /api/auth/public-key` - Get JWT public key

### Customer Service (Port 8081)
- `GET /api/customers` - List customers
- `POST /api/customers` - Create customer
- `GET /api/customers/{id}` - Get customer by ID

### Cars Service (Port 8082)
- `GET /api/cars` - List all cars
- `POST /api/cars` - Register new car
- `GET /api/cars/owner/{ownerId}` - Get cars by owner
- `PUT /api/cars/{id}` - Update car
- `DELETE /api/cars/{id}` - Delete car

### Workshop Service (Port 8084)
- `GET /api/maintenance` - List maintenance schedules
- `POST /api/maintenance` - Schedule maintenance
- `PUT /api/maintenance/{id}/complete` - Complete maintenance

### Gateway (Port 8888)
All services are accessible through the gateway:
- `http://localhost:8888/customer-service/**`
- `http://localhost:8888/cars-service/**`
- `http://localhost:8888/workshop-service/**`
- `http://localhost:8888/billing-service/**`

## 📨 Event-Driven Architecture

### Kafka Topics
- `car-created-events` - Vehicle registration notifications
- `maintenance-completed-events` - Maintenance completion
- `billing-events` - Invoice generation and delivery

### Event Flow
1. **Car Registration**: Cars Service → Notification Service (email)
2. **Maintenance Completion**: Workshop Service → Billing Service → Notification Service
3. **Invoice Generation**: Billing Service → Notification Service (PDF email)

## 🔐 Security

### JWT Authentication
- RSA key pair generation for token signing/verification
- Public key distribution through auth service
- Gateway-level token validation for all requests

### CORS Configuration
- Frontend origins whitelisted in gateway
- Flexible CORS patterns for development

## 🧪 Testing

### Run Auth Service Tests
```bash
cd auth-service
npm test
```

### Test API Endpoints
```bash
# Login to get token
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@example.com","password":"password"}'

# Use token for authenticated requests
curl -X GET http://localhost:8888/customer-service/api/customers \
  -H "Authorization: Bearer <your-jwt-token>"
```

## 📊 Monitoring and Observability

### Service Discovery
- Eureka Dashboard: http://localhost:8761

### Application Logs
```bash
# View service logs
docker-compose logs -f [service-name]

# View all logs
docker-compose logs -f
```

## 🔄 Data Flow Examples

### 1. Vehicle Registration Flow
```
Frontend → Gateway → Cars Service → Customer Service (validation)
    ↓
Cars Service → Kafka (car-created-event)
    ↓
Notification Service → Email Service (registration confirmation)
```

### 2. Maintenance Completion Flow
```
Workshop Service (scheduler) → Complete Maintenance
    ↓
Workshop Service → Cars Service (update status)
    ↓
Workshop Service → Kafka (maintenance-completed-event)
    ↓
Billing Service → Generate Invoice → Kafka (billing-event)
    ↓
Notification Service → Email with PDF attachment
```

## 🚀 Deployment

### Docker Production Deployment
```bash
# Production environment
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

### Kubernetes Deployment
```bash
# Apply Kubernetes configurations
kubectl apply -f k8s-config/
```

### Istio Service Mesh
```bash
# Apply Istio configurations
kubectl apply -f istio-addons/
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📝 Environment Variables

### Required Environment Variables
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=your_db_name
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Service Discovery
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Email Configuration (Notification Service)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 🐛 Troubleshooting

### Common Issues

1. **Service not registering with Eureka**
   - Check network connectivity
   - Verify Eureka server is running
   - Check service configuration

2. **Database connection issues**
   - Verify PostgreSQL is running
   - Check connection credentials
   - Ensure databases are created

3. **Kafka connection problems**
   - Verify Kafka and Zookeeper are running
   - Check topic creation
   - Verify bootstrap servers configuration

4. **JWT token issues**
   - Ensure auth service is running
   - Verify public key endpoint accessibility
   - Check token expiration

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.


---

**Built with ❤️ using Spring Boot, Node.js, and React**

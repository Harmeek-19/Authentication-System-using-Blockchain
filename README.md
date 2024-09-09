# BlockchainX Authentication System

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [System Architecture](#system-architecture)
5. [Setup and Installation](#setup-and-installation)
6. [Usage](#usage)
7. [API Endpoints](#api-endpoints)
8. [Database Schema](#database-schema)
9. [Blockchain Implementation](#blockchain-implementation)
10. [Security Measures](#security-measures)
11. [Testing](#testing)
12. [Known Issues and Limitations](#known-issues-and-limitations)
13. [Future Enhancements](#future-enhancements)
14. [Contributing](#contributing)
15. [License](#license)

## Project Overview
BlockchainX is a secure authentication system that leverages blockchain technology to enhance user authentication and transaction logging. This project combines traditional web authentication methods with the immutability and transparency of blockchain, providing a robust and innovative solution for user management and secure transactions.

## Features
- User registration with email confirmation
- Secure login with JWT authentication
- Password reset functionality
- Blockchain-based transaction logging
- Public key cryptography for user identification
- Admin dashboard for user management
- Blockchain status viewing

## Technologies Used
- Backend:
  - Java 17
  - Spring Boot 3.x
  - Spring Security
  - JPA/Hibernate
  - PostgreSQL (H2 for development)
- Frontend:
  - React.js
  - Next.js
  - Tailwind CSS
- Blockchain:
  - Custom implementation using Java
- Authentication:
  - JWT (JSON Web Tokens)
- Build Tool:
  - Maven

## System Architecture
The system is divided into two main components:
1. Backend API (Spring Boot)
2. Frontend Application (React/Next.js)

The backend handles user authentication, blockchain operations, and database interactions. The frontend provides a user interface for registration, login, and interacting with the blockchain features.

## Setup and Installation
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/blockchainx.git
   cd blockchainx
   ```

2. Backend Setup:
   ```
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. Frontend Setup:
   ```
   cd frontend
   npm install
   npm run dev
   ```

4. Database:
   - For development, the application uses H2 in-memory database.
   - For production, configure PostgreSQL in `application.properties`.

5. Environment Variables:
   - Set up necessary environment variables as specified in `.env.example` files in both backend and frontend directories.

## Usage
1. Access the application at `http://localhost:3000`
2. Register a new account
3. Confirm your email address
4. Log in to access the dashboard
5. Explore blockchain features and transaction logging

## API Endpoints
- POST `/api/users/register` - User registration
- POST `/api/users/login` - User login
- GET `/api/users/confirm-email` - Email confirmation
- POST `/api/users/request-password-reset` - Request password reset
- POST `/api/users/reset-password` - Reset password
- GET `/api/users/public-key` - Get user's public key
- GET `/api/blockchain/status` - Get blockchain status
- POST `/api/blockchain/transaction` - Create a new transaction

For a complete list of endpoints and their descriptions, refer to the API documentation.

## Database Schema
The main entities in the database are:
- User
- Transaction
- Block

Refer to the entity classes in the `com.authen.sec.model` package for detailed schema information.

## Blockchain Implementation
The blockchain is implemented as a chain of blocks, where each block contains:
- A list of transactions
- A timestamp
- The previous block's hash
- Its own hash

The system uses a simple Proof of Work algorithm for mining new blocks.

## Security Measures
- Password hashing using BCrypt
- JWT for stateless authentication
- Email confirmation for new registrations
- Secure password reset mechanism
- Public key cryptography for blockchain transactions

## Testing
- Unit tests for service layer logic
- Integration tests for API endpoints
- Frontend component testing using Jest and React Testing Library

To run tests:
```
mvn test        # For backend tests
npm run test    # For frontend tests
```

## Known Issues and Limitations
- The blockchain implementation is simplified and not suitable for production use without further enhancements.
- The system currently doesn't support multi-node blockchain network.

## Future Enhancements
- Implement a distributed blockchain network
- Add support for smart contracts
- Enhance admin features for better blockchain management
- Implement real-time updates using WebSockets

## Contributing
Contributions to BlockchainX are welcome! Please follow these steps:
1. Fork the repository
2. Create a new branch for your feature
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

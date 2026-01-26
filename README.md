# NexBuy E-commerce Backend

This is the backend service for the NexBuy E-commerce application.

## Setup and Configuration

### Prerequisites
- Java 17 or higher
- MySQL 8.0
- Maven

### Database Setup
1. Create a MySQL database named `full-stack-ecommerce`
2. Update the database connection details in `application.properties` if needed

### Environment Variables
For security reasons, sensitive information such as API keys are not stored in the repository. 
You need to set up the following environment variables:

#### Required Environment Variables
The following environment variables should be set for production environments:

- `STRIPE_API_KEY`: Your Stripe secret key
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `OKTA_ISSUER`: Auth0/Okta issuer URL (must end with "/")
- `OKTA_CLIENT_ID`: Auth0/Okta client ID

#### Setting Environment Variables

**Windows:**
```
set STRIPE_API_KEY=your_stripe_secret_key
set DB_USERNAME=your_db_username
set DB_PASSWORD=your_db_password
set OKTA_ISSUER=your_okta_issuer_url
set OKTA_CLIENT_ID=your_okta_client_id
```

**Linux/Mac:**
```
export STRIPE_API_KEY=your_stripe_secret_key
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export OKTA_ISSUER=your_okta_issuer_url
export OKTA_CLIENT_ID=your_okta_client_id
```

**IntelliJ IDEA:**
1. Go to Run → Edit Configurations
2. Select your Spring Boot configuration
3. Add the environment variables in the "Environment variables" field:
   ```
   STRIPE_API_KEY=your_stripe_secret_key;DB_USERNAME=your_db_username;DB_PASSWORD=your_db_password
   ;OKTA_ISSUER=your_okta_issuer_url;OKTA_CLIENT_ID=your_okta_client_id
   ```

#### Development Environment
For development, default values are provided in the application.properties file, but it's recommended to use environment
variables even in development for consistency with production.

### Running the Application
```
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api`

## API Documentation
The API endpoints are documented using Swagger UI and can be accessed at `http://localhost:8080/swagger-ui.html` 
when the application is running.

## Security
- Never commit sensitive information like API keys to the repository
- Always use environment variables for sensitive configuration
- Regularly rotate API keys and secrets

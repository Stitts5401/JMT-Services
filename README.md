### Latest Update Notes:

Implemented a preliminary version of the session, role, and user management features. The modifications primarily focused on configuring Redis, streamlining security settings, introducing a custom exception, and refining controller logic.

#### Detailed Changes:

- **`pom.xml`**: Updated project dependencies.
- **`SecurityApplication.java`**: Introduced new Spring Boot configurations.
- **`CustomAuthenticationEntryPoint.java`**: Removed redundant custom authentication entry point.
- **`DatabaseConfig.java`**: Minor configuration change.
- **`EncoderConfig.java`**: 
  - Renamed from `PasswordEncoderConfig`.
  - Added new encoding configurations.
- **`RedisConfig.java`**: Overhauled Redis configurations, focusing on session management.
- **`SecurityConfig.java`**: Incorporated new security configurations, particularly for user and role management.
- **`WebConfig.java`**: Revised web configurations.
- **`LoginController.java`**: Refactored login logic.
- **`ResourceController.java`**: Modified resource access methods.
- **`UserController.java`**: Updated user management functions.
- **`CustomUserDetails.java`**: Enhanced custom user details attributes.
- **`User.java`**: Minor entity attribute adjustment.
- **`UserNotFoundException.java`**: Introduced a custom exception for user not found scenarios.
- **`RoleBasedAuthenticationSuccessHandler.java`**: Updated authentication success scenarios.
  
#### Repository Layer:
- **`RoleRepository.java`**: New repository interface for role management.
- **`UserRepository.java` & `UserRoleRepository.java`**: Minor repository tweaks.

#### Service Layer:
- **`CustomReactiveUserDetailsService.java`**: Revised custom reactive user details service.
- **`UserService.java`**: Streamlined user service methods.
  
- **`Logback.xml`**: Optimized logging configurations.
- **`application.properties`**: Adjusted application settings.

#### Template Adjustments:
- **`forgot-password.html`**: Made minor UI tweaks.
- **`home.html`**: Renamed from `index.html`.
- **`login-error.html`**: Renamed from `error.html`.
- **`login.html` & `sign-up.html`**: Refined login and sign-up UI.
- **`account-profile.html`**: Unspecified updates.

Overall, these changes pave the way for a more coherent and efficient user, role, and session management experience within the security service.

### TODO List for Separating User/Authentication Service from UI

- [ ] **Create a New User/Authentication Service**:
    - [ ] Set up a new microservice project for user management and authentication.
    - [ ] Transfer all backend components (models, repositories, controllers) related to user management and authentication.
    - [ ] Implement endpoints for authentication actions (login, logout, registration, etc.)

- [ ] **Update the UI Service**:
    - [ ] Remove backend components related to user management and authentication.
    - [ ] For authentication or user operations, make an HTTP call to the user/authentication service.
    - [ ] Securely store received authentication token or session ID.

- [ ] **Update Security Configuration**:
    - [ ] Secure specific endpoints in the UI service.
    - [ ] Validate user's authenticity and authorization using the token or session ID.

- [ ] **Centralize Configuration**:
    - [ ] If not already, utilize Spring Cloud Config or a similar service for centralized configuration management.

- [ ] **Handle UI Redirections**:
    - [ ] Implement redirects for unauthenticated users trying to access protected pages.

- [ ] **Testing**:
    - [ ] Ensure integration tests exist to verify interaction between UI and user/authentication service.

- [ ] **Deployment**:
    - [ ] Set up independent deployments for both services. Consider containerization tools like Docker.

- [ ] **Service Discovery**:
    - [ ] Integrate a service discovery mechanism, e.g., Netflix Eureka.

- [ ] **Consider API Gateway**:
    - [ ] If expanding microservices, set up an API gateway like Spring Cloud Gateway or Netflix Zuul.

**Note**: After completing each task, mark it as done `[x]` for easier tracking.

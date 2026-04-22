# Smart Farming Advisory System

A full-stack web application designed to help farmers make informed agricultural decisions through smart recommendations and agronomist support.

## Tech Stack
- **Backend:** Java Spring Boot, Spring Security (JWT), Spring Data JPA, MySQL
- **Frontend:** React, Vite, Framer Motion (Animations), Lucide-React (Icons), Axios
- **Architecture:** Clean Layered Architecture (Controller-Service-Repository)

## Project Structure
```text
smart-farming-advisory/
├── backend/                # Spring Boot application
│   ├── src/main/java/      # Source code
│   └── src/main/resources/ # Configuration
├── frontend/               # React Vite application
│   └── src/                # UI components & services
├── schema.sql              # Database schema
├── sample_data.sql         # Seed data for testing
└── postman_collection.json # API testing collection
```

## Setup Instructions

### 1. Database Setup
1. Open your MySQL client (Workbench or CLI).
2. Execute `schema.sql` to create the database and tables.
3. (Optional) Execute `sample_data.sql` to populate the system with crops and diseases.

### 2. Backend Setup
1. Navigate to `backend/`.
2. Ensure you have Java 17+ installed.
3. Update `src/main/resources/application.yml` with your MySQL password if necessary.
4. Run: `mvn spring-boot:run`.
   - The API will be available at `http://localhost:8080`.
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`.

### 3. Frontend Setup
1. Navigate to `frontend/`.
2. Run: `npm install`.
3. Run: `npm run dev`.
4. The web app will be available at `http://localhost:5173`.

## Roles & Features
- **Farmer:** Get crop recommendations, search diseases, ask questions to agronomists.
- **Agronomist:** Respond to farmer questions, provide specialized advice.
- **Agro-Dealer:** Manage fertilizer stock and details.
- **Admin:** Overall system management (CRUD on crops, diseases, fertilizers).

## API Endpoints Summary
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Authenticate & get JWT
- `GET /api/crops/recommend?soil=...&season=...` - Get smart advice
- `POST /api/questions` - Farmers ask agronomists
- `POST /api/questions/{id}/answers` - Agronomists respond

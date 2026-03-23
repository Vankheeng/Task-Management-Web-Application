# Taskei - Task Management System

A full-stack task management web application built with Spring Boot and React.

## Features

- **Team Management** — Create teams, manage members with Admin/Member roles
- **Project Management** — Organize work into projects with custom statuses
- **Task Management** — Create, assign, and track tasks with priority and deadline
- **Real-time Notifications** — Get notified on task assignments, status updates, comments
- **Calendar View** — Visualize tasks by deadline across months
- **Role-based Access Control** — Admins manage structure, members manage tasks

## Tech Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Hibernate
- MySQL
- MapStruct
- Lombok

### Frontend
- React 18 + Vite
- React Router v6
- Axios
- Tailwind CSS
- Day.js
- React Icons

## Project Structure

```
taskmanagement/
├── backend/                  # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/myapplication/taskmanagement/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── entity/
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   └── response/
│   │   │   │   ├── mapper/
│   │   │   │   ├── exception/
│   │   │   │   └── utils/
│   │   │   └── resources/
│   │   │       └── application.yaml
│   │   └── test/
│   └── pom.xml
├── frontend/                 # React application
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   │   ├── common/
│   │   │   ├── task/
│   │   │   ├── team/
│   │   │   ├── project/
│   │   │   ├── taskList/
│   │   │   ├── notification/
│   │   │   └── calendar/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── pages/
│   │   │   ├── auth/
│   │   │   ├── team/
│   │   │   ├── project/
│   │   │   ├── taskList/
│   │   │   ├── task/
│   │   │   ├── notification/
│   │   │   ├── calendar/
│   │   │   └── profile/
│   │   ├── routes/
│   │   └── utils/
│   ├── package.json
│   └── vite.config.js
└── document/                 # Documentation
```

## Getting Started

### Prerequisites

- Java 21+
- Node.js 18+
- MySQL 8+
- Maven 3.8+

### Backend Setup

**1. Configure database**

Create a MySQL database:
```sql
CREATE DATABASE `task-management`;
```

**2. Configure application.yaml**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/task-management
    username: ${DBMS_USERNAME:root}
    password: ${DBMS_PASSWORD:your_password}
```

**3. Run backend**

```bash
cd backend
mvn spring-boot:run
```

Backend runs at `http://localhost:8080/task-management`

### Frontend Setup

**1. Install dependencies**

```bash
cd frontend
npm install
```

**2. Configure environment**

Create `.env` file in `frontend/`:
```env
VITE_API_URL=http://localhost:8080
```

**3. Run frontend**

```bash
npm run dev
```

Frontend runs at `http://localhost:5173`

## API Endpoints

| Module | Endpoint | Description |
|--------|----------|-------------|
| Auth | `POST /auth/token` | Login |
| Auth | `POST /auth/logout` | Logout |
| Users | `POST /users` | Register |
| Users | `GET /users/my-info` | Get current user |
| Teams | `GET /teams/my-teams` | Get my teams |
| Teams | `POST /teams` | Create team |
| Team Members | `GET /team-members/team/{teamId}` | Get team members |
| Projects | `GET /projects/team/{teamId}` | Get projects by team |
| Statuses | `GET /statuses/project/{projectId}` | Get statuses by project |
| Task Lists | `GET /task-lists/project/{projectId}` | Get task lists |
| Tasks | `GET /tasks/task-list/{taskListId}` | Get tasks |
| Tasks | `GET /tasks/{taskId}` | Get task detail |
| Tasks | `GET /tasks/my-tasks?startDay=&endDay=` | Get my tasks by deadline |
| Notifications | `GET /notifications` | Get notifications |

## Role-based Permissions

| Action | Admin | Member |
|--------|-------|--------|
| Create/Delete project | ✅ | ❌ |
| Create task list | ✅ | ✅ |
| Delete task list | ✅ | ❌ |
| Create task | ✅ | ✅ |
| Edit task | ✅ | ✅ |
| Delete task | ✅ | ❌ |
| Add/Remove member | ✅ | ❌ |
| Comment | ✅ | ✅ |
| Update task status | ✅ | ✅ |

## Environment Variables

### Backend
| Variable | Default | Description |
|----------|---------|-------------|
| `DBMS_USERNAME` | `root` | Database username |
| `DBMS_PASSWORD` | `123456` | Database password |
| `JWT_SIGNER_KEY` | — | JWT signing key |

### Frontend
| Variable | Default | Description |
|----------|---------|-------------|
| `VITE_API_URL` | `http://localhost:8080` | Backend URL |

## License

MIT License
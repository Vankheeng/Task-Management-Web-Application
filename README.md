# Taskei - Task Management System

A full-stack task management web application built with Spring Boot and React.

---

## Features

- **Team Management** — Create teams, manage members with Admin/Member roles
- **Project Management** — Organize work into projects with custom statuses
- **Task Management** — Create, assign, and track tasks with priority and deadline
- **Notifications** — Get notified on task assignments, status updates, comments
- **Calendar View** — Visualize tasks by deadline across months
- **Role-based Access Control** — Admins manage structure, members manage tasks

---

## Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.x |
| Spring Security + JWT | — |
| Spring Data JPA | — |
| MySQL | 8.0 |
| MapStruct | 1.5.x |
| Lombok | — |

### Frontend
| Technology | Version |
|---|---|
| React | 18 |
| Vite | 5.x |
| React Router | 6 |
| Axios | — |
| Tailwind CSS | 3.x |
| Day.js | — |

---

## Project Structure

```
taskmanagement/
├── backend/
│   ├── src/main/java/com/myapplication/taskmanagement/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── mapper/
│   │   ├── exception/
│   │   └── utils/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── pages/
│   │   └── utils/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── document/
│   ├── ERD.png 
│   ├── Frontend Design.pdf
│   └── User Interface Demo.pdf 
├── docker-compose.yml
├── .env
└── README.md
```
## Frontend Design
[View on Figma](https://www.figma.com/design/iQS5E32Zldf4Cq1drg9IYL/Taskie---Task-Management?node-id=0-1&t=1ffwwfUX4j1D8QKr-1)

---

## Getting Started

### Option 1: Docker (Recommended)

**Prerequisites:** Docker Desktop

**Step 1 — Clone repository**
```bash
git clone https://github.com/your-username/taskmanagement.git
cd taskmanagement
```

**Step 2 — Configure .env**
```env
MYSQL_ROOT_PASSWORD=123456
DBMS_USERNAME=root
DBMS_PASSWORD=123456
JWT_SIGNER_KEY=your-secret-key-at-least-256-bits-long
```

**Step 3 — Build and run**
```bash
docker-compose up --build
```

**Step 4 — Access**
| Service | URL |
|---|---|
| Frontend | http://localhost |
| Backend | http://localhost:8080/task-management |
| MySQL | localhost:3307 |

```bash
# Stop
docker-compose down

# Stop and remove data
docker-compose down -v
```

---

### Option 2: Local Development

**Prerequisites:** Java 21+, Node.js 18+, MySQL 8+, Maven 3.9+

**Step 1 — Setup database**
```sql
CREATE DATABASE `task-management`;
```

**Step 2 — application.yaml**
```yaml
spring:
  datasource:
    url: "jdbc:mysql://localhost:3307/task-management"
    username: root
    password: your_password
```

**Step 3 — Run backend**
```bash
cd backend
mvn spring-boot:run
# Runs at http://localhost:8080/task-management
```

**Step 4 — Run frontend**
```bash
cd frontend
npm install
npm run dev
# Runs at http://localhost:5173
```

---

## application.yaml — Docker vs Local

| Setting | Local | Docker |
|---|---|---|
| `DB_HOST` | `localhost` | `mysql` (service name) |
| `DB_PORT` | `3307` | `3306` |

```yaml
# Tự động chuyển đổi qua biến môi trường
url: "jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3307}/task-management"
```

---

## API Endpoints

| Module | Method | Endpoint |
|---|---|---|
| Auth | POST | `/auth/token` |
| Auth | POST | `/auth/logout` |
| Users | POST | `/users` |
| Users | GET | `/users/my-info` |
| Users | GET | `/users/search?username=` |
| Teams | GET | `/teams/my-teams` |
| Teams | POST/PUT/DELETE | `/teams`, `/teams/{id}` |
| Team Members | GET | `/team-members/team/{teamId}` |
| Team Members | POST/PUT/DELETE | `/team-members`, `/team-members/{id}` |
| Projects | GET | `/projects/team/{teamId}` |
| Projects | POST/PUT/DELETE | `/projects`, `/projects/{id}` |
| Statuses | GET | `/statuses/project/{projectId}` |
| Statuses | POST/PUT/DELETE | `/statuses`, `/statuses/{id}` |
| Task Lists | GET | `/task-lists/project/{projectId}` |
| Task Lists | POST/PUT/DELETE | `/task-lists`, `/task-lists/{id}` |
| Tasks | GET | `/tasks/task-list/{taskListId}` |
| Tasks | GET | `/tasks/{taskId}` |
| Tasks | GET | `/tasks/my-tasks?startDay=&endDay=` |
| Tasks | POST/PUT/DELETE | `/tasks`, `/tasks/{id}` |
| Assignments | POST/DELETE | `/task-assignments`, `/task-assignments/{id}` |
| Comments | GET/POST/DELETE | `/comments/task/{taskId}`, `/comments/{id}` |
| Attachments | POST/DELETE | `/task-attachments`, `/task-attachments/{id}` |
| Notifications | GET | `/notifications` |
| Notifications | PUT | `/notifications/read/{id}` |
| Notifications | PUT | `/notifications/read-all` |

---

## Role-based Permissions

| Action | Admin | Member |
|---|---|---|
| Create / Delete project | ✅ | ❌ |
| Create task list | ✅ | ✅ |
| Delete task list | ✅ | ❌ |
| Create / Edit task | ✅ | ✅ |
| Delete task | ✅ | ❌ |
| Assign member to task | ✅ | ✅ |
| Add attachment | ✅ | ❌ |
| Comment | ✅ | ✅ |
| Add / Remove team member | ✅ | ❌ |

---

## Docker Architecture

```
┌──────────────────────────────────────┐
│           taskei-network             │
│                                      │
│  ┌──────────┐   ┌────────────────┐   │
│  │ frontend │──▶│    backend     │   │
│  │  :80     │   │    :8080       │   │
│  │ (Nginx)  │   │ (Spring Boot)  │   │
│  └──────────┘   └───────┬────────┘   │
│                         │            │
│               ┌─────────▼────────┐   │
│               │      mysql       │   │
│               │      :3306       │   │
│               └──────────────────┘   │
└──────────────────────────────────────┘
```

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | Database host |
| `DB_PORT` | `3307` | Database port |
| `DBMS_USERNAME` | `root` | Database username |
| `DBMS_PASSWORD` | `123456` | Database password |
| `JWT_SIGNER_KEY` | — | JWT secret key (min 256 bits) |
| `VITE_API_URL` | `http://localhost:8080` | Backend URL (frontend) |

---

## License

MIT License
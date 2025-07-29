# SmartHR Backend

A comprehensive **Employee Management System backend** built with **Java**, **Spring Boot**, and **MongoDB**, designed to streamline HR operations such as attendance, leave management, real-time chat, meetings, and task scheduling. This backend exposes secure RESTful APIs and WebSocket endpoints to support a feature-rich HRMS frontend.

---

## 🚀 Core Features

- **User Authentication**
  - Google OAuth 2.0
  - Email/Password login
  - JWT-based secured endpoints

- **Real-Time Chat System**
  - WebSocket + STOMP-based 1-to-1 messaging
  - Encrypted messages (supports text and images)

- **Attendance Management**
  - Geofencing-based check-in/out
  - Role-based view for HR and employee
  - Daily, date-wise, and user-wise queries

- **Leave Management**
  - Apply, approve, reject, and cancel leave
  - Tracks leave status and history
  - Admin and employee-level access

- **Meeting Scheduler**
  - Create meetings with participant lists
  - Google Calendar API integration
  - Automatic scheduling and syncing

- **Task Management**
  - Create, assign, update tasks
  - Upload attachments with tasks
  - HR can manage all employees' tasks

- **Role-Based Access**
  - Admin, HR, and Employee roles
  - Controlled API access by user type

- **File Storage**
  - Supabase integration for media storage
  - Securely stores chat images and task files

---

## 🧰 Tech Stack

| Layer         | Technology                             |
|--------------|-----------------------------------------|
| Language      | Java                                    |
| Framework     | Spring Boot                             |
| Database      | MongoDB                                 |
| Messaging     | WebSocket + STOMP                       |
| Auth & Secure | JWT, Google OAuth 2.0, Spring Security  |
| File Storage  | Supabase                                |
| Calendar Sync | Google Calendar API                     |
| API Client    | Postman Collection (REST & WebSocket)   |

---

## 🔐 Security & Auth

- JWT-based auth for all secured routes
- OAuth 2.0 via Google for seamless login
- Passwords hashed securely (backend-handled)
- Role-based API access: Admin | HR | Employee

---

## 📦 Integrations

- **Google OAuth 2.0** — Auth login
- **Google Calendar API** — Meeting creation & syncing
- **Supabase** — File storage for images, attachments
- **MongoDB Atlas** — Cloud-hosted NoSQL database

---

## 🏗️ Architecture Notes

- Modular Spring Boot project structure
- DTOs for clean request/response contracts
- Services & Repositories follow separation of concerns
- WebSocket config is centralized and secured

---

## 👨‍💻 Contributing

If you’d like to contribute, fix bugs, or expand this project, feel free to open issues or pull requests!

---

## 📎 Repository

[GitHub Repo](https://github.com/HR-Task-Management-App/SmartHR-Backend)

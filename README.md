# Academic Database Management System	

### 👥 Team Members
This project was developed as part of a team effort for [ENGG1420's Final Project].

- **Nikita Upadhyay** – Frontend Developer (UIUX - Scenebuilder) + Backend Assistance (Events Logic)
- **Hamza Khan** – Backend Developer (Subject, Faculty and Courses Logic)
- **Cherith Boya** – Backend Developer (User, Admin and Faculty Authentication, Student Logic)
- **Mark Touzo** – QA & Testing, Documentation (User, Admin and Faculty Authentication, Student Logic)

# Mini Project 2: cryptoMagic Encryption/Decryption Tool 
### Author: Nikita Upadhyay

## 📚 Summary  
The Academic Database Management System is a JavaFX desktop application built to support university operations through role-based access for Admins, Faculty, and Students. It enables administrators to manage user records and academic events, while faculty and students can view, register for, and interact with courses and events. The system uses file I/O for data storage, ensuring persistence without a database. Developed as a team project, it demonstrates practical skills in GUI design, object-oriented programming, and user-focused functionality.

## 🚀 Features  

### 🔐 Role-Based Access
- **Admin**
  - Manage all modules (students, faculty, courses, subjects, events)
  - Full create/edit/delete access
- **Faculty**
  - View and register for teaching courses and subjects
  - Track enrolled students
- **Student**
  - Browse and register for courses, subjects, and events
  - View personal academic records

---

## 📁 Module Features

<details>
<summary>📅 <strong>Event Management</strong></summary>

- Create, edit, and delete academic events
- Scrollable and searchable event list view
- View event capacity and registered participants
- Students can register for upcoming events
</details>

<details>
<summary>📘 <strong>Subject Management</strong></summary>

- Admins can add and assign subjects to faculty
- Students can view and register for available subjects
- Each subject linked with unique codes and course associations
</details>

<details>
<summary>📚 <strong>Course Management</strong></summary>

- Add, view, and manage academic courses
- Display course capacity, registration count, and assigned faculty
- Faculty can register to teach courses
- Students can browse and enroll in courses
</details>

<details>
<summary>👨‍🎓 <strong>Student Management</strong></summary>

- Admins can create, edit, and delete student records
- Stores student ID, name, program, and enrollments
- Students can view personal registrations and academic info
</details>

<details>
<summary>👩‍🏫 <strong>Faculty Management</strong></summary>

- Manage faculty profiles with code, name, and contact info
- Assign courses and subjects to faculty
- Faculty can view assigned teaching load and responsibilities
</details>

---

## 💾 File-Based Storage

- Each module uses structured `.txt` files for persistent storage
- Separate files for students, faculty, courses, events, and subjects
- Lightweight and database-free for simple deployment

## 🖥️ JavaFX User Interface

- GUI built with JavaFX and FXML
- Clean layout with role-specific dashboards
- Modular, scalable controller architecture

##[Read our Report!](https://example.com)

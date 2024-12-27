# ToDo List Application

## **Project Overview**
This project is a simple ToDo List application built using Java. It features a **backend REST API** powered by Spring Boot and a **frontend graphical user interface (GUI)** built with JavaFX. The application allows users to manage their tasks by performing CRUD operations (Create, Read, Update, and Delete) through a user-friendly interface.

## **Features**
- **Backend API:**
    - **GET /tasks:** Retrieve all tasks.
    - **POST /tasks:** Create a new task.
    - **PUT /tasks/{id}:** Update a specific task by ID.
    - **DELETE /tasks/{id}:** Delete a specific task by ID.
- **Frontend:**
    - View all tasks in a list.
    - Add new tasks.
    - Delete tasks.
    - Automatically syncs with the backend for all operations.

## **Technologies Used**
- **Backend:**
    - Spring Boot
    - JPA (Java Persistence API) with Hibernate
    - H2 Database (In-memory)
- **Frontend:**
    - JavaFX
    - HTTP Client
    - Jackson (for JSON serialization/deserialization)

---

## **Setup Instructions**

### **1. Clone the Repository**
```bash
git clone <https://github.com/Natti-y/Avancerad-java-Natasha-Axelsen-slutprojekt>
cd ToDoList
```

### **2. Backend Setup**
1. Ensure you have **Java 17+** and **Maven** installed.
2. Navigate to the backend project folder (if separated).
3. Build the backend:
   ```bash
   mvn clean install
   ```
4. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
5. The backend API will start on `http://localhost:8080`.

### **3. Frontend Setup**
1. Open the project in an IDE that supports JavaFX, such as **IntelliJ IDEA**.
2. Ensure JavaFX libraries are correctly configured in your IDE.
3. Run the `ToDoApp` class located in `com.example.ToDoList.view`.

### **4. Test the Application**
- Once both the backend and frontend are running, you can interact with the application via the GUI.
- The frontend communicates with the backend through REST API calls.

---

## **API Endpoints**

### **1. GET /tasks**
Retrieve all tasks.
- **Example Request:**
  ```bash
  curl -X GET http://localhost:8080/tasks
  ```
- **Example Response:**
  ```json
  [
    {
      "id": 1,
      "title": "Buy milk",
      "completed": false
    },
    {
      "id": 2,
      "title": "Read a book",
      "completed": true
    }
  ]
  ```

### **2. POST /tasks**
Create a new task.
- **Example Request:**
  ```bash
  curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d '{"title": "New Task", "completed": false}'
  ```
- **Example Response:**
  ```json
  {
    "id": 3,
    "title": "New Task",
    "completed": false
  }
  ```

### **3. PUT /tasks/{id}**
Update a specific task by ID.
- **Example Request:**
  ```bash
  curl -X PUT http://localhost:8080/tasks/1 -H "Content-Type: application/json" -d '{"title": "Updated Task", "completed": true}'
  ```
- **Example Response:**
  ```json
  {
    "id": 1,
    "title": "Updated Task",
    "completed": true
  }
  ```

### **4. DELETE /tasks/{id}**
Delete a specific task by ID.
- **Example Request:**
  ```bash
  curl -X DELETE http://localhost:8080/tasks/1
  ```

---

## **Project Structure**

### **Backend**
- `com.example.ToDoList.model`  
  Contains the `ToDoItem` entity class for task representation.
- `com.example.ToDoList.repo`  
  Contains the `ToDoRepo` interface for database operations.
- `com.example.ToDoList.controller`  
  Contains the `ToDoController` class to handle API requests.

### **Frontend**
- `com.example.ToDoList.view`  
  Contains the `ToDoApp` class for the JavaFX-based GUI.

---

## **Design Decisions**
1. **Spring Boot for Backend:** Chosen for its simplicity in setting up REST APIs and managing dependencies.
2. **JavaFX for Frontend:** Provides a native GUI application experience and integrates well with Java's ecosystem.
3. **H2 Database:** An in-memory database was used for simplicity during development.

---

## **Future Improvements**
- Add **authentication** to secure the backend API.
- Improve the GUI by allowing users to mark tasks as completed or edit tasks directly.
- Implement more robust error handling and user feedback in the frontend.
- Replace the in-memory H2 database with a persistent database (e.g., PostgreSQL or MySQL).

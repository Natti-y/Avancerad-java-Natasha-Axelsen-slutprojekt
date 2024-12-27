package com.example.ToDoList.repo;

import com.example.ToDoList.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepo extends JpaRepository<ToDoItem, Long> {
}

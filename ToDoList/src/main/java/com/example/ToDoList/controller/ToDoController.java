package com.example.ToDoList.controller;

import com.example.ToDoList.model.ToDoItem;
import com.example.ToDoList.repo.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tasks")
@Validated
public class ToDoController {

    @Autowired
    private ToDoRepo toDoRepo;

    // GET /tasks - Retrieve all tasks
    @GetMapping
    public List<ToDoItem> findAll() {
        return toDoRepo.findAll();
    }

    // POST /tasks - Create a new task
    @PostMapping
    public ToDoItem save(@RequestBody ToDoItem toDoItem) {
        return toDoRepo.save(toDoItem);
    }

    // PUT /tasks/{id} - Update a specific task by ID
    @PutMapping("/{id}")
    public ToDoItem update(@PathVariable Long id, @RequestBody ToDoItem toDoItem) {
        return toDoRepo.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(toDoItem.getTitle());
                    existingTask.setCompleted(toDoItem.isCompleted());
                    return toDoRepo.save(existingTask);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    // DELETE /tasks/{id} - Delete a specific task by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (toDoRepo.existsById(id)) {
            toDoRepo.deleteById(id);
        } else {
            throw new RuntimeException("Task not found with ID: " + id);
        }
    }
}

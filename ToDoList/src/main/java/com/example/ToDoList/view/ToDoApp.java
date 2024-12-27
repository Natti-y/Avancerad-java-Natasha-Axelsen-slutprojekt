package com.example.ToDoList.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ToDoApp extends Application {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObservableList<String> tasks = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        ListView<String> todoList = new ListView<>(tasks);
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task");

        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> {
            String newTaskTitle = taskInput.getText().trim();
            if (!newTaskTitle.isEmpty()) {
                createTask(newTaskTitle); // Send POST request to add a task
                taskInput.clear();
            }
        });

        Button deleteButton = new Button("Delete Task");
        deleteButton.setOnAction(e -> {
            String selectedTask = todoList.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                deleteTask(selectedTask); // Send DELETE request to remove the task
            }
        });

        HBox inputLayout = new HBox(10, taskInput, addButton);
        VBox layout = new VBox(10, todoList, inputLayout, deleteButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("ToDo List");
        stage.setScene(scene);
        stage.show();

        // Load tasks from the backend on startup
        fetchTasks();
    }

    private void fetchTasks() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseTasks)
                .thenAccept(fetchedTasks -> {
                    tasks.clear();
                    tasks.addAll(fetchedTasks);
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private List<String> parseTasks(String responseBody) {
        try {
            List<ToDoItem> items = objectMapper.readValue(responseBody, new TypeReference<List<ToDoItem>>() {});
            return items.stream().map(ToDoItem::getTitle).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void createTask(String title) {
        ToDoItem newItem = new ToDoItem(null, title, false);
        try {
            String json = objectMapper.writeValueAsString(newItem);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> fetchTasks()) // Refresh tasks after adding
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(String title) {
        // Fetch the corresponding ID by title from the backend
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        httpClient.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseTasksToItems)
                .thenAccept(items -> {
                    items.stream()
                            .filter(item -> item.getTitle().equals(title))
                            .findFirst()
                            .ifPresent(item -> {
                                // Perform DELETE request
                                HttpRequest deleteRequest = HttpRequest.newBuilder()
                                        .uri(URI.create("http://localhost:8080/tasks/" + item.getId()))
                                        .DELETE()
                                        .build();

                                httpClient.sendAsync(deleteRequest, HttpResponse.BodyHandlers.ofString())
                                        .thenAccept(response -> fetchTasks()) // Refresh tasks after deleting
                                        .exceptionally(e -> {
                                            e.printStackTrace();
                                            return null;
                                        });
                            });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private List<ToDoItem> parseTasksToItems(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<List<ToDoItem>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Nested class to match the ToDoItem structure from the backend
    public static class ToDoItem {
        private Long id;
        private String title;
        private boolean completed;

        public ToDoItem() {}

        public ToDoItem(Long id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}

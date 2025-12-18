package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User getById(Long id);
    List<User> getAll();
    User update(Long id, User user);
    void delete(Long id);
}
package com.projects.user.service.services;

import com.projects.user.service.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User createUser(User user);

    public User updateUser(User user);

    public User getUserById(String userId);

    public List<User> getAllUsers();
}

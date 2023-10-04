package com.techtrove.userservice.service;

import com.techtrove.userservice.model.User;

public interface UserService {
    User registerUser(User user);

    User loginUser(String username, String password);

    User getUserById(String id);
}

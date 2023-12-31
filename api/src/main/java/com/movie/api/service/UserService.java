package com.movie.api.service;

import com.movie.api.model.dto.CxLoginDto;
import com.movie.api.model.entity.User;

import java.util.List;

public interface UserService {

    User login(CxLoginDto dto) throws Exception;

    List<User> findAll();

    User findById(String id);

    User update(User user);

    User save(User user) throws Exception;

    User findByUsername(String username);

    void deleteById(String id);

}

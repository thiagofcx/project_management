package com.thiago.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    PagedResult<User> findAll(int page, int size);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByEmailExceptId(String email, Long id);
    Optional<User> findByEmail(String email);
}

package com.thiago.projectmanagement.infrastructure.repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;
import com.thiago.projectmanagement.infrastructure.mapper.UserMapper;
import com.thiago.projectmanagement.infrastructure.persistence.entity.UserJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.repository.SpringDataUserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository springDataRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(
            SpringDataUserRepository springDataRepository,
            UserMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<User> findAll(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var springPage = springDataRepository.findAll(pageable);
        List<User> content = springPage.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return PagedResult.of(content, springPage.getTotalElements(), springPage.getNumber(), springPage.getSize());
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailExceptId(String email, Long id) {
        return springDataRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email).map(mapper::toDomain);
    }
}

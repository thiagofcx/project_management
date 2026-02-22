package com.thiago.projectmanagement.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.infrastructure.persistence.entity.UserJpaEntity;

@Component
public class UserMapper {

    public UserJpaEntity toEntity(User domain) {
        if (domain == null) return null;
        
        return new UserJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getRole(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
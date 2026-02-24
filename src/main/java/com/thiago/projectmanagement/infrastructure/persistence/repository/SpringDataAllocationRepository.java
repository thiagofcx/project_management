package com.thiago.projectmanagement.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.infrastructure.persistence.entity.AllocationJpaEntity;

public interface SpringDataAllocationRepository extends JpaRepository<AllocationJpaEntity, Long>, JpaSpecificationExecutor<AllocationJpaEntity> {
    List<AllocationJpaEntity> findByProject_Id(Long projectId);
    List<AllocationJpaEntity> findByResource_Id(Long resourceId);
    boolean existsByProject_IdAndResource_Id(Long projectId, Long resourceId);
    boolean existsByProject_IdAndResource_IdAndIdNot(Long projectId, Long resourceId, Long id);
    boolean existsByResource_Id(Long resourceId);
    boolean existsByResource_IdAndIdNot(Long resourceId, Long id);

    @Query("select count(a) > 0 from AllocationJpaEntity a where a.resource.id = :resourceId "
            + "and a.project.status <> :completedStatus")
    boolean existsByResource_IdAndProject_StatusNot(
            @Param("resourceId") Long resourceId,
            @Param("completedStatus") ProjectStatus completedStatus);

    @Query("select count(a) > 0 from AllocationJpaEntity a where a.resource.id = :resourceId and a.id <> :exclusionId "
            + "and a.project.status <> :completedStatus")
    boolean existsByResource_IdAndProject_StatusNotAndIdNot(
            @Param("resourceId") Long resourceId,
            @Param("exclusionId") Long exclusionId,
            @Param("completedStatus") ProjectStatus completedStatus);

    @Query("select count(distinct a.resource.id) from AllocationJpaEntity a where a.project.status <> :completedStatus")
    long countDistinctResourceIdsByProjectStatusNot(@Param("completedStatus") ProjectStatus completedStatus);
}

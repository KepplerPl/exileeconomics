package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.NextIdEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface NextIdRepository extends CrudRepository<NextIdEntity, Long> {
    @Query(
            value = "SELECT * FROM next_id order by created_at DESC LIMIT 1",
            nativeQuery = true)
    Collection<NextIdEntity> mostCurrentNextId();
}

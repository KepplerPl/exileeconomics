package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.NextId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface NextIdRepository extends CrudRepository<NextId, Long> {
    @Query(
            value = "SELECT * FROM next_id order by created_at DESC LIMIT 1",
            nativeQuery = true)
    Collection<NextId> mostCurrentNextId();
}

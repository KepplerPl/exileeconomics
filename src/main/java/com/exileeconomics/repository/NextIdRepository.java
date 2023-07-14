package com.exileeconomics.repository;

import com.exileeconomics.entity.NextIdEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NextIdRepository extends CrudRepository<NextIdEntity, Long> {
    @Query(
            value = "SELECT * FROM next_id order by created_at DESC LIMIT 1",
            nativeQuery = true)
    Collection<NextIdEntity> mostCurrentNextId();
}

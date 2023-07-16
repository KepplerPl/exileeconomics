package com.exileeconomics.repository;

import com.exileeconomics.entity.NextIdEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NextIdRepository extends CrudRepository<NextIdEntity, Long> {
    NextIdEntity findFirstByOrderByCreatedAtDesc();
}

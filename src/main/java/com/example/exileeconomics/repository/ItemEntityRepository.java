package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemEntityRepository extends CrudRepository<ItemEntity, Long> {

}

package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemEntityRepository extends CrudRepository<Item, Long> {

}

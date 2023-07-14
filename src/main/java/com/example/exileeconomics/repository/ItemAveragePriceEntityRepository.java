package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemAveragePriceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAveragePriceEntityRepository extends CrudRepository<ItemAveragePriceEntity, Long> {
}

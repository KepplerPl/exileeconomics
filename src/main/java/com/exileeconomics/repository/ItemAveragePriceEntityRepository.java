package com.exileeconomics.repository;

import com.exileeconomics.entity.ItemAveragePriceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAveragePriceEntityRepository extends CrudRepository<ItemAveragePriceEntity, Long> {
}

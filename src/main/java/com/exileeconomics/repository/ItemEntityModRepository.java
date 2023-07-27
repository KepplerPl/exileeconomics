package com.exileeconomics.repository;

import com.exileeconomics.entity.ItemEntityMod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemEntityModRepository extends CrudRepository<ItemEntityMod, Long> {
    Optional<ItemEntityMod> findFirstByItemMod(String mod);
}

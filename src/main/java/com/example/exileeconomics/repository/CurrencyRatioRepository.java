package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.CurrencyRatio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRatioRepository extends CrudRepository<CurrencyRatio, Long> {
    @Query(
            value = "SELECT * FROM currency_ratio " +
                    "WHERE created_at " +
                    "BETWEEN timestamp(CURRENT_DATE) AND (timestamp(CURRENT_DATE) + INTERVAL 1 DAY)" +
                    "ORDER BY created_at DESC LIMIT 1;",
            nativeQuery = true)
    CurrencyRatio mostCurrentCurrencyRatio();
}

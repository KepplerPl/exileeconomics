package com.exileeconomics.repository;

import com.exileeconomics.entity.ItemAveragePriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ItemAveragePriceEntityRepository extends CrudRepository<ItemAveragePriceEntity, Long> {

    @Query(
            value = "SELECT " +
                    "iap.* " +
                    "FROM item_average_price iap " +
                    "JOIN currency_ratio cr ON iap.currency_ratio_id = cr.id " +
                    "JOIN item_definition ide ON iap.item_id = ide.id " +
                    "WHERE iap.quantity_lower_limit = :quantity_lower_limit AND iap.quantity_upper_limit = :quantity_upper_limit " +
                    "AND ide.name = :item_name " +
                    "AND cr.id IN ( " +
                    "SELECT cr.id FROM currency_ratio cr JOIN item_definition ide " +
                    "ON cr.item_definition_entity_id = ide.id " +
                    "WHERE ide.name = :currency_name " +
                    ")" +
                    "ORDER BY iap.created_at DESC",
            nativeQuery = true
    )
    Collection<ItemAveragePriceEntity> getPriceForItemsInBetweenQuantities(
            @Param("item_name") String itemName,
            @Param("currency_name") String currency_name,
            @Param("quantity_lower_limit") int quantityLowerLimit,
            @Param("quantity_upper_limit") int quantityUpperLimit
    );
}

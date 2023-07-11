SELECT FLOOR(AVG(tbl.price)) as price FROM 
(SELECT it.price
FROM item it LEFT JOIN currency_ratio cr ON it.currency_ratio_id = cr.id
WHERE it.item_id = @soldItem AND cr.item_definition_entity_id = @sodlFor
  AND it.sold_quantity < it.total_quantity
  AND it.total_quantity < 10
  AND it.created_at BETWEEN :lower_time_limit AND :upperTimeLimit
ORDER BY price LIMIT 100 OFFSET 10) AS tbl;



SET @soldItem = (SELECT id FROM item_definition WHERE `name` = 'divine orb');
SET @sodlFor = (SELECT id FROM item_definition WHERE `name` = 'chaos orb');

SELECT it.*, cr.chaos
FROM item it LEFT JOIN currency_ratio cr ON it.currency_ratio_id = cr.id
WHERE it.item_id = @soldItem AND cr.item_definition_entity_id = @sodlFor
  AND it.sold_quantity < it.total_quantity
  AND it.total_quantity < 10
  AND it.created_at BETWEEN timestamp(UTC_TIMESTAMP()) AND timestamp(UTC_TIMESTAMP() + INTERVAL 24 HOUR)
ORDER BY price ASC LIMIT 2000 OFFSET 10;


SELECT FLOOR(AVG(tbl.price)) as price FROM
(SELECT it.price FROM item it LEFT JOIN currency_ratio cr ON it.currency_ratio_id = cr.id
                 WHERE it.item_id = ? AND cr.item_definition_entity_id = ?
                   AND it.sold_quantity < it.total_quantity
                   AND it.total_quantity < 10   AND it.created_at
                       BETWEEN ? AND ? ORDER BY price LIMIT ? OFFSET ?) AS tbl;

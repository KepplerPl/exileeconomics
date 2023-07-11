DROP PROCEDURE IF EXISTS getDivineToChaosRation;

DELIMITER $
CREATE PROCEDURE getDivineToChaosRation(qOffset INT, qLimit INT)
BEGIN

    SET @_qOffset =qOffset ;
    SET @_qLimit = qLimit;
    PREPARE stmt FROM "SELECT FLOOR(AVG(tbl.price)) as price FROM
(
SELECT price
FROM item WHERE item_definition_entity_id = 277
AND quantity < 20
AND created_at BETWEEN timestamp(CURRENT_DATE()) AND timestamp(CURRENT_DATE() + INTERVAL 12 HOUR)
ORDER BY price ASC
LIMIT ? OFFSET ?
) AS tbl;";
    EXECUTE stmt USING @_qOffset, @_qLimit;
    DEALLOCATE PREPARE stmt;
END$
DELIMITER ;

SET @total =(SELECT count(price) from item where item_definition_entity_id = 277 and quantity < 20
                                             AND created_at BETWEEN timestamp(CURRENT_DATE()) AND timestamp(CURRENT_DATE() + INTERVAL 12 HOUR)) ;

SET @qLimit = (@total - FLOOR(@total * 0.2));
SET @qOffset = FLOOR(@total * 0.01);

CALL getDivineToChaosRation(@qOffset, @qLimit);


276 - divine orb
290 - chaos orb
96 - awakened sextant
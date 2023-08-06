package com.exileeconomics.definitions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ItemDefinitionEnumTest {
    @Test
    public void testNumberOfValuesIs385() {
        // this is not a redundant test, if one enum gets accidentally deleted this test will fail
        assertEquals(674, ItemDefinitionEnum.values().length);
    }
    @Test
    public void testThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ItemDefinitionEnum.fromString("wrong name"));

        assertTrue(exception.getMessage().contains("Unrecognized enum value, got wrong name"));
    }
}

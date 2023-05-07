package de.markus.meier.coolschrank.util;

import de.markus.meier.coolschrank.model.dto.ShoppingInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingListDto;
import de.markus.meier.coolschrank.model.model.ShoppingInventoryEntity;
import de.markus.meier.coolschrank.model.model.ShoppingListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ShoppingListMapper.class})
public class ShoppingListMapperTest {
    @Autowired
    @InjectMocks
    private ShoppingListMapper shoppingListMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToShoppingListDto() {
        List<ShoppingInventoryEntity>shoppingInventoryEntityList = new ArrayList<>();
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,"Cola",0.5f);
        shoppingInventoryEntityList.add(shoppingInventoryEntity);
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L,"XXX", shoppingInventoryEntityList);
        ShoppingListDto result = shoppingListMapper.toShoppingListDto(shoppingListEntity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(shoppingListEntity.getId());
        assertThat(result.getFridgeId()).isEqualTo(shoppingListEntity.getFridgeId());
        assertThat(result.getShoppingInventoryDtoList().size()).isEqualTo(1);
        assertThat(result.getShoppingInventoryDtoList().get(0).getFridgeInventoryId())
                .isEqualTo(shoppingListEntity.getShoppingInventoryEntityList().get(0).getFridgeInventoryId());
        assertThat(result.getShoppingInventoryDtoList().get(0).getId())
                .isEqualTo(shoppingListEntity.getShoppingInventoryEntityList().get(0).getId());
        assertThat(result.getShoppingInventoryDtoList().get(0).getName())
                .isEqualTo(shoppingListEntity.getShoppingInventoryEntityList().get(0).getName());
        assertThat(result.getShoppingInventoryDtoList().get(0).getAmount())
                .isEqualTo(shoppingListEntity.getShoppingInventoryEntityList().get(0).getAmount());
    }

    @Test
    void testToShoppingInventoryDto() {
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L, 1L, "Cola", 0.5f);
        ShoppingInventoryDto result = shoppingListMapper.toShoppingInventoryDto(shoppingInventoryEntity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(shoppingInventoryEntity.getId());
        assertThat(result.getFridgeInventoryId()).isEqualTo(shoppingInventoryEntity.getFridgeInventoryId());
        assertThat(result.getName()).isEqualTo(shoppingInventoryEntity.getName());
        assertThat(result.getAmount()).isEqualTo(shoppingInventoryEntity.getAmount());
    }
}

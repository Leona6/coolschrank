package de.markus.meier.coolschrank.util;

import de.markus.meier.coolschrank.model.dto.ShoppingInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingListDto;
import de.markus.meier.coolschrank.model.model.ShoppingInventoryEntity;
import de.markus.meier.coolschrank.model.model.ShoppingListEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for mapping between ShoppingListEntity and ShoppingListDto.
 */
@Service
public class ShoppingListMapper {
    /**
     * Maps a ShoppingListEntity object to a ShoppingListDto object.
     *
     * @param shoppingListEntity The ShoppingListEntity object to be mapped.
     * @return The mapped ShoppingListDto object.
     */
    public ShoppingListDto toShoppingListDto(ShoppingListEntity shoppingListEntity) {
        List<ShoppingInventoryDto> shoppingListAPIDtoList = new ArrayList<>();
        for (ShoppingInventoryEntity shoppingInventoryEntity : shoppingListEntity.getShoppingInventoryEntityList()) {
            shoppingListAPIDtoList.add(new ShoppingInventoryDto(
                    shoppingInventoryEntity.getId(),
                    shoppingInventoryEntity.getFridgeInventoryId(),
                    shoppingInventoryEntity.getName(),
                    shoppingInventoryEntity.getAmount()
            ));
        }
        return new ShoppingListDto(
                shoppingListEntity.getId(),
                shoppingListEntity.getFridgeId(),
                shoppingListAPIDtoList
        );
    }

    /**
     * Maps a ShoppingInventoryEntity object to a ShoppingInventoryDto object.
     *
     * @param shoppingInventoryEntity The ShoppingInventoryEntity object to be mapped.
     * @return The mapped ShoppingInventoryDto object.
     */
    public ShoppingInventoryDto toShoppingInventoryDto(ShoppingInventoryEntity shoppingInventoryEntity) {
        return new ShoppingInventoryDto(
                shoppingInventoryEntity.getId(),
                shoppingInventoryEntity.getFridgeInventoryId(),
                shoppingInventoryEntity.getName(),
                shoppingInventoryEntity.getAmount()
        );
    }

}

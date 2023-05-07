package de.markus.meier.coolschrank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.markus.meier.coolschrank.model.dto.FridgeDto;
import de.markus.meier.coolschrank.model.dto.FridgeInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingListDto;
import de.markus.meier.coolschrank.model.model.ShoppingInventoryEntity;
import de.markus.meier.coolschrank.model.model.ShoppingListEntity;
import de.markus.meier.coolschrank.repository.ShoppingListRepository;
import de.markus.meier.coolschrank.util.ShoppingListMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * The ShoppingListService class provides methods for managing shopping lists.
 */
@Service
public class ShoppingListService {

    private WebClient webClient;

    private final ShoppingListRepository shoppingListRepository;

    private final ShoppingListMapper shoppingListMapper;

    /**
     * Constructs a new ShoppingListService with the specified repositories and mappers.
     *
     * @param shoppingListRepository The repository for shopping lists.
     * @param shoppingListMapper The mapper for shopping list entities and DTOs.
     */
    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, ShoppingListMapper  shoppingListMapper) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListMapper = shoppingListMapper;
    }

    @PostConstruct
    private void setup() {
        webClient = WebClient.builder().build();
    }

    /**
     * Creates a new shopping list based on the given ID.
     *
     * @param id The ID of the shopping list.
     * @return The created shopping list DTO, or null if the shopping list already exists or the fridge API response is empty.
     * @throws JsonProcessingException if there is an error processing the JSON response.
     */
    public ShoppingListDto createShoppingList(String id) throws JsonProcessingException {
        if (shoppingListExist(id)) {
            return null;
        }
        String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge/" + id;
        String response = this.webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        FridgeDto fridgeAPIDto = objectMapper.readValue(response, FridgeDto.class);
        if (fridgeAPIDto.getInventory().size() == 0) {
            return null;
        }

        List<ShoppingInventoryEntity> shoppingInventoryAPIEntityList = new ArrayList<>();
        for (FridgeInventoryDto fridgeInvetoryAPIDto : fridgeAPIDto.getInventory()) {
            if (fridgeInvetoryAPIDto.getTarget() - fridgeInvetoryAPIDto.getActual() > 0) {
                shoppingInventoryAPIEntityList.add(new ShoppingInventoryEntity(
                        fridgeInvetoryAPIDto.getId(),
                        fridgeInvetoryAPIDto.getName(),
                        fridgeInvetoryAPIDto.getTarget() - fridgeInvetoryAPIDto.getActual()
                ));
            }
        }
        ShoppingListEntity shoppingListAPIEntity = new ShoppingListEntity(1L, id, shoppingInventoryAPIEntityList);
        return shoppingListMapper.toShoppingListDto(shoppingListRepository.save(shoppingListAPIEntity));
    }

    /**
     * Checks if a shopping list with the given ID already exists.
     *
     * @param id The ID of the shopping list.
     * @return true if the shopping list exists, false otherwise.
     */
    private boolean shoppingListExist(String id) {
        for (ShoppingListEntity shoppingListAPIEntity : shoppingListRepository.findAll()) {
            if (shoppingListAPIEntity.getFridgeId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the shopping list by automatically retrieving data from the fridge API.
     *
     * @param id The ID of the shopping list.
     * @param fridgeId The ID of the fridge.
     * @return The updated shopping list DTO, or null if the shopping list doesn't exist or the fridge API response is empty.
     * @throws JsonProcessingException if there is an error processing the JSON response.
     */
    public ShoppingListDto autoUpdateShoppingList(Long id, String fridgeId) throws JsonProcessingException {
        ShoppingListEntity shoppingListEntity = shoppingListRepository.findById(id).orElse(null);
        if (shoppingListEntity == null) {
            return null;
        }
        String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge/" + fridgeId;
        String response = this.webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        FridgeDto fridgeAPIDto = objectMapper.readValue(response, FridgeDto.class);
        if (fridgeAPIDto.getInventory().size() == 0) {
            return null;
        }
        float sum;
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = checkAndCompleteList(shoppingListEntity.getShoppingInventoryEntityList(), fridgeAPIDto.getInventory());
        for (ShoppingInventoryEntity shoppingInventoryAPIEntity : shoppingInventoryEntityList) {
            for (FridgeInventoryDto fridgeInvetoryAPIDto : fridgeAPIDto.getInventory()) {
                if (shoppingInventoryAPIEntity.getFridgeInventoryId() == fridgeInvetoryAPIDto.getId()) {
                    sum = fridgeInvetoryAPIDto.getTarget() - fridgeInvetoryAPIDto.getActual();
                    if (sum < 0) {
                        shoppingInventoryAPIEntity.setAmount(0);
                    }
                    shoppingInventoryAPIEntity.setAmount(sum);
                }
            }
        }
        shoppingListEntity.setShoppingInventoryEntityList(shoppingInventoryEntityList);
        shoppingListRepository.save(shoppingListEntity);
        return shoppingListMapper.toShoppingListDto(shoppingListEntity);

    }

    /**
     * Checks and completes the shopping inventory list based on the provided inventory.
     *
     * @param shoppingInventoryEntityList The original shopping inventory list.
     * @param inventory                  The inventory list.
     * @return The updated shopping inventory list.
     */
    public List<ShoppingInventoryEntity> checkAndCompleteList(List<ShoppingInventoryEntity> shoppingInventoryEntityList, List<FridgeInventoryDto> inventory) {
        if (shoppingInventoryEntityList.size() == inventory.size()) {
            return shoppingInventoryEntityList;
        }
        boolean exist = false;
        for (FridgeInventoryDto fridgeInvetoryAPIDto : inventory) {
            for (ShoppingInventoryEntity shoppingInventoryAPIEntity : shoppingInventoryEntityList) {
                if (shoppingInventoryAPIEntity.getFridgeInventoryId() == fridgeInvetoryAPIDto.getId()) {
                    exist = true;
                }
            }
            if (!exist) {
                float sum = fridgeInvetoryAPIDto.getTarget() - fridgeInvetoryAPIDto.getActual();
                if (sum > 0) {
                    shoppingInventoryEntityList.add(new ShoppingInventoryEntity(
                            fridgeInvetoryAPIDto.getId(),
                            fridgeInvetoryAPIDto.getName(),
                            sum
                    ));
                }
            }
            exist = false;
        }
        return shoppingInventoryEntityList;
    }

    /**
     * Deletes a shopping inventory item from the shopping list.
     *
     * @param id     The ID of the shopping list.
     * @param itemId The ID of the shopping inventory item.
     * @return The deleted shopping inventory item.
     */
    public ShoppingInventoryDto deleteShoppingInventory(Long id, Long itemId) {
        if (shoppingListRepository.existsById(id)) {
            ShoppingListEntity shoppingListAPIEntity = shoppingListRepository.findById(id).get();
            for (ShoppingInventoryEntity shoppingInventoryAPIEntity : shoppingListAPIEntity.getShoppingInventoryEntityList()) {
                System.out.println(shoppingInventoryAPIEntity.getAmount()+" < "+itemId);
                if (shoppingInventoryAPIEntity.getId() == itemId) {
                    shoppingListAPIEntity.getShoppingInventoryEntityList().remove(shoppingInventoryAPIEntity);
                    shoppingListRepository.save(shoppingListAPIEntity);
                    return shoppingListMapper.toShoppingInventoryDto(shoppingInventoryAPIEntity);
                }
            }
        }
        return null;
    }

    /**
     * Deletes a shopping list.
     *
     * @param id The ID of the shopping list.
     * @return The deleted shopping list.
     */
    public ShoppingListDto deleteShoppingList(Long id) {
        if (shoppingListRepository.existsById(id)) {
            ShoppingListEntity shoppingListAPIEntity = shoppingListRepository.findById(id).get();
            shoppingListRepository.delete(shoppingListAPIEntity);
            return shoppingListMapper.toShoppingListDto(shoppingListAPIEntity);
        }
        return null;
    }

    /**
     * Updates a shopping list with the provided shopping inventory entity.
     *
     * @param id                       The ID of the shopping list.
     * @param shoppingInventoryAPIEntity The shopping inventory entity.
     * @return The updated shopping inventory item.
     */
    public ShoppingInventoryDto updateShoppingList(Long id, ShoppingInventoryEntity shoppingInventoryAPIEntity) {
        if (shoppingListRepository.existsById(id)) {
            ShoppingListEntity shoppingListAPIEntity = shoppingListRepository.findById(id).get();
            for (ShoppingInventoryEntity result : shoppingListAPIEntity.getShoppingInventoryEntityList()) {
                if (result.getId() == shoppingInventoryAPIEntity.getId()) {
                    result.setAmount(shoppingInventoryAPIEntity.getAmount());
                    shoppingListRepository.save(shoppingListAPIEntity);
                    return shoppingListMapper.toShoppingInventoryDto(result);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a shopping list based on the specified ID.
     *
     * @param id The ID of the shopping list.
     * @return The ShoppingListDto object representing the retrieved shopping list, or null if no shopping list is found.
     */
    public ShoppingListDto getShoppingList(Long id) {
        ShoppingListEntity shoppingListAPIEntity = shoppingListRepository.findById(id).orElse(null);
        if (shoppingListAPIEntity == null) {
            return null;
        }
        return shoppingListMapper.toShoppingListDto(shoppingListAPIEntity);
    }

}

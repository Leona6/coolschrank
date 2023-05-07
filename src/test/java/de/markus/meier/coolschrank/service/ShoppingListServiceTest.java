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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {ShoppingListService.class})
public class ShoppingListServiceTest {

    @Autowired
    @InjectMocks
    private ShoppingListService shoppingListService;

    @MockBean(name = "shoppingListRepository")
    private ShoppingListRepository shoppingListRepository;

    @MockBean(name = "shoppingListMapper")
    private ShoppingListMapper shoppingListMapper;

    @MockBean
    private WebClient mockedWebClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCeateShoppingList_Create() throws IOException {

        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        ShoppingListDto shoppingListDto = new ShoppingListDto(1L, "XXX", shoppingInventoryDtoList);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(mockedWebClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        String exampleJson = "{\"id\": \"XXX\", \"inventory\": [{\"name\": \"Cola\", \"target\": 0.5, \"actual\": 0.5}]}";

        Mono<String> jsonResponse = Mono.just(exampleJson);
        Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(jsonResponse);
        Mockito.when(shoppingListMapper.toShoppingListDto(any())).thenReturn(shoppingListDto);
        Mockito.verify(shoppingListRepository, Mockito.never()).save(shoppingListEntity);

        ShoppingListDto result = shoppingListService.createShoppingList("XXX");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFridgeId()).isEqualTo("XXX");
        assertThat(result.getShoppingInventoryDtoList().size()).isEqualTo(0);

    }

    @Test
    void testCeateShoppingList_EmptyList() throws IOException {
        String exampleJson = "{\"id\": \"XXX\", \"inventory\": [{\"name\": \"Cola\", \"target\": 0.5, \"actual\": 0.5}]}";

        Mono<String> jsonResponse = Mono.just(exampleJson);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(mockedWebClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(jsonResponse);

        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        Mockito.when(objectMapperMock.readValue(Mockito.anyString(), Mockito.eq(FridgeDto.class))).thenReturn(null);


        ShoppingListDto result = shoppingListService.createShoppingList("XXX");
        assertThat(result).isNull();

    }

    @Test
    void testAutoUpdateShoppingList_NotFound() throws JsonProcessingException {
        Mockito.when(shoppingListRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        ShoppingListDto result = shoppingListService.autoUpdateShoppingList(1L, "XXX");
        assertThat(result).isNull();
    }

    @Test
    void testAutoUpdateShoppingList_FridgeListEmpty() throws JsonProcessingException {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        Mockito.when(shoppingListRepository.findById(any())).thenReturn(Optional.ofNullable(shoppingListEntity));

        String exampleJson = "{\"id\": \"XXX\", \"inventory\": []}";

        Mono<String> jsonResponse = Mono.just(exampleJson);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(mockedWebClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(jsonResponse);


        ShoppingListDto result = shoppingListService.autoUpdateShoppingList(1L, "XXX");
        assertThat(result).isNull();
    }

    @Test
    void testAutoUpdateShoppingList_Ok() throws JsonProcessingException {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        ShoppingListDto shoppingListDto = new ShoppingListDto(1L, "XXX", shoppingInventoryDtoList);

        Mockito.when(shoppingListRepository.findById(any())).thenReturn(Optional.ofNullable(shoppingListEntity));

        String exampleJson = "{\"id\": \"XXX\", \"inventory\": [{\"name\": \"Cola\", \"target\": 0.5, \"actual\": 0.5}]}";

        Mono<String> jsonResponse = Mono.just(exampleJson);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(mockedWebClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(jsonResponse);

        Mockito.when(shoppingListRepository.save(any())).thenReturn(shoppingListEntity);
        Mockito.when(shoppingListMapper.toShoppingListDto(any())).thenReturn(shoppingListDto);

        ShoppingListDto result = shoppingListService.autoUpdateShoppingList(1L, "XXX");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFridgeId()).isEqualTo("XXX");
        assertThat(result.getShoppingInventoryDtoList().size()).isEqualTo(0);
    }

    @Test
    void testCheckAndCompleteList_True() {
        List<FridgeInventoryDto> inventory = new ArrayList<>();
        inventory.add(new FridgeInventoryDto("Cola", 0.5f, 0.5f));
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        shoppingInventoryEntityList.add(new ShoppingInventoryEntity(1L, "Cola", 0.5f));
        assertThat(shoppingListService.checkAndCompleteList(shoppingInventoryEntityList, inventory).size()).isEqualTo(1);
        inventory.add(new FridgeInventoryDto("Milch", 0.5f, 0.5f));
        assertThat(shoppingListService.checkAndCompleteList(shoppingInventoryEntityList, inventory).size()).isEqualTo(1);
    }

    @Test
    void testCheckAndCompleteList_False() {
        List<FridgeInventoryDto> inventory = new ArrayList<>();
        inventory.add(new FridgeInventoryDto("Milch", 0.5f, 0.7f));
        inventory.add(new FridgeInventoryDto("Cola", 0.5f, 0.5f));
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        shoppingInventoryEntityList.add(new ShoppingInventoryEntity(1L, "Cola", 0.5f));
        assertThat(shoppingListService.checkAndCompleteList(shoppingInventoryEntityList, inventory).size()).isEqualTo(2);
    }

    @Test
    void testDeleteShoppingInventory_NotFound() {
        Mockito.when(shoppingListRepository.existsById(any())).thenReturn(false);
        ShoppingInventoryDto result = shoppingListService.deleteShoppingInventory(1L, 1L);
        assertThat(result).isNull();
    }

    @Test
    void testDeleteShoppingInventory_Found() {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,1L, "Cola", 0.5f);
        shoppingInventoryEntityList.add(shoppingInventoryEntity);
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        Mockito.when(shoppingListRepository.existsById(any())).thenReturn(true);
        Mockito.when(shoppingListRepository.findById(any())).thenReturn(Optional.of(shoppingListEntity));
        Mockito.when(shoppingListRepository.save(Mockito.any(ShoppingListEntity.class))).thenReturn(shoppingListEntity);
        Mockito.when(shoppingListMapper.toShoppingInventoryDto(Mockito.any(ShoppingInventoryEntity.class))).thenReturn(
                new ShoppingInventoryDto(1L, 1L, "Cola", 0.5f));

        ShoppingInventoryDto result = shoppingListService.deleteShoppingInventory(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFridgeInventoryId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Cola");
        assertThat(result.getAmount()).isEqualTo(0.5f);
    }

    @Test
    void testDeleteShoppingList_NotFound() {
        Mockito.when(shoppingListRepository.existsById(any())).thenReturn(false);
        ShoppingListDto result = shoppingListService.deleteShoppingList(1L);
        assertThat(result).isNull();
    }

    @Test
    void testDeleteShoppingList_Found() {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,1L, "Cola", 0.5f);
        shoppingInventoryEntityList.add(shoppingInventoryEntity);
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        Mockito.when(shoppingListRepository.existsById(1L)).thenReturn(true);
        Mockito.when(shoppingListRepository.findById(1L)).thenReturn(Optional.of(shoppingListEntity));
        Mockito.when(shoppingListMapper.toShoppingListDto(Mockito.any(ShoppingListEntity.class))).thenReturn(
                new ShoppingListDto(1L, "XXX", new ArrayList<>()));

        ShoppingListDto result = shoppingListService.deleteShoppingList(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFridgeId()).isEqualTo("XXX");
        assertThat(result.getShoppingInventoryDtoList().size()).isEqualTo(0);
    }

    @Test
    void testUpdateShoppingList_NotFound() {
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,1L, "Cola", 0.5f);
        Mockito.when(shoppingListRepository.existsById(any())).thenReturn(false);
        ShoppingInventoryDto result = shoppingListService.updateShoppingList(1L,shoppingInventoryEntity);
        assertThat(result).isNull();
    }

    @Test
    void testUpdateShoppingList_Found() {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,1L, "Cola", 0.5f);
        shoppingInventoryEntityList.add(shoppingInventoryEntity);
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        Mockito.when(shoppingListRepository.existsById(1L)).thenReturn(true);
        Mockito.when(shoppingListRepository.findById(1L)).thenReturn(Optional.of(shoppingListEntity));
        Mockito.when(shoppingListRepository.save(Mockito.any(ShoppingListEntity.class))).thenReturn(shoppingListEntity);
        Mockito.when(shoppingListMapper.toShoppingListDto(Mockito.any(ShoppingListEntity.class))).thenReturn(
                new ShoppingListDto(1L,  "XXX", new ArrayList<>()));
        ShoppingInventoryDto result = shoppingListService.updateShoppingList(1L,shoppingInventoryEntity);
        assertThat(result).isNull();
    }

    @Test
    void testGetShoppingList_NotFound() {
        Mockito.when(shoppingListRepository.existsById(any())).thenReturn(false);
        ShoppingListDto result = shoppingListService.getShoppingList(1L);
        assertThat(result).isNull();
    }

    @Test
    void testGetShoppingList_Found() {
        List<ShoppingInventoryEntity> shoppingInventoryEntityList = new ArrayList<>();
        ShoppingInventoryEntity shoppingInventoryEntity = new ShoppingInventoryEntity(1L,1L, "Cola", 0.5f);
        shoppingInventoryEntityList.add(shoppingInventoryEntity);
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity(1L, "XXX", shoppingInventoryEntityList);

        Mockito.when(shoppingListRepository.existsById(1L)).thenReturn(true);
        Mockito.when(shoppingListRepository.findById(1L)).thenReturn(Optional.of(shoppingListEntity));
        Mockito.when(shoppingListMapper.toShoppingListDto(Mockito.any(ShoppingListEntity.class))).thenReturn(
                new ShoppingListDto(1L,  "XXX", new ArrayList<>()));

        ShoppingListDto result = shoppingListService.getShoppingList(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getShoppingInventoryDtoList().size()).isEqualTo(0);
    }

}

package de.markus.meier.coolschrank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.markus.meier.coolschrank.model.dto.FridgeDto;
import de.markus.meier.coolschrank.model.dto.FridgeInventoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {FridgeService.class})
public class FridgeServiceTest {

    @Autowired
    @InjectMocks
    private FridgeService fridgeService;

    @MockBean
    private WebClient mockedWebClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFridge() throws JsonProcessingException {

        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(mockedWebClient.post()).thenReturn(requestBodyUriSpecMock);
        Mockito.when(requestBodyUriSpecMock.uri(Mockito.anyString())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("{\"id\": \"XXX\"}"));


        String result = fridgeService.createFridge();

        assertThat(result).isEqualTo("XXX");

        Mockito.verify(mockedWebClient).post();
        Mockito.verify(requestBodyUriSpecMock).uri("https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge");
        Mockito.verify(requestBodySpecMock).retrieve();
        Mockito.verify(responseSpecMock).bodyToMono(String.class);
    }

    @Test
    void testAddFridgeInventory_OK() throws JsonProcessingException {

        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto("Cola", 0.5f, 0.5f);

        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(mockedWebClient.post()).thenReturn(requestBodyUriSpecMock);
        Mockito.when(requestBodyUriSpecMock.uri(Mockito.anyString())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.body(Mockito.any(BodyInserter.class))).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.headers(Mockito.any())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);

        String exampleJson = "{\"id\": 123, \"name\": \"Cola\", \"actual\": 0.5, \"target\": 0.5}";
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(exampleJson));
        Mockito.when(objectMapper.readValue(exampleJson, FridgeInventoryDto.class)).thenReturn(fridgeInventoryDto);

        FridgeInventoryDto result = fridgeService.addFridgeInventory(fridgeInventoryDto, "XXX");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Cola");
        assertThat(result.getActual()).isEqualTo(0.5f);
        assertThat(result.getTarget()).isEqualTo(0.5f);
    }

    @Test
    void testAddFridgeInventory_Conflict() throws JsonProcessingException {
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto("Cola", 0.5f, 0.5f);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = Mockito.mock(WebClient.RequestBodySpec.class);

        Mockito.when(mockedWebClient.post()).thenReturn(requestBodyUriSpecMock);
        Mockito.when(requestBodyUriSpecMock.uri(Mockito.anyString())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.body(Mockito.any(BodyInserter.class))).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.headers(Mockito.any())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.retrieve()).thenThrow(WebClientResponseException.class);

        FridgeInventoryDto result = fridgeService.addFridgeInventory(fridgeInventoryDto, "XXX");

        assertThat(result).isNull();
    }

    @Test
    void testUpDateFridgeInventory() throws JsonProcessingException {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto("Cola", 0.5f, 0.5f);

        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(mockedWebClient.post()).thenReturn(requestBodyUriSpecMock);
        Mockito.when(requestBodyUriSpecMock.uri(Mockito.anyString())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.body(Mockito.any(BodyInserter.class))).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.headers(Mockito.any())).thenReturn(requestBodySpecMock);
        Mockito.when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);

        String exampleJson = "{\"id\": 123, \"name\": \"Cola\", \"actual\": 0.5, \"target\": 0.5}";
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(exampleJson));
        Mockito.when(objectMapper.readValue(exampleJson, FridgeInventoryDto.class)).thenReturn(fridgeInventoryDto);

        FridgeInventoryDto result = fridgeService.upDateFridgeInventory(fridgeInventoryDto, "XXX");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Cola");
        assertThat(result.getActual()).isEqualTo(0.5f);
        assertThat(result.getTarget()).isEqualTo(0.5f);
    }

    @Test
    void testGetFridge_Ok() throws JsonProcessingException {
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        Mockito.when(mockedWebClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        String exampleJson = "{\"id\": \"XXX\", \"inventory\": [{\"name\": \"Cola\", \"target\": 0.5, \"actual\": 0.5}]}";

        Mono<String> jsonResponse = Mono.just(exampleJson);
        Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(jsonResponse);


        FridgeDto result = fridgeService.getFridge("XXX");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("XXX");
        assertThat(result.getInventory().size()).isEqualTo(1);
        assertThat(result.getInventory().get(0).getName()).isEqualTo("Cola");
        assertThat(result.getInventory().get(0).getTarget()).isEqualTo(0.5f);
        assertThat(result.getInventory().get(0).getActual()).isEqualTo(0.5f);
    }

}

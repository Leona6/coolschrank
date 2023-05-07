package de.markus.meier.coolschrank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.markus.meier.coolschrank.model.dto.FridgeDto;
import de.markus.meier.coolschrank.model.dto.FridgeInventoryDto;
import de.markus.meier.coolschrank.model.model.FridgeInventoryEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * The FridgeService class is responsible for managing and modifying fridges.
 */
@Service
public class FridgeService {

    private WebClient webClient;

    @PostConstruct
    private void setup() {
        webClient = WebClient.builder().build();
    }

    /**
     * Creates a new fridge.
     *
     * @return The ID of the created fridge.
     * @throws JsonProcessingException if there is an error processing JSON data.
     */
    public String createFridge() throws JsonProcessingException {
        String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge";
        String response = this.webClient.post().uri(url).retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        FridgeDto fridgeAPIDto = objectMapper.readValue(response, FridgeDto.class);
        return fridgeAPIDto.getId();
    }

    /**
     * Adds a fridge inventory to a fridge.
     *
     * @param fridgeInvetoryAPIDto The fridge inventory DTO to add.
     * @param fridgeId The ID of the fridge.
     * @return The added fridge inventory DTO.
     * @throws JsonProcessingException if there is an error processing JSON data.
     */
    public FridgeInventoryDto addFridgeInventory(FridgeInventoryDto fridgeInvetoryAPIDto, String fridgeId) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge/" + fridgeId + "/item";
            WebClient.RequestBodySpec requestBodySpec = this.webClient.post().uri(url);
            requestBodySpec.body(BodyInserters.fromValue(new FridgeInventoryEntity(fridgeInvetoryAPIDto.getName(), fridgeInvetoryAPIDto.getTarget())));
            requestBodySpec.headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8));
            String response = requestBodySpec.retrieve().bodyToMono(String.class).block();
            return objectMapper.readValue(response, FridgeInventoryDto.class);
        } catch (WebClientResponseException e) {
            return null;
        }
    }

    /**
     * Updates a fridge inventory in a fridge.
     *
     * @param fridgeInvetoryAPIDto The updated fridge inventory DTO.
     * @param fridgeId The ID of the fridge.
     * @return The updated fridge inventory DTO.
     * @throws JsonProcessingException if there is an error processing JSON data.
     * @throws WebClientResponseException if there is an error with the web client response.
     */
    public FridgeInventoryDto upDateFridgeInventory(FridgeInventoryDto fridgeInvetoryAPIDto, String fridgeId) throws JsonProcessingException, WebClientResponseException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge/" + fridgeId + "/item/" + fridgeInvetoryAPIDto.getId();
        WebClient.RequestBodySpec requestBodySpec = this.webClient.post().uri(url);
        requestBodySpec.body(BodyInserters.fromValue(fridgeInvetoryAPIDto));
        requestBodySpec.headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8));
        String response = requestBodySpec.retrieve().bodyToMono(String.class).block();
        return objectMapper.readValue(response, FridgeInventoryDto.class);
    }

    /**
     * Retrieves a fridge by its ID.
     *
     * @param id The ID of the fridge.
     * @return The retrieved fridge DTO.
     * @throws JsonProcessingException if there is an error processing JSON data.
     */
    public FridgeDto getFridge(String id) throws JsonProcessingException {
        try {
            String url = "https://innovations.rola.com/build/rola/coolschrank/ongoing/application/fridge/" + id;
            String response = this.webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, FridgeDto.class);
        } catch (WebClientResponseException e) {
            return null;
        }
    }
}

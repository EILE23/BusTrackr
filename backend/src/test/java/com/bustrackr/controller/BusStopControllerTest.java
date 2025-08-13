package com.bustrackr.controller;

import com.bustrackr.dto.BusStopSearchResponse;
import com.bustrackr.service.BusStopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusStopController.class)
class BusStopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusStopService busStopService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchBusStops_withKeyword_shouldReturnOk() throws Exception {
        // Given
        BusStopSearchResponse response = new BusStopSearchResponse();
        response.setStopId("STOP001");
        response.setStopName("강남역");
        response.setLatitude(37.4981);
        response.setLongitude(127.0276);
        response.setDistrict("강남구");

        when(busStopService.searchBusStops("강남"))
            .thenReturn(Arrays.asList(response));

        // When & Then
        mockMvc.perform(get("/bus-stops/search")
                .param("keyword", "강남"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].stopId").value("STOP001"))
            .andExpect(jsonPath("$[0].stopName").value("강남역"))
            .andExpect(jsonPath("$[0].district").value("강남구"));
    }

    @Test
    void searchBusStops_withName_shouldReturnOk() throws Exception {
        // Given
        BusStopSearchResponse response = new BusStopSearchResponse();
        response.setStopId("STOP002");
        response.setStopName("역삼역");
        response.setDistrict("강남구");

        when(busStopService.searchBusStopsByName("역삼"))
            .thenReturn(Arrays.asList(response));

        // When & Then
        mockMvc.perform(get("/bus-stops/search")
                .param("name", "역삼"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].stopName").value("역삼역"));
    }

    @Test
    void searchBusStops_withDistrict_shouldReturnOk() throws Exception {
        // Given
        when(busStopService.getBusStopsByDistrict("강남구"))
            .thenReturn(Arrays.asList(new BusStopSearchResponse()));

        // When & Then
        mockMvc.perform(get("/bus-stops/search")
                .param("district", "강남구"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void searchBusStops_withoutParameters_shouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/bus-stops/search"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void searchBusStops_emptyResult_shouldReturnEmptyArray() throws Exception {
        // Given
        when(busStopService.searchBusStops("없는역"))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/bus-stops/search")
                .param("keyword", "없는역"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getNearbyBusStops_shouldReturnOk() throws Exception {
        // Given
        BusStopSearchResponse response = new BusStopSearchResponse();
        response.setStopId("STOP001");
        response.setStopName("강남역");

        when(busStopService.getBusStopsNearLocation(
            eq(37.5000), eq(127.0300), eq(1.0)))
            .thenReturn(Arrays.asList(response));

        // When & Then
        mockMvc.perform(get("/bus-stops/nearby")
                .param("latitude", "37.5000")
                .param("longitude", "127.0300")
                .param("radiusKm", "1.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].stopName").value("강남역"));
    }

    @Test
    void getNearbyBusStops_withoutCoordinates_shouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/bus-stops/nearby")
                .param("radiusKm", "1.0"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getNearbyBusStops_withDefaultRadius_shouldReturnOk() throws Exception {
        // Given
        when(busStopService.getBusStopsNearLocation(
            eq(37.5000), eq(127.0300), eq(1.0)))
            .thenReturn(Arrays.asList(new BusStopSearchResponse()));

        // When & Then
        mockMvc.perform(get("/bus-stops/nearby")
                .param("latitude", "37.5000")
                .param("longitude", "127.0300"))
            .andExpect(status().isOk());
    }

    @Test
    void getBusStopsByDistrict_shouldReturnOk() throws Exception {
        // Given
        when(busStopService.getBusStopsByDistrict("강남구"))
            .thenReturn(Arrays.asList(new BusStopSearchResponse()));

        // When & Then
        mockMvc.perform(get("/bus-stops/district/강남구"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
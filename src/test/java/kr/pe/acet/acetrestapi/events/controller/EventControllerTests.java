package kr.pe.acet.acetrestapi.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pe.acet.acetrestapi.events.dto.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Springboot")
                .description("REST api development with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,02,26,17,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,02,27,17,32))
                .beginEventDateTime(LocalDateTime.of(2022,02,26,17,32))
                .endEventDateTime(LocalDateTime.of(2022,02,27,17,32))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("제주 첨단로 카카오스페이스 닷 투")
                .build();
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
        ;
    }

}

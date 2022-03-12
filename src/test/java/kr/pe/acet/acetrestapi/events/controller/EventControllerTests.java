package kr.pe.acet.acetrestapi.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pe.acet.acetrestapi.events.EventStatus;
import kr.pe.acet.acetrestapi.events.dto.Event;
import kr.pe.acet.acetrestapi.events.dto.EventDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
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
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Springboot")
                .description("REST api development with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,02,28,17,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,02,24,17,32))
                .beginEventDateTime(LocalDateTime.of(2022,02,24,17,32))
                .endEventDateTime(LocalDateTime.of(2022,02,23,17,32))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("제주 첨단로 카카오스페이스 닷 투")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }

}

package kr.pe.acet.acetrestapi.events.dto;


import kr.pe.acet.acetrestapi.events.dto.Event;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
   @Test
   public void builder(){
      Event event = Event.builder()
              .name("acet Spring REST Api Study")
              .description("REST Api Development with Springboot")
              .build();
      assertThat(event).isNotNull();
   }

   @Test
   public void javaBeanSpec(){
      // Given
      String name = "Event";
      String description = "Springboot";

      // When
      Event event = new Event();
      event.setName(name);
      event.setDescription(description);

      // Then
      assertThat(event.getName()).isEqualTo(name);
      assertThat(event.getDescription()).isEqualTo(description);
   }

   @Test
   public void testFree(){
      // Given
      Event event = Event.builder()
              .basePrice(0)
              .maxPrice(0)
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isFree()).isTrue();

      // Given
      event = Event.builder()
              .basePrice(100)
              .maxPrice(0)
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isFree()).isFalse();

      // Given
      event = Event.builder()
              .basePrice(0)
              .maxPrice(100)
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isFree()).isFalse();

   }

   @Test
   public void testOffline() {
      // Given
      Event event = Event.builder()
              .location("제주 첨단로 카카오스페이스 닷 투")
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isOffline()).isTrue();

      // Given
      event = Event.builder()
              .location("")
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isOffline()).isFalse();

   }

   }
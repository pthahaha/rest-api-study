package kr.pe.acet.acetrestapi.events;


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
}
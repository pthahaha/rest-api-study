package kr.pe.acet.acetrestapi.events.dto;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

   @ParameterizedTest
   @MethodSource("parametersProvider")
   public void testFree(int basePrice, int maxPrice, boolean isFree){
      // Given
      Event event = Event.builder()
              .basePrice(basePrice)
              .maxPrice(maxPrice)
              .build();
      // When
      event.update();

      // Then
      assertThat(event.isFree()).isEqualTo(isFree);
   }

   static Stream<Arguments> parametersProvider(){
      return Stream.of(
              arguments(0,0,true),
              arguments(100,0,false),
              arguments(0,100,false)
      );
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
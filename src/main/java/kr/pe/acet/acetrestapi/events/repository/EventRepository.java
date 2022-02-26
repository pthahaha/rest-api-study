package kr.pe.acet.acetrestapi.events.repository;

import kr.pe.acet.acetrestapi.events.dto.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}

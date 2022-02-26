package kr.pe.acet.acetrestapi.events.controller;

import kr.pe.acet.acetrestapi.events.dto.Event;
import kr.pe.acet.acetrestapi.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
   // @Autowired EventRepository eventRepository;
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event){
        Event eventSave = this.eventRepository.save(event);
         URI createdUri = linkTo(EventController.class).slash(eventSave.getId()).toUri();
         event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}

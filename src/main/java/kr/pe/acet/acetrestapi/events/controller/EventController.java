package kr.pe.acet.acetrestapi.events.controller;

import kr.pe.acet.acetrestapi.events.dto.Event;
import kr.pe.acet.acetrestapi.events.dto.EventDto;
import kr.pe.acet.acetrestapi.events.dto.EventResource;
import kr.pe.acet.acetrestapi.events.repository.EventRepository;
import kr.pe.acet.acetrestapi.events.EventValidator;
import kr.pe.acet.acetrestapi.utils.ErrorsResource;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
public class EventController {
   // @Autowired EventRepository eventRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @Autowired ErrorsResource errorsResource;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity<EntityModel> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class );
        event.update();
        Event eventSave = this.eventRepository.save(event);
        WebMvcLinkBuilder selfLinkbuilder = linkTo(EventController.class).slash(eventSave.getId());
        URI createdUri = selfLinkbuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkbuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create", "profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    private ResponseEntity<EntityModel> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(errorsResource.addLink(errors));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedModel = assembler.toModel(page, e -> new EventResource(e));
        pagedModel.add(Link.of("/docs/index.html#resources-events-list", "profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get", "profile"));

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {  // param에서 오류 발생! 즉, @Valid에서 걸리면
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors); // 바인딩 잘됐음 비즈니스 로직 상 체크
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent); // 어디에서 , 어디로
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update", "profile"));

        return ResponseEntity.ok(eventResource);
    }
}

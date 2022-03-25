package kr.pe.acet.acetrestapi.utils;

import kr.pe.acet.acetrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ErrorsResource{
    public EntityModel addLink(Errors content) {
        EntityModel<Errors> entityModel = EntityModel.of(content);
        entityModel.add(linkTo(methodOn(IndexController.class).indxe()).withRel("index"));
        return entityModel;
    }
}

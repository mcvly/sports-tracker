package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.model.repository.PersonRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mcvly
 * @since 12.04.14
 */
@RestController
@RequestMapping(value="/person/", produces= MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    @Resource
    private PersonRepository personRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Person information(@PathVariable("id") Integer personId) {
        return personRepository.getInfo(personId);
    }

}

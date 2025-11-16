package org.csu.javapersonnel.controller;

import org.csu.javapersonnel.entity.Personnel;
import org.csu.javapersonnel.service.PersonnelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personnel")
public class PersonnelController {

    private final PersonnelService service;

    public PersonnelController(PersonnelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Personnel> getAll() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Personnel getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public Personnel save(@RequestBody Personnel p) {
        service.save(p);
        return p;
    }

    @PutMapping("/{id}")
    public Personnel update(@PathVariable Integer id, @RequestBody Personnel p) {
        p.setPersonnelId(id);
        service.updateById(p);
        return p;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.removeById(id);
    }
}
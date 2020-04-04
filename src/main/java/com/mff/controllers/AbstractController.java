package com.mff.controllers;

import com.mff.data.dto.*;
import com.mff.data.entities.Account;
import com.mff.data.entities.ConvertibleEntity;
import com.mff.data.entities.Schedule;
import com.mff.repository.AccountRepository;
import com.mff.repository.ScheduleRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController<E extends ConvertibleEntity<D>,
        D extends ConvertibleDto<E>,
        N extends ConvertibleDto<E>,
        R extends CrudRepository<E, Integer>> {

    protected R repository;

    AbstractController(R repository) {
        this.repository = repository;
    }

    @GetMapping
    private Iterable<D> getAll() {
        List<D> entities = new ArrayList<>();
        repository.findAll().forEach(s -> entities.add(s.toDto()));
        return entities;
    }

    @GetMapping("/{id}")
    private ResponseEntity<D> get(@PathVariable Integer id) {
        return repository.findById(id).map(entity -> ResponseEntity.ok(entity.toDto())).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<D> create(@RequestBody N dto) throws URISyntaxException {
        D newDto = repository.save(dto.toEntity()).toDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<D> update(@PathVariable int id, @RequestBody N dto) {
        return ResponseEntity.ok(repository.save(dto.toEntity()).toDto());
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<StatusDto> delete(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok(StatusDto.builder().status("OK").build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

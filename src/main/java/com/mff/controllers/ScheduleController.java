package com.mff.controllers;

import com.mff.data.dto.NewScheduleDto;
import com.mff.data.dto.ScheduleDto;
import com.mff.data.dto.StatusDto;
import com.mff.data.entities.Schedule;
import com.mff.repository.AccountRepository;
import com.mff.repository.ScheduleRepository;
import com.mff.util.ScheduleFactory;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.springframework.data.rest.core.config.JsonSchemaFormat.URI;

@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @GetMapping
    private ResponseEntity<List<ScheduleDto>> getAll() {
        List<ScheduleDto> schedules = new ArrayList<>();
        scheduleRepository.findAll().forEach(s -> schedules.add(toDto(s)));
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Schedule> get(@PathVariable int id) {
        return scheduleRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<ScheduleDto> create(@RequestBody NewScheduleDto dto) throws URISyntaxException {
        if (dto.getIsPeriodic()) {
            if (dto.getPeriodLength() == null || dto.getPeriodUnit() == null || dto.getStartDate() == null) {
                return ResponseEntity.badRequest().build();
            }
        } else if (dto.getBaseDayOfMonth() == null) {
            return ResponseEntity.badRequest().build();
        }
        ScheduleDto scheduleDto = toDto(scheduleRepository.save(toEntity(dto)));
        return ResponseEntity.created(new URI("/" + scheduleDto.getId())).body(scheduleDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<ScheduleDto> create(@PathVariable int id, @RequestBody ScheduleDto dto) {
        if (dto.getIsPeriodic()) {
            if (dto.getPeriodLength() == null || dto.getPeriodUnit() == null || dto.getStartDate() == null) {
                return ResponseEntity.badRequest().build();
            }
        } else if (dto.getBaseDayOfMonth() == null) {
            return ResponseEntity.badRequest().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(toDto(scheduleRepository.save(toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<StatusDto> delete(@PathVariable int id) {
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            return ResponseEntity.ok(StatusDto.builder().status("OK").build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Schedule toEntity(NewScheduleDto dto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Schedule.class);
    }

    private Schedule toEntity(ScheduleDto dto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Schedule.class);
    }

    private ScheduleDto toDto(Schedule entity) {
        ModelMapper mapper = new ModelMapper();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Converter<Integer, Date> generateNextDate = ctx ->
                mapper.map(scheduleFactory.generateDate(entity, gregorianCalendar.get(Calendar.MONTH), gregorianCalendar.get(Calendar.YEAR))
                        , Date.class);
        Converter<Integer, Boolean> generateCanDelete = ctx ->
                mapper.map(accountRepository.countByScheduleId(ctx.getSource()) == 0
                        , Boolean.class);
        mapper.createTypeMap(Schedule.class, ScheduleDto.class)
                .addMappings(m -> {
                    m.using(generateNextDate)
                            .map(Schedule::getId, ScheduleDto::setNextDate);
                    m.using(generateCanDelete)
                            .map(Schedule::getId, ScheduleDto::setCanDelete);
                });
        return mapper.map(entity, ScheduleDto.class);
    }

}

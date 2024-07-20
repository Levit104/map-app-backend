package org.study.grabli_application.controller;

import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.study.grabli_application.dto.NewStreetObjectDto;
import org.study.grabli_application.dto.ObjectTypeDto;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.UpdateStreetObject;
import org.study.grabli_application.repository.StreetObjectDao;
import org.study.grabli_application.repository.StreetObjectTypeRepository;

@Slf4j
@RestController
@RequestMapping("/grabli")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StreetObjectController {
    private final StreetObjectTypeRepository objectTypeRepository;
    private final StreetObjectDao streetObjectDao;

    @GetMapping("/streetObjects")
    public ResponseEntity<List<StreetObjectDto>> getAllStreetObjects() {
        return ResponseEntity.ok(streetObjectDao.getAll());
    }

    @PostMapping("/streetObjects")
    public ResponseEntity<StreetObjectDto> saveStreetObject(@RequestBody NewStreetObjectDto streetObjectDto) {
        return ResponseEntity.ok(streetObjectDao.save(streetObjectDto));
    }

    @PostMapping("/commentStreetObject/{id}")
    public ResponseEntity<?> commentStreetObject(@PathVariable Long id, @RequestBody UpdateStreetObject dto) {
        streetObjectDao.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/streetObjects")
    public ResponseEntity<?> deleteStreetObject(@RequestParam Long id) {
        streetObjectDao.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/objectTypes")
    public ResponseEntity<List<ObjectTypeDto>> getStreetObjectTypes() {
        return ResponseEntity.ok(objectTypeRepository.findAll().stream()
                .map(t -> ObjectTypeDto.builder()
                        .id(t.getId())
                        .ObjectName(t.getObjectName())
                        .tag(t.getTag())
                        .build())
                .collect(toList()));
    }
}

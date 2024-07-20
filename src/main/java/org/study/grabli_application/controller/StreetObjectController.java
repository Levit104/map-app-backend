package org.study.grabli_application.controller;

import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        return ResponseEntity.ok(streetObjectDao.getAllStreetObjects());
    }

    @PostMapping("/streetObjects")
    public ResponseEntity<StreetObjectDto> saveStreetObject(@RequestBody NewStreetObjectDto streetObjectDto) {
        return ResponseEntity.ok(streetObjectDao.saveStreetObject(streetObjectDto));
    }

    @PostMapping("/commentStreetObject/{id}")
    public ResponseEntity commentStreetObject(
            @PathVariable Long id, @RequestBody UpdateStreetObject dto) {

        return streetObjectDao.commentStreetObject(id, dto);
    }

    @DeleteMapping("/streetObjects")
    public ResponseEntity deleteStreetObject(@RequestParam Long id) {
        return streetObjectDao.deleteStreetObject(id);
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

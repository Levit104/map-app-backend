package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.service.StreetObjectService;
import org.study.grabli_application.service.StreetObjectTypeService;

import java.util.List;

@RestController
@RequestMapping("/grabli")
@RequiredArgsConstructor
@Slf4j
public class StreetObjectController {
    private final StreetObjectService streetObjectService;
    private final StreetObjectTypeService streetObjectTypeService;

    @GetMapping("/streetObjects")
    public ResponseEntity<?> getAllStreetObjects() {
        return ResponseEntity.ok(streetObjectService.getAll());
    }

    @PostMapping("/streetObjects")
    public ResponseEntity<?> saveStreetObject(@RequestBody StreetObjectDtoCreate streetObjectDto) {
        return ResponseEntity.ok(streetObjectService.save(streetObjectDto));
    }

    @PostMapping("/commentStreetObject/{id}")
    public ResponseEntity<?> commentStreetObject(@PathVariable Long id, @RequestBody StreetObjectDtoUpdate dto) {
        streetObjectService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/streetObjects")
    public ResponseEntity<?> deleteStreetObject(@RequestParam Long id) {
        streetObjectService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/objectTypes")
    public ResponseEntity<List<StreetObjectTypeDto>> getStreetObjectTypes() {
        return ResponseEntity.ok(streetObjectTypeService.getAll());
    }

    // TODO @PreAuthorize
    @PatchMapping("/streetObjects/{id}")
    public ResponseEntity<?> approveStreetObject(@PathVariable Long id, @RequestParam boolean approved) {
        streetObjectService.changeApproved(id, approved);
        return ResponseEntity.ok().build();
    }
}

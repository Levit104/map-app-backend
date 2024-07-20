package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.grabli_application.dto.NewStreetObjectDto;
import org.study.grabli_application.dto.ObjectTypeDto;
import org.study.grabli_application.dto.UpdateStreetObject;
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
    public ResponseEntity<?> saveStreetObject(@RequestBody NewStreetObjectDto streetObjectDto) {
        return ResponseEntity.ok(streetObjectService.save(streetObjectDto));
    }

    @PostMapping("/commentStreetObject/{id}")
    public ResponseEntity<?> commentStreetObject(@PathVariable Long id, @RequestBody UpdateStreetObject dto) {
        streetObjectService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/streetObjects")
    public ResponseEntity<?> deleteStreetObject(@RequestParam Long id) {
        streetObjectService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/objectTypes")
    public ResponseEntity<List<ObjectTypeDto>> getStreetObjectTypes() {
        return ResponseEntity.ok(streetObjectTypeService.getAll());
    }
}

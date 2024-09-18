package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.service.StreetObjectService;
import org.study.grabli_application.service.StreetObjectTypeService;

import java.util.List;

@RestController
@RequestMapping("/street-objects")
@RequiredArgsConstructor
@Slf4j
public class StreetObjectController {
    private final StreetObjectService streetObjectService;
    private final StreetObjectTypeService streetObjectTypeService;

    @GetMapping
    public List<StreetObjectDto> getAllStreetObjects() {
        log.info("Получение всех объектов");
        return streetObjectService.getAll();
    }

    @PostMapping
    public StreetObjectDto saveStreetObject(/*@RequestBody*/ @ModelAttribute StreetObjectDtoCreate streetObjectDto) {
        log.info("Сохранение объекта {}", streetObjectDto);
        return streetObjectService.save(streetObjectDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public void updateStreetObject(@PathVariable Long id, @RequestBody StreetObjectDtoUpdate dto) {
        log.info("Обновление объекта #{}", id);
        streetObjectService.update(id, dto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteStreetObject(@PathVariable Long id) {
        log.info("Удаление объекта #{}", id);
        streetObjectService.delete(id);
    }

    @GetMapping("/types")
    public List<StreetObjectTypeDto> getStreetObjectTypes() {
        log.info("Получение всех типов объектов");
        return streetObjectTypeService.getAll();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public void approveStreetObject(@PathVariable Long id, @RequestParam boolean approved) {
        log.info("Обновление статуса объекта #{}", id);
        streetObjectService.changeApproved(id, approved);
    }
}

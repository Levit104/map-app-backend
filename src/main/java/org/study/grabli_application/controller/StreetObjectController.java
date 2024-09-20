package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.service.ImageService;
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
    private final ImageService imageService;

    private String generateImageUrl(String filePathOrName) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/street-objects/images/")
                .path(imageService.getFileName(filePathOrName))
                .toUriString();
    }

    @GetMapping
    public List<StreetObjectDto> getAllStreetObjects() {
        log.info("Получение всех объектов");
        List<StreetObjectDto> dtoList = streetObjectService.getAll();
        dtoList.forEach(dto -> dto.setImage(generateImageUrl(dto.getImage())));
        return dtoList;
    }

    @PostMapping
    public StreetObjectDto saveStreetObject(@ModelAttribute StreetObjectDtoCreate requestDto) {
        log.info("Сохранение объекта {}", requestDto);
        String imagePath = imageService.save(requestDto.getImage());
        StreetObjectDto responseDto = streetObjectService.save(requestDto, imagePath);
        responseDto.setImage(generateImageUrl(imagePath));
        return responseDto;
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        log.info("Получение изображения {}", fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageService.getContentType(fileName)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(imageService.load(fileName));
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

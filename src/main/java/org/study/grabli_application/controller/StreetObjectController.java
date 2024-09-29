package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.service.StreetObjectService;
import org.study.grabli_application.service.StreetObjectTypeService;
import org.study.grabli_application.util.ImageContainer;

import java.util.List;

@RestController
@RequestMapping("/street-objects")
@RequiredArgsConstructor
@Slf4j
public class StreetObjectController {
    private final StreetObjectService streetObjectService;
    private final StreetObjectTypeService streetObjectTypeService;

    private void setImageUrl(StreetObjectDto dto) {
        dto.setImage("/street-objects/images/" + dto.getImage());
    }

    @GetMapping
    public List<StreetObjectDto> getAllStreetObjects() {
        log.info("Получение всех объектов");
        List<StreetObjectDto> dtoList = streetObjectService.getAll();
        dtoList.forEach(this::setImageUrl);
        return dtoList;
    }

    @PostMapping
    public StreetObjectDto saveStreetObject(
            @RequestPart("info") StreetObjectDtoCreate requestDto,
            @RequestPart("image") MultipartFile image
    ) {
        log.info("Сохранение объекта {}", requestDto);
        StreetObjectDto responseDto = streetObjectService.save(requestDto, image);
        setImageUrl(responseDto);
        return responseDto;
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        log.info("Получение изображения {}", fileName);
        ImageContainer image = streetObjectService.loadImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(image.getResource());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public StreetObjectDto updateStreetObject(@PathVariable Long id, @RequestBody StreetObjectDtoUpdate requestDto) {
        log.info("Обновление объекта #{}", id);
        StreetObjectDto responseDto = streetObjectService.update(id, requestDto);
        setImageUrl(responseDto);
        return responseDto;
    }

    // @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStreetObject(@PathVariable Long id) {
        log.info("Удаление объекта #{}", id);
        streetObjectService.delete(id);
        return ResponseEntity.noContent().build();
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

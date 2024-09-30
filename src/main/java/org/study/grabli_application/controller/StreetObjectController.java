package org.study.grabli_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/street-objects")
@RequiredArgsConstructor
public class StreetObjectController {
    private final StreetObjectService streetObjectService;
    private final StreetObjectTypeService streetObjectTypeService;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    private void setImageUrl(StreetObjectDto dto) {
        dto.setImage(servletContextPath + "/street-objects/images/" + dto.getImage());
    }

    @GetMapping
    public List<StreetObjectDto> getAllStreetObjects() {
        List<StreetObjectDto> dtoList = streetObjectService.getAll();
        dtoList.forEach(this::setImageUrl);
        return dtoList;
    }

    @PostMapping
    public StreetObjectDto saveStreetObject(
            @Valid @RequestPart("info") StreetObjectDtoCreate requestDto,
            @RequestPart("image") MultipartFile image
    ) {
        StreetObjectDto responseDto = streetObjectService.save(requestDto, image);
        setImageUrl(responseDto);
        return responseDto;
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        ImageContainer image = streetObjectService.loadImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(image.getResource());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public StreetObjectDto updateStreetObject(
            @PathVariable Long id,
            @Valid @RequestBody StreetObjectDtoUpdate requestDto
    ) {
        StreetObjectDto responseDto = streetObjectService.update(id, requestDto);
        setImageUrl(responseDto);
        return responseDto;
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStreetObject(@PathVariable Long id) {
        streetObjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    public List<StreetObjectTypeDto> getStreetObjectTypes() {
        return streetObjectTypeService.getAll();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public void approveStreetObject(@PathVariable Long id, @RequestParam boolean approved) {
        streetObjectService.changeApproved(id, approved);
    }
}

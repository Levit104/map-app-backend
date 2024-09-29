package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.exceptions.EntityNotFoundException;
import org.study.grabli_application.mapper.StreetObjectMapper;
import org.study.grabli_application.repository.StreetObjectRepository;
import org.study.grabli_application.util.ImageContainer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetObjectService {
    private final StreetObjectRepository streetObjectRepository;
    private final StreetObjectMapper streetObjectMapper;
    private final ImageService imageService;

    public List<StreetObjectDto> getAll() {
        return streetObjectMapper.toDtoList(streetObjectRepository.findAll());
    }

    public StreetObjectDto save(StreetObjectDtoCreate dto, MultipartFile image) {
        StreetObject streetObject = streetObjectMapper.toEntity(dto);
        streetObject.setImage(imageService.save(image));
        return streetObjectMapper.toDto(streetObjectRepository.save(streetObject));
    }

    public ImageContainer loadImage(String fileName) {
        return imageService.load(fileName);
    }

    public StreetObjectDto update(Long id, StreetObjectDtoUpdate dto) {
        StreetObject streetObject = getById(id);
        streetObjectMapper.updateEntity(streetObject, dto);
        return streetObjectMapper.toDto(streetObjectRepository.save(streetObject));
    }

    public void delete(Long id) {
        StreetObject streetObject = getById(id);
        imageService.delete(streetObject.getImage());
        streetObjectRepository.delete(streetObject);
    }

    public void changeApproved(Long id, boolean approved) {
        StreetObject streetObject = getById(id);
        streetObject.setApproved(approved);
        streetObjectRepository.save(streetObject);
    }

    private StreetObject getById(Long id) {
        return streetObjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Объект не найден")
        );
    }
}

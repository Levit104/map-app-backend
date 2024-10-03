package org.study.grabli_application.service.impl;

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
import org.study.grabli_application.service.ImageService;
import org.study.grabli_application.service.StreetObjectService;
import org.study.grabli_application.util.ImageContainer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetObjectServiceImpl implements StreetObjectService {
    private final StreetObjectRepository streetObjectRepository;
    private final StreetObjectMapper streetObjectMapper;
    private final ImageService imageService;

    @Override
    public List<StreetObjectDto> getAll() {
        return streetObjectMapper.toDtoList(streetObjectRepository.findAll());
    }

    @Override
    public StreetObjectDto get(Long id) {
        return streetObjectMapper.toDto(findById(id));
    }

    @Override
    public StreetObjectDto save(StreetObjectDtoCreate dto, MultipartFile image) {
        StreetObject streetObject = streetObjectMapper.toEntity(dto);
        streetObject.setImage(imageService.save(image));
        return streetObjectMapper.toDto(streetObjectRepository.save(streetObject));
    }

    @Override
    public StreetObjectDto update(Long id, StreetObjectDtoUpdate dto) {
        StreetObject streetObject = findById(id);
        streetObjectMapper.updateEntity(streetObject, dto);
        return streetObjectMapper.toDto(streetObjectRepository.save(streetObject));
    }

    @Override
    public void delete(Long id) {
        StreetObject streetObject = findById(id);
        imageService.delete(streetObject.getImage());
        streetObjectRepository.delete(streetObject);
    }

    @Override
    public void changeApproved(Long id, boolean approved) {
        StreetObject streetObject = findById(id);
        streetObject.setApproved(approved);
        streetObjectRepository.save(streetObject);
    }

    @Override
    public ImageContainer loadImage(String fileName) {
        return imageService.load(fileName);
    }

    private StreetObject findById(Long id) {
        return streetObjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Объект не найден")
        );
    }
}

package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
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

    public List<StreetObjectDto> getAll(String downloadUrl) {
        List<StreetObjectDto> dtoList = streetObjectMapper.toDtoList(streetObjectRepository.findAll());
        dtoList.forEach(dto -> dto.setImage(downloadUrl + imageService.getFileName(dto.getImage())));
        return dtoList;
    }

    public StreetObjectDto save(StreetObjectDtoCreate requestDto, String downloadUrl) {
        String imagePath = imageService.save(requestDto.getImage());

        StreetObject streetObject = streetObjectMapper.toEntity(requestDto);
        streetObject.setImage(imagePath);

        StreetObjectDto responseDto = streetObjectMapper.toDto(streetObjectRepository.save(streetObject));
        responseDto.setImage(downloadUrl + imageService.getFileName(responseDto.getImage()));

        return responseDto;
    }

    public ImageContainer loadImage(String fileName) {
        return new ImageContainer(imageService.getContentType(fileName), imageService.load(fileName));
    }

    public void update(Long id, StreetObjectDtoUpdate dto) {
        StreetObject streetObject = getById(id);
        // TODO доработать фронт
        streetObject.setTitle(dto.getTitle());
        streetObject.setDescription("TODO");
        streetObject.setImage("TODO");
        streetObject.setCreatorName("TODO");
        streetObject.setCreatorContact("TODO");
        streetObjectRepository.save(streetObject);
    }

    public void delete(Long id) {
        // try catch нужен для Spring Boot 2, в Boot 3 исключение не бросается
        try {
            streetObjectRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Объект не найден");
        }
    }

    public void changeApproved(Long id, boolean approved) {
        StreetObject streetObject = getById(id);
        streetObject.setApproved(approved);
        streetObjectRepository.save(streetObject);
    }

    public StreetObject getById(Long id) {
        return streetObjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Объект не найден")
        );
    }
}

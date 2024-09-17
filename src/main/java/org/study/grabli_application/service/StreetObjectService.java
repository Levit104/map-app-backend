package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.exceptions.EntityNotFoundException;
import org.study.grabli_application.repository.StreetObjectRepository;
import org.study.grabli_application.util.MappingHelper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreetObjectService {
    private final StreetObjectRepository streetObjectRepository;
    private final MappingHelper mappingHelper;

    public List<StreetObjectDto> getAll() {
        return mappingHelper.mapList(streetObjectRepository.findAll(), StreetObjectDto.class);
    }

    public StreetObjectDto save(StreetObjectDtoCreate dto) {
        StreetObject streetObject = mappingHelper.mapObject(dto, StreetObject.class);
        streetObject.setImage("TODO"); // TODO доработать фронт
        streetObjectRepository.save(streetObject);
        return mappingHelper.mapObject(streetObject, StreetObjectDto.class);
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

package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.mapper.StreetObjectTypeMapper;
import org.study.grabli_application.repository.StreetObjectTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetObjectTypeService {
    private final StreetObjectTypeRepository streetObjectTypeRepository;
    private final StreetObjectTypeMapper streetObjectTypeMapper;

    public List<StreetObjectTypeDto> getAll() {
        return streetObjectTypeMapper.toDtoList(streetObjectTypeRepository.findAll());
    }
}

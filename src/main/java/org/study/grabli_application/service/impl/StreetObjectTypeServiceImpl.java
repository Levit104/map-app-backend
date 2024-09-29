package org.study.grabli_application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.mapper.StreetObjectTypeMapper;
import org.study.grabli_application.repository.StreetObjectTypeRepository;
import org.study.grabli_application.service.StreetObjectTypeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetObjectTypeServiceImpl implements StreetObjectTypeService {
    private final StreetObjectTypeRepository streetObjectTypeRepository;
    private final StreetObjectTypeMapper streetObjectTypeMapper;

    @Override
    public List<StreetObjectTypeDto> getAll() {
        return streetObjectTypeMapper.toDtoList(streetObjectTypeRepository.findAll());
    }
}

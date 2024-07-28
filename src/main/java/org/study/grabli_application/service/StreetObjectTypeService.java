package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.repository.StreetObjectTypeRepository;
import org.study.grabli_application.util.MappingHelper;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StreetObjectTypeService {
    private final StreetObjectTypeRepository streetObjectTypeRepository;
    private final MappingHelper mappingHelper;

    public List<StreetObjectTypeDto> getAll() {
        return mappingHelper.mapList(streetObjectTypeRepository.findAll(), StreetObjectTypeDto.class);
    }
}

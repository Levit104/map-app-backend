package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.ObjectTypeDto;
import org.study.grabli_application.entity.StreetObjectType;
import org.study.grabli_application.repository.StreetObjectTypeRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StreetObjectTypeService {
    private final StreetObjectTypeRepository streetObjectTypeRepository;

    public List<ObjectTypeDto> getAll() {
        return streetObjectTypeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ObjectTypeDto mapToDto(StreetObjectType s) {
        return ObjectTypeDto.builder()
                .id(s.getId())
                .ObjectName(s.getObjectName())
                .tag(s.getTag())
                .build();
    }
}

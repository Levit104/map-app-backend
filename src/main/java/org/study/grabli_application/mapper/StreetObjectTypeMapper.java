package org.study.grabli_application.mapper;

import org.mapstruct.*;
import org.study.grabli_application.dto.StreetObjectTypeDto;
import org.study.grabli_application.entity.StreetObjectType;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StreetObjectTypeMapper {
    StreetObjectTypeDto toDto(StreetObjectType streetObjectType);

    List<StreetObjectTypeDto> toDtoList(List<StreetObjectType> streetObjectTypes);
}

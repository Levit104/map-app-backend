package org.study.grabli_application.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Point;
import org.mapstruct.*;
import org.study.grabli_application.dto.Coordinates;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.exceptions.EntityCreationException;
import org.study.grabli_application.util.GeometryUtil;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = StreetObjectTypeMapper.class)
public interface StreetObjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type.id", source = "typeId")
    @Mapping(target = "location", expression = "java(toPoint(dto))")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "approved", ignore = true)
    StreetObject toEntity(StreetObjectDtoCreate dto);

    @Mapping(target = "coordinates", expression = "java(toCoordinates(streetObject))")
    StreetObjectDto toDto(StreetObject streetObject);

    List<StreetObjectDto> toDtoList(List<StreetObject> streetObjectList);

    default Point toPoint(StreetObjectDtoCreate dto) {
        try {
            Coordinates coordinates = new ObjectMapper().readValue(dto.getCoordinates(), Coordinates.class);
            return GeometryUtil.createPoint(coordinates.getLat(), coordinates.getLng());
        } catch (JsonProcessingException e) {
            throw new EntityCreationException("Ошибка при создании объекта");
        }
    }

    default Coordinates toCoordinates(StreetObject streetObject) {
        return new Coordinates(streetObject.getLocation().getX(), streetObject.getLocation().getY());
    }
}

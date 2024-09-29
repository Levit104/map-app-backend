package org.study.grabli_application.mapper;

import org.locationtech.jts.geom.Point;
import org.mapstruct.*;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.util.GeometryUtils;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = StreetObjectTypeMapper.class
)
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "creatorName", ignore = true)
    @Mapping(target = "creatorContact", ignore = true)
    void updateEntity(@MappingTarget StreetObject streetObject, StreetObjectDtoUpdate dto);

    default Point toPoint(StreetObjectDtoCreate dto) {
        double[] coordinates = dto.getCoordinates();
        return GeometryUtils.createPoint(coordinates[0], coordinates[1]);
    }

    default double[] toCoordinates(StreetObject streetObject) {
        Point location = streetObject.getLocation();
        return new double[]{location.getX(), location.getY()};
    }
}

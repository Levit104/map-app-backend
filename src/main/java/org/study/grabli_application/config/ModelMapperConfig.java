package org.study.grabli_application.config;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.study.grabli_application.dto.Coordinates;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.entity.StreetObject;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        TypeMap<StreetObject, StreetObjectDto> toDTO = modelMapper
                .createTypeMap(StreetObject.class, StreetObjectDto.class);

        Converter<Point, Coordinates> toLatLng = (context) -> new Coordinates(
                context.getSource().getX(),
                context.getSource().getY()
        );

        toDTO.addMappings(mapper -> mapper
                .using(toLatLng)
                .map(StreetObject::getLocation, StreetObjectDto::setCoordinates)
        );

        TypeMap<StreetObjectDtoCreate, StreetObject> fromDTO = modelMapper
                .createTypeMap(StreetObjectDtoCreate.class, StreetObject.class);

        Converter<Coordinates, Point> fromLatLng = (context -> geometryFactory
                .createPoint(new Coordinate(context.getSource().getLat(), context.getSource().getLng()))
        );

        fromDTO.addMappings(mapper -> mapper
                .using(fromLatLng)
                .map(StreetObjectDtoCreate::getCoordinates, StreetObject::setLocation)
        );

        // без этого устанавливается streetObject.id такой же, как у streetObject.type.id
        fromDTO.addMappings(mapper -> mapper
                .skip(StreetObject::setId)
        );

        return modelMapper;
    }
}

package org.study.grabli_application.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.study.grabli_application.exceptions.EntityCreationException;

@Configuration
@Slf4j
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        ObjectMapper objectMapper = new ObjectMapper();
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

        // Converter<Coordinates, Point> fromLatLng = (context -> geometryFactory
        //         .createPoint(new Coordinate(context.getSource().getLat(), context.getSource().getLng()))

        Converter<String, Point> fromLatLng = (context -> {
            try {
                Coordinates coordinates = objectMapper.readValue(context.getSource(), Coordinates.class);
                return geometryFactory
                        .createPoint(new Coordinate(coordinates.getLat(), coordinates.getLng()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new EntityCreationException("Ошибка при создании объекта");
            }
        }
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

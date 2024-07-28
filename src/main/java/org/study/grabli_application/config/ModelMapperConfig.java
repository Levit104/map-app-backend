package org.study.grabli_application.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.study.grabli_application.dto.Coordinate;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.entity.StreetObject;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ModelMapperConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<StreetObject, StreetObjectDto> typeMap = modelMapper
                .createTypeMap(StreetObject.class, StreetObjectDto.class);

        Converter<String, Coordinate> toCoordinate = (context) -> {
            Coordinate coordinate = null;

            try {
                coordinate = objectMapper.readValue(context.getSource(), Coordinate.class);
            } catch (JsonProcessingException e) {
                log.error("Ошибка при получении координат", e);
            }

            return coordinate;
        };

        typeMap.addMappings(mapper -> mapper
                .using(toCoordinate)
                .map(StreetObject::getCoordinate, StreetObjectDto::setCoordinate)
        );

        return modelMapper;
    }
}

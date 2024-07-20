package org.study.grabli_application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.Coordinate;
import org.study.grabli_application.dto.NewStreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.UpdateStreetObject;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.exceptions.EntityCreationException;
import org.study.grabli_application.exceptions.EntityNotFoundException;
import org.study.grabli_application.repository.StreetObjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreetObjectService {
    private final StreetObjectRepository streetObjectRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public List<StreetObjectDto> getAll() {
        return streetObjectRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private StreetObjectDto mapToDTO(StreetObject s) {
        Coordinate coordinate = null;

        try {
            coordinate = objectMapper.readValue(s.getCoordinate(), Coordinate.class);
        } catch (JsonProcessingException ex) {
            log.error("error while extract coordinates", ex);
        }

        return StreetObjectDto.builder()
                .id(s.getId())
                .idStreetObjectType(s.getIdStreetObjectType())
                .idCreator(s.getIdCreator())
                .coordinate(coordinate)
                .comment(s.getComment())
                .build();
    }

    public StreetObjectDto save(NewStreetObjectDto dto) {
        String sql = "insert into grabli_schema.project_object "
                + "(id_creater, id_object, coordinate, commentary) "
                + "values (:userId, :type, ST_GeomFromText(:point, 4326), :commentary)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", 4) // FIXME ставится id-шник существующего пользователя, т.к. пользователей не будет - убрать
                .addValue("type", dto.getIdStreetObjectType())
                .addValue("point", String.format("POINT(%s %s)",
                        dto.getCoordinate().getCoordinates()[0], dto.getCoordinate().getCoordinates()[1]))
                .addValue("commentary", dto.getComment());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int insertedRows = jdbcTemplate.update(sql, params, keyHolder);

        if (insertedRows != 1) {
            throw new EntityCreationException("Ошибка при создании объекта");
        }

        Long id = (Long) Optional.ofNullable(keyHolder.getKeys())
                .orElseThrow(() -> new EntityCreationException("Ошибка при создании объекта"))
                .get("id");

        return StreetObjectDto.builder().id(id).build();
    }

    public void update(Long id, UpdateStreetObject dto) {
        StreetObject streetObject = streetObjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Объект не найден")
        );

        streetObject.setComment(dto.getComment());
        streetObjectRepository.save(streetObject);
    }

    public void delete(Long id) {
        // try catch нужен для Spring Boot 2, в Boot 3 исключение не бросается
        try {
            streetObjectRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Объект не найден");
        }
    }
}

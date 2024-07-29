package org.study.grabli_application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.entity.StreetObject;
import org.study.grabli_application.exceptions.EntityCreationException;
import org.study.grabli_application.exceptions.EntityNotFoundException;
import org.study.grabli_application.repository.StreetObjectRepository;
import org.study.grabli_application.util.MappingHelper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreetObjectService {
    private final StreetObjectRepository streetObjectRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MappingHelper mappingHelper;

    public List<StreetObjectDto> getAll() {
        return mappingHelper.mapList(streetObjectRepository.findAll(), StreetObjectDto.class);
    }

    public StreetObjectDto save(StreetObjectDtoCreate dto) {
        String sql = "insert into grabli_schema.street_object "
                + "(type_id, coordinate, title, description, image, creator_name, creator_contact) "
                + "values (:typeId, ST_GeomFromText(:point, 4326), :title, :description, :image, :creatorName, :creatorContact)";


        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("typeId", dto.getTypeId())
                .addValue("point", String.format("POINT(%s %s)",
                        dto.getCoordinate().getCoordinates()[0], dto.getCoordinate().getCoordinates()[1]))
                .addValue("title", dto.getTitle())
                // TODO доработать фронт
                .addValue("description", "TODO")
                .addValue("image", "TODO")
                .addValue("creatorName", "TODO")
                .addValue("creatorContact", "TODO");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int insertedRows = jdbcTemplate.update(sql, params, keyHolder);

        if (insertedRows != 1) {
            throw new EntityCreationException("Ошибка при создании объекта");
        }

        Long id = (Long) Optional.ofNullable(keyHolder.getKeys())
                .orElseThrow(() -> new EntityCreationException("Ошибка при создании объекта"))
                .get("id");

        return new StreetObjectDto(id);
    }

    public void update(Long id, StreetObjectDtoUpdate dto) {
        StreetObject streetObject = getById(id);
        // TODO доработать фронт
        streetObject.setTitle(dto.getTitle());
        streetObject.setDescription("TODO");
        streetObject.setImage("TODO");
        streetObject.setCreatorName("TODO");
        streetObject.setCreatorContact("TODO");
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

    public void changeApproved(Long id, boolean approved) {
        StreetObject streetObject = getById(id);
        streetObject.setApproved(approved);
        streetObjectRepository.save(streetObject);
    }

    public StreetObject getById(Long id) {
        return streetObjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Объект не найден")
        );
    }
}

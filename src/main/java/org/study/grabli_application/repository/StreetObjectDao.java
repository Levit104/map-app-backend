package org.study.grabli_application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.study.grabli_application.dto.Coordinate;
import org.study.grabli_application.dto.NewStreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.UpdateStreetObject;
import org.study.grabli_application.entity.StreetObject;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StreetObjectDao {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;
  private final StreetObjectRepository streetObjectRepository;

  public List<StreetObjectDto> getStreetObjects(Long userId) {

    return streetObjectRepository.findByIdCreator(userId).stream()
        .map(this::mapStreetObjectToDTO)
        .collect(Collectors.toList());
  }

    public List<StreetObjectDto> getAllStreetObjects() {
      return streetObjectRepository.findAll().stream()
              .map(this::mapStreetObjectToDTO)
              .collect(Collectors.toList());
    }

    private StreetObjectDto mapStreetObjectToDTO(StreetObject s) {
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

  public StreetObjectDto saveStreetObject(NewStreetObjectDto streetObjectDto, Long userId) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int numberOfInsertedRows = jdbcTemplate.update(
        "insert into grabli_schema.project_object "
            + "(id_creater, id_object, coordinate, commentary) "
            + "values (:userId, :type, ST_GeomFromText(:point, 4326), :commentary)",
        new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("type", streetObjectDto.getIdStreetObjectType())
            .addValue("point",
                "POINT("
                    + streetObjectDto.getCoordinate().getCoordinates()[0]
                    + " " + streetObjectDto.getCoordinate().getCoordinates()[1] + ")")
            .addValue("commentary", streetObjectDto.getComment()),
        keyHolder);

    if (numberOfInsertedRows == 1) {
      return Optional.ofNullable(keyHolder.getKeys()).map(m ->
              StreetObjectDto.builder()
                  .id((Long) m.get("id"))
                  .build())
          .orElse(null);
    } else {
      return null;
    }
  }

  public ResponseEntity commentStreetObject(Long id, UpdateStreetObject dto, Long userId) {

    return streetObjectRepository.findById(id)
        .map(s -> {
          if (s.getIdCreator().equals(userId)) {
            s.setComment(dto.getComment());
            streetObjectRepository.saveAndFlush(s);
            return ResponseEntity.ok().build();
          } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
          }
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  public ResponseEntity deleteStreetObject(Long id, Long userId) {

    return streetObjectRepository.findById(id)
        .map(s -> {
          if (s.getIdCreator().equals(userId)) {
            streetObjectRepository.delete(s);
            return ResponseEntity.ok().build();
          } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
          }
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

  }
}

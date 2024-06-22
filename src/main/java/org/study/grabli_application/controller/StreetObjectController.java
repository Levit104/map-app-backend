package org.study.grabli_application.controller;

import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import org.study.grabli_application.dto.NewStreetObjectDto;
import org.study.grabli_application.dto.ObjectTypeDto;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.UpdateStreetObject;
import org.study.grabli_application.entity.User;
import org.study.grabli_application.repository.StreetObjectDao;
import org.study.grabli_application.repository.StreetObjectTypeRepository;
import org.study.grabli_application.service.UserService;

@Slf4j
@RestController
@RequestMapping("/grabli")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StreetObjectController {

  private final StreetObjectTypeRepository objectTypeRepository;
  private final UserService userService;
  private final StreetObjectDao streetObjectDao;


  @GetMapping("/userStreetObjects")
  public ResponseEntity<List<StreetObjectDto>> getStreetObjects() {

    return ResponseEntity.ok(
        Optional.ofNullable(getCurrentUser())
            .map(u -> streetObjectDao.getStreetObjects(u.getId()))
            .orElseGet(Collections::emptyList));
  }

  @GetMapping("/streetObjects")
  public ResponseEntity<List<StreetObjectDto>> getAllStreetObjects() {
    return ResponseEntity.ok(streetObjectDao.getAllStreetObjects());
  }

  @PostMapping("/streetObjects")
  public ResponseEntity<StreetObjectDto> saveStreetObject(@RequestBody NewStreetObjectDto streetObjectDto) {

    User user = getCurrentUser();

    return Optional.ofNullable(user)
        .map(u ->
            ResponseEntity.ok(streetObjectDao.saveStreetObject(streetObjectDto, u.getId())))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }


  @PostMapping("/commentStreetObject/{id}")
  public ResponseEntity commentStreetObject(
      @PathVariable Long id, @RequestBody UpdateStreetObject dto) {

    return streetObjectDao.commentStreetObject(id, dto, getCurrentUser().getId());
  }


  @DeleteMapping("/streetObjects")
  public ResponseEntity deleteStreetObject(@RequestParam Long id) {

    return streetObjectDao.deleteStreetObject(id, getCurrentUser().getId());
  }


  @GetMapping("/objectTypes")
  public ResponseEntity<List<ObjectTypeDto>> getStreetObjectTypes() {

    return ResponseEntity.ok(objectTypeRepository.findAll().stream()
        .map(t -> ObjectTypeDto.builder()
            .id(t.getId())
            .ObjectName(t.getObjectName())
            .tag(t.getTag())
            .build())
        .collect(toList()));
  }


  private User getCurrentUser() {

    User user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() != null) {
      user = userService.findByUsername(
          (authentication.getPrincipal().toString()));
    }

    return user;
  }
}

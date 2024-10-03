package org.study.grabli_application.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.dto.StreetObjectDto;
import org.study.grabli_application.dto.StreetObjectDtoCreate;
import org.study.grabli_application.dto.StreetObjectDtoUpdate;
import org.study.grabli_application.util.ImageContainer;

import java.util.List;

public interface StreetObjectService {
    List<StreetObjectDto> getAll();

    StreetObjectDto get(Long id);

    StreetObjectDto save(StreetObjectDtoCreate dto, MultipartFile image);

    StreetObjectDto update(Long id, StreetObjectDtoUpdate dto);

    void delete(Long id);

    void changeApproved(Long id, boolean approved);

    ImageContainer loadImage(String fileName);
}

package org.admin.services.interfaces;

import org.admin.entities.device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ideviceservice {
    Page<device> findAll (Pageable pageable);

    List<device> getAll();

    Optional<device> findOneById(Integer deviceid);

    device createOrEditOne(device device);

    void deleteOneById(Integer  deviceid);

    device save(device device);
}


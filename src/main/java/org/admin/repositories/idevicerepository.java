package org.admin.repositories;

import org.admin.entities.device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface idevicerepository extends JpaRepository<device, Integer> {
}

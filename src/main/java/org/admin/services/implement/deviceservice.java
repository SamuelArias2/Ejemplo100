package org.admin.services.implement;

import org.admin.entities.device;
import org.admin.repositories.idevicerepository;
import org.admin.services.interfaces.ideviceservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class deviceservice implements ideviceservice {
    @Override
    public device save(device device) {
        return devicerepository.save(device);
    }

    @Autowired
    private idevicerepository devicerepository;

    @Override
    public Page<device> findAll(Pageable pageable) {
        return  devicerepository.findAll(pageable);
    }

    @Override
    public List<device> getAll() {
        return devicerepository.findAll();
    }

    public Optional<device> findOneById(Integer deviceid) {
        return devicerepository.findById(deviceid);
    }

    @Override
    public device createOrEditOne(device device) {
        return devicerepository.save(device);
    }

    @Override
    public void deleteOneById(Integer deviceid) {
        devicerepository.deleteById(deviceid);
    }
}

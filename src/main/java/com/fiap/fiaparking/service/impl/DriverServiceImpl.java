package com.fiap.fiaparking.service.impl;


import com.fiap.fiaparking.mappers.DriverMapper;
import com.fiap.fiaparking.model.Driver;
import com.fiap.fiaparking.model.Vehicle;
import com.fiap.fiaparking.repository.DriverRepository;
import com.fiap.fiaparking.repository.VehicleRepository;
import com.fiap.fiaparking.service.DriverService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Driver createDriver(Driver driverDTO) {
        log.info("Creating a new driver with ID: {}", driverDTO.getId());
        Driver newDriver = driverMapper.toDriverEntity(driverDTO);
        Driver savedDriver = driverRepository.save(newDriver);
        log.info("New driver created with ID: {}", savedDriver.getId());
        return savedDriver;
    }

    @Override
    public Driver updateDriver(Driver driver) {
        log.info("Updating driver with ID: {}", driver.getId());
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(Long id) {
        log.info("Retrieving driver with ID: {}", id);
        return driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with ID: {} not found", id);
                    return new EntityNotFoundException("Driver not found");
                });
    }

    @Override
    public List<Driver> getAllDrivers() {
        log.info("Retrieving all drivers");
        return driverRepository.findAll();
    }

    @Override
    public Driver addVehicleToDriver(Long driverId, String vehicleId) {
        log.info("Adding vehicle with ID: {} to driver with ID: {}", vehicleId, driverId);
        Driver driver = getDriverById(driverId);
        Vehicle vehicle = vehicleRepository.findById(Long.valueOf(vehicleId))
                .orElseThrow(() -> {
                    log.error("Vehicle with ID: {} not found", vehicleId);
                    return new EntityNotFoundException("Vehicle not found");
                });
        driver.getVehicles()
                .add(vehicle);
        vehicle.setDriver(driver);
        Driver updatedDriver = updateDriver(driver);
        log.info("Vehicle with ID: {} added to driver with ID: {}", vehicleId, driverId);
        return updatedDriver;
    }

    @Override
    public ResponseEntity<List<Vehicle>> getDriverVehicles(String driverId) {
        log.info("Retrieving vehicles for driver with ID: {}", driverId);
        Driver driver = getDriverById(Long.valueOf(driverId));
        return ResponseEntity.ok(driver.getVehicles());
    }

}


package com.fiap.fiaparking.controller;


import com.fiap.fiaparking.dtos.ParkingSessionDTO;
import com.fiap.fiaparking.model.ParkingSession;
import com.fiap.fiaparking.repository.ParkingSessionRepository;
import com.fiap.fiaparking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/parking")
public class ParkingSessionController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    ParkingSessionRepository parkingRepository;

    @PostMapping
    public ResponseEntity<ParkingSession> createParking(@RequestBody ParkingSessionDTO parkingSessionDTO) {
        ParkingSession newParking = parkingService.createParking(parkingSessionDTO);
        return new ResponseEntity<>(newParking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSession> getParking(@PathVariable String id) {
        Optional<ParkingSession> parkingOptional = parkingRepository.findById(Long.valueOf(id));
        return parkingOptional.map(parking -> new ResponseEntity<>(parking, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSession>> listParkings() {
        List<ParkingSession> parkings = parkingRepository.findAll();
        return new ResponseEntity<>(parkings, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteParkings() {
        parkingRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSession> updateParkingExit(
            @PathVariable String id,
            @RequestBody ParkingSessionDTO parkingSessionDTO
    ) {
        return new ResponseEntity<>(
                parkingService.updateParkingExit(id, parkingSessionDTO), HttpStatus.OK);
    }


}

package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(final SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public Sensor saveSensor(final SensorDto sensor) {
    if (sensorRepository.existsByName(sensor.name())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Sensor with name " + sensor.name() + " already exists");
    }

    return sensorRepository.save(new Sensor(
        sensor.name(),
        sensor.type()
    ));
  }
}

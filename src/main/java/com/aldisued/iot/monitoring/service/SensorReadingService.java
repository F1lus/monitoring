package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(
          final SensorReadingRepository sensorReadingRepository,
          final SensorRepository sensorRepository
  ) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public SensorReading saveSensorReading(final SensorReadingDto sensorReadingDto) {
    final Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Sensor with id " + sensorReadingDto.sensorId() + " not found"
            ));

    final SensorReading sensorReading = new SensorReading(
            sensorReadingDto.value(),
            sensorReadingDto.timestamp(),
            sensor
    );

    return sensorReadingRepository.save(sensorReading);
  }

}

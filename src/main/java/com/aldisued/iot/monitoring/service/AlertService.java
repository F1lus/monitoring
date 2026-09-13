package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(
          final AlertRepository alertRepository,
          final SensorRepository sensorRepository,
          final KafkaTemplate<String, AlertDto> kafkaTemplate
  ) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public Alert saveAlert(final AlertDto alertDto) {
    final Sensor sensor = sensorRepository.findById(alertDto.sensorId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Sensor with id " + alertDto.sensorId() + " not found"
            ));
    final Alert alert = alertRepository.save(new Alert(
            alertDto.message(),
            alertDto.timestamp(),
            sensor
    ));

    kafkaTemplate.send("alerts", new AlertDto(sensor.getId(), alert.getMessage(), alert.getTimestamp()));

    return alert;
  }

  @Transactional(readOnly = true)
  public AlertDto findLastAlertBySensorId(final UUID sensorId) {
    return alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId)
            .map(alert -> new AlertDto(sensorId, alert.getMessage(), alert.getTimestamp()))
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There are no alerts in the database"
            ));
  }
}

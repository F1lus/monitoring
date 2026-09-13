package com.aldisued.iot.monitoring.messaging;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.service.SensorReadingService;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class SensorReadingListener {
  private final SensorReadingService sensorReadingService;

  public SensorReadingListener(SensorReadingService sensorReadingService) {
    this.sensorReadingService = sensorReadingService;
  }

  @KafkaListener(topics = {"sensor-reading"}, groupId = "iot-monitoring")
  public void listen(@Valid SensorReadingDto sensorReadingDto) {
    sensorReadingService.saveSensorReading(sensorReadingDto);
  }

}

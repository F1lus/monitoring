package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, String> {

    @Query("""
            SELECT AVG(sensorReading.value) FROM SensorReading sensorReading
            JOIN sensorReading.sensor sensor
            WHERE sensor.type = com.aldisued.iot.monitoring.entity.SensorType.TEMPERATURE
            AND sensorReading.timestamp BETWEEN :from AND :to""")
    Optional<Double> getAverageTemperatureBetweenDates(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}

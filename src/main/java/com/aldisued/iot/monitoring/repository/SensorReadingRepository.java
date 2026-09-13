package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
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

    @Query("""
            SELECT sensorReading.value FROM SensorReading sensorReading
            JOIN sensorReading.sensor sensor
            WHERE sensor.type = :sensorType
            AND sensorReading.timestamp BETWEEN :from AND :to
            ORDER BY sensorReading.timestamp""")
    List<Double> getMeasurementValuesBySensorTypeBetweenDates(
            @Param("sensorType") SensorType sensorType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}

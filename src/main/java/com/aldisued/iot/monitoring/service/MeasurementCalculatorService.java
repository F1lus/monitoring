package com.aldisued.iot.monitoring.service;


import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(final List<Double> values, final Double deviation) {
    if (deviation == null || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Deviation must not be null and must be between 0.0 and 1.0");
    }

    if (values == null) {
      throw new IllegalArgumentException("The list of values cannot be null");
    }

    final double averageDeviation = values.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

    final double range = Math.abs(deviation * averageDeviation);
    final double acceptableMinDeviation = averageDeviation - range;
    final double acceptableMaxDeviation = averageDeviation + range;

    return values.stream()
            .filter(value -> value >= acceptableMinDeviation && value <= acceptableMaxDeviation)
            .toList();
  }

  public List<Double> getMovingAverage(List<Double> data, int windowSize) {
    // TODO: Task 10
    return List.of();
  }

}

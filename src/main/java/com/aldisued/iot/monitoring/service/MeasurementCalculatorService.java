package com.aldisued.iot.monitoring.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

  public List<Double> getMovingAverage(final List<Double> data, final int windowSize) {
    if (CollectionUtils.isEmpty(data)) {
      throw new IllegalArgumentException("Data must not be empty");
    }

    if (windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException("Window size must be between 1 and " + data.size());
    }

    final List<Double> movingAverage = new ArrayList<>();
    double windowSum = IntStream.range(0, windowSize)
            .mapToDouble(data::get)
            .sum();
    movingAverage.add(windowSum / windowSize);

    for (int i = windowSize; i < data.size(); i++) {
      windowSum += data.get(i) - data.get(i - windowSize);
      movingAverage.add(windowSum / windowSize);
    }

    return movingAverage;
  }

}

package org.example.newrelic_opentelemetry.service;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    @Autowired
    OpenTelemetry openTelemetry;

    public void recordCustom(int value) {
        Meter meter = openTelemetry.getMeter("MetricServiceMeter");
        LongCounter counter = meter.counterBuilder("TestCounter")
                .setUnit("1")
                .build();
        counter.add(value);
    }
}

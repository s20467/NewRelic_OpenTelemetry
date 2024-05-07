package org.example.newrelic_opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import io.opentelemetry.instrumentation.runtimemetrics.BufferPools;
import io.opentelemetry.instrumentation.runtimemetrics.Classes;
import io.opentelemetry.instrumentation.runtimemetrics.Cpu;
import io.opentelemetry.instrumentation.runtimemetrics.GarbageCollector;
import io.opentelemetry.instrumentation.runtimemetrics.MemoryPools;
import io.opentelemetry.instrumentation.runtimemetrics.Threads;
import io.opentelemetry.instrumentation.spring.webmvc.v6_0.SpringWebMvcTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import jakarta.servlet.Filter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NewRelicOpenTelemetryApplication {
    private static volatile OpenTelemetry openTelemetry = OpenTelemetry.noop();

    public static void main(String[] args) {
        var openTelemetrySdk = AutoConfiguredOpenTelemetrySdk.builder()
                .build()
                .getOpenTelemetrySdk();
        NewRelicOpenTelemetryApplication.openTelemetry =
                openTelemetrySdk;

        BufferPools.registerObservers(openTelemetrySdk);
        Classes.registerObservers(openTelemetrySdk);
        Cpu.registerObservers(openTelemetrySdk);
        GarbageCollector.registerObservers(openTelemetrySdk);
        MemoryPools.registerObservers(openTelemetrySdk);
        Threads.registerObservers(openTelemetrySdk);

        SpringApplication.run(
                NewRelicOpenTelemetryApplication.class, args);

        OpenTelemetryAppender.install(openTelemetrySdk);
    }

    @Bean
    public OpenTelemetry openTelemetry() {
        return openTelemetry;
    }

    @Bean
    public Filter webMvcTracingFilter(OpenTelemetry openTelemetry) {
        return SpringWebMvcTelemetry.create(openTelemetry)
                .createServletFilter();
    }
}
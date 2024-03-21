package com.table.service.tableservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;

// import io.opentelemetry.api.OpenTelemetry;
// import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

@SpringBootApplication
public class TableServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableServiceApplication.class, args);
	}

	// @Bean
	// public OpenTelemetry openTelemetry() {
	// 	return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
	// }

}

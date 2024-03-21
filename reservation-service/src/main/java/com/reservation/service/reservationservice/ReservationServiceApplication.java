package com.reservation.service.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;

// import io.opentelemetry.api.OpenTelemetry;
// import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

	// @Bean
	// public OpenTelemetry openTelemetry() {
	// 	return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
	// }

}

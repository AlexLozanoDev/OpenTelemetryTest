package com.recarga.recargaservice.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recarga.recargaservice.Models.ClientModel;
import com.recarga.recargaservice.Models.RecargaModel;
import com.recarga.recargaservice.Respository.IRecargaRepository;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;

@Service
public class RecargaService {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RecargaModel.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    IRecargaRepository recargaRepository;
    private final String clientsApiUrl = "http://localhost:8081";
    private final RestTemplate restTemplate = new RestTemplate();

    public ArrayList<RecargaModel> getRecargas() {

        return (ArrayList<RecargaModel>) recargaRepository.findAll();
    }

    public RecargaModel newRecarga(RecargaModel recargaModel) throws Exception {

        Span span = Span.current();
        try (Scope scope = span.makeCurrent()){
            String bodyRequest = objectMapper.writeValueAsString(recargaModel);
            span.setAttribute("body.request", bodyRequest);

            
            ClientModel client = getClient(recargaModel.getLinea());
            if (client == null) {
                recargaModel.setStatus("Linea invalida");
            }
            if (generarNumeroAleatorio() < 0.3) {
                recargaModel.setStatus("Pago no acreditado");
                LOGGER.info("No se pudo realizar la recarga con id: ", recargaModel.getId());
                span.setStatus(StatusCode.ERROR, "No se pudo procesar el pago");
            } else {
                recargaModel.setStatus("Exitoso");
                Double saldoNuevo = client.getSaldo() + recargaModel.getMonto();
                LOGGER.info("Saldo: " + saldoNuevo);
                actualizarSaldoClient(saldoNuevo, recargaModel.getLinea());
            }
            RecargaModel response = recargaRepository.save(recargaModel);

            String bodyResponse = objectMapper.writeValueAsString(response);
            span.setAttribute("body.response", bodyResponse);

            return response;

        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }

    private ClientModel getClient(String linea) {
        return restTemplate.getForObject(clientsApiUrl + "/clients/" + linea, ClientModel.class);
    }

    private void actualizarSaldoClient(double monto, String linea) {
        String requestUrl = clientsApiUrl + "/clients/" + linea;
        java.util.Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("saldo", monto);
        LOGGER.info("Saldo 2: " + monto + " linea: " + linea);
        restTemplate.put(requestUrl, requestBody);
    }

    private double generarNumeroAleatorio() {
        Random random = new Random();
        return random.nextDouble();
    }
}

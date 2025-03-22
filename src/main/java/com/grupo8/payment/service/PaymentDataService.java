package com.grupo8.payment.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo8.payment.models.InfoPayment;

public class PaymentDataService {

    public String GetPayment(String token) throws JsonProcessingException{
        InfoPayment info = new InfoPayment();

        info.setDescripcion("Prueba");
        info.setMonto(10000);
        info.setTime(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        info.setStatus("Completado");
        info.setToken(token);
        info.setMetodo("Debito");

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(info);
    }

}

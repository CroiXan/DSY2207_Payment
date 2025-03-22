package com.grupo8.payment.functions;

import java.util.Base64;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo8.payment.models.CreatePayment;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import com.grupo8.payment.service.PaymentDataService;

public class PaymentFunctions {

    private PaymentDataService paymentDataService;

   @FunctionName("createpayment")
    public HttpResponseMessage postCreate(
        @HttpTrigger(
            name = "request",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.FUNCTION
        )
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context
    ) {
        if(!this.checkAuthorization(request.getHeaders().get("authorization"))){
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .body("No autorizado.")
                    .build();
        }

        context.getLogger().info("Se recibió un POST.");
        String requestBody = request.getBody().orElse("");
        CreatePayment create = new CreatePayment();
        ObjectMapper mapper = new ObjectMapper();

        try {
            create = mapper.readValue(requestBody, CreatePayment.class);
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Error de parametros")
                    .build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                .body("Pago Creado y en Proceso: " + create.getToken())
                .build();
    }

    @FunctionName("checkpayment")
    public HttpResponseMessage get(
        @HttpTrigger(
            name = "request",
            methods = {HttpMethod.GET},
            authLevel = AuthorizationLevel.FUNCTION,
            route = "checkpayment/{token}"
        )
        HttpRequestMessage<Optional<String>> request,
        @BindingName("token") String token,
        final ExecutionContext context
    ) {
        if(!this.checkAuthorization(request.getHeaders().get("authorization"))){
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .body("No autorizado.")
                    .build();
        }

        context.getLogger().info("Se recibió un GET.");
        String jsonResponse;

        try {
            jsonResponse = paymentDataService.GetPayment(token);
        } catch (JsonProcessingException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Error de Integrtacion")
                    .build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
    }

    private boolean checkAuthorization(String authHeader){

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }

        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);

        if (values.length < 2) {
            return false;
        }

        String username = values[0];
        String password = values[1];

        if (!username.equals("grupo8") || !password.equals("grupo8")) {
            return false;
        }

        return true;
    }
}
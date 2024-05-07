package request.store;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import io.qameta.allure.Step;
import request.SendRequest;

public class Order {

    private static String responseOrder;

    @Step("Place an order for a pet")
    public static String postOrder(String id, String petId, String quantity) {

        String time = Instant.now().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT);

        String method = SendRequest.methodHTTP.POST;
        String endpoint = "/store/order";
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        String requestBody = "{\n" +
                             "  \"id\": " + id + ",\n" +
                             "  \"petId\": " + petId + ",\n" +
                             "  \"quantity\": " + quantity + ",\n" +
                             "  \"shipDate\": \"" + time + "\",\n" +
                             "  \"status\": \"placed\",\n" +
                             "  \"complete\": true\n" +
                             "}";

        responseOrder = SendRequest.sendRequest(method, endpoint, headers, requestBody);

        return responseOrder;
    }

    public static String returnResponseOrder() {
        return responseOrder;
    }

    public static String getOrder(String orderId) {

        String method = SendRequest.methodHTTP.GET;
        String endpoint = "/store/order/" + orderId;
        Map<String, String> headers = null;
        Map<String, String> params = null;

        return SendRequest.sendRequest(method, endpoint, headers, params);
    }

    public static String deleteOrder(String orderId) {

        String method = SendRequest.methodHTTP.DELETE;
        String endpoint = "/store/order/" + orderId;
        Map<String, String> headers = null;
        Map<String, String> params = null;

        return SendRequest.sendRequest(method, endpoint, headers, params);
    }
}

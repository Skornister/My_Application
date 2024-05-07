package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SendRequest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    public interface methodHTTP {
        String POST = "POST";
        String GET = "GET";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

    public static String sendRequest(String method, String endpoint, Map<String, String> headers, Map<String, String> params) {
        // Формируем полный URL
        String fullUrl = BASE_URL + endpoint;

        try {
            URL url = new URL(fullUrl);

            // Открываем соединение с URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Устанавливаем метод запроса (POST, GET, ...)
            connection.setRequestMethod(method);

            // Устанавливаем заголовки
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // Обработка тела запроса для метода PUT или POST
            if (method.equals(methodHTTP.PUT) || method.equals(methodHTTP.POST)) {
                if (params != null && !params.isEmpty()) {
                    connection.setDoOutput(true);

                    // Создаем параметры для тела запроса
                    String postData = encodeParams(params);

                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }
            }

            // Получаем код ответа от сервера
            int responseCode = connection.getResponseCode();
            System.out.println(method + " Response Code for '" + fullUrl + "' :: " + responseCode);

            // Обработка успешного запроса
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encodeParams(Map<String, String> params) {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!postData.isEmpty()) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return postData.toString();
    }

    public static String sendRequest(String method, String endpoint, Map<String, String> headers, String requestBody) {
        // Формируем полный URL
        String fullUrl = BASE_URL + endpoint;

        try {
            URL url = new URL(fullUrl);

            // Открываем соединение с URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Устанавливаем метод запроса (POST, GET, ...)
            connection.setRequestMethod(method);

            // Устанавливаем заголовки
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // Обработка тела запроса для метода PUT или POST
            if (method.equals("PUT") || method.equals("POST")) {
                if (requestBody != null && !requestBody.isEmpty()) {
                    connection.setDoOutput(true);

                    // Устанавливаем Content-Type как application/json
                    connection.setRequestProperty("Content-Type", "application/json");

                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }
            }

            // Получаем код ответа от сервера
            int responseCode = connection.getResponseCode();
            System.out.println(method + " Response Code for '" + fullUrl + "' :: " + responseCode);

            // Обработка успешного запроса
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
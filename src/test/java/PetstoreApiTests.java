import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PetstoreApiTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String TAG_POSITIVE = "positive";
    private static final String TAG_NEGATIVE = "negative";

    @Test
    @Tag(TAG_POSITIVE)
    void testGetInventory() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/store/inventory")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }
    }

    @Test
    @Tag(TAG_POSITIVE)
    void testPlaceOrder() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("petId", "1")
                .add("quantity", "1")
                .add("shipDate", "2024-05-06T10:00:00Z")
                .add("status", "placed")
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/store/order")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            // Дополнительные проверки на содержимое ответа
        }
    }

    @Test
    @Tag(TAG_POSITIVE)
    void testGetOrderById() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/store/order/1")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            // Дополнительные проверки на содержимое ответа
        }
    }

    @Test
    @Tag(TAG_NEGATIVE)
    void testDeleteOrderById() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/store/order/9999") // Используем несуществующий orderId
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(404, response.code());
        }
    }
}
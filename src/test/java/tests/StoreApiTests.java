package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static io.qameta.allure.util.ResultsUtils.ISSUE_LINK_TYPE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import io.qameta.allure.Description;
import io.qameta.allure.Link;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import request.store.Inventory;
import request.store.Order;

@DisplayName("API Store")
@Link(name = "Swagger", url = "https://petstore.swagger.io", type = ISSUE_LINK_TYPE)
public class StoreApiTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2/";
    private static final String TAG_POSITIVE = "positive";
    private static final String TAG_NEGATIVE = "negative";

    @Tag(TAG_POSITIVE)
    @Test
    @DisplayName("Товары в наличии")
    @Description("""
            Вернуть количество товаров '{elementName}' из GET /store/inventory
            Проверка: наличие товаров '{elementName}' больше 0""")
    public void testGetInventory() {
        String elementName = "available";
        int count = Inventory.getElementCount(elementName);
        Assertions.assertTrue(count > 0);
        System.out.println("Count: " + count);
    }

    @Tag(TAG_POSITIVE)
    @Test
    @DisplayName("Разнообразие товаров")
    @Description("""
            Вернуть список товаров из GET /store/inventory
            Проверка: разнообразие товаров больше 1""")
    public void testGetInventoryNegative() {
        List<String> inventoryList = Inventory.getInventoryList();
        Assertions.assertTrue(inventoryList.size() > 1);
        System.out.println("Товаров: " + inventoryList.size());
    }

    @Tag(TAG_POSITIVE)
    @Test
    @DisplayName("Создание нового заказа")
    @Description("""
                 Создать новый заказ с помощью POST /store/order
                 Проверка: заказ создан /store/order/{orderId}
                 Удалить заказ с помощью DELETE /store/order/{orderId}""")
    public void testPlaceOrder() {
        String id = "1";
        String petId = "1";
        String quantity = "1";
        String response = Order.postOrder(id, petId, quantity);
        System.out.println("response: " + response);

        String response2 = Order.getOrder(petId);
        System.out.println("response2: " + response2);

        Assertions.assertEquals(response, response2);
        Order.deleteOrder(petId);
    }

    @Tag(TAG_NEGATIVE)
    @Test
    @DisplayName("Ошибка: При создании заказа указать текст в количестве")
    @Description("""
                 Запрос POST /store/order с невалидным значением в поле quantity
                 Падающий тест""")
    public void testPlaceOrderNegative() {
        String id = "1";
        String petId = "1";
        String quantity = "asf";
        String response = Order.postOrder(id, petId, quantity);
        System.out.println("response: " + response);

        String response2 = Order.getOrder(petId);
        System.out.println("response2: " + response2);

        Assertions.assertEquals(response, response2);

    }

    @Tag(TAG_NEGATIVE)
    @Test
    @DisplayName("Ошибка: Удалить несуществующий заказ")
    @Description("""
                 Запрос DELETE /store/order/{orderId}
                 Проверка: код ответа 200
                 Падающий тест""")
    public void testDeleteOrderById() {
        String orderId = "1";
        String response = Order.deleteOrder(orderId);
        System.out.println("response: " + response);
        Assertions.assertTrue(response.contains("\"code\":200"), "response не содержит '\"code\":200'");
    }

    @Tag(TAG_NEGATIVE)
    @Test
    @DisplayName("Ошибка: При удалении заказа указать текст в поле orderId")
    @Description("""
                 Запрос DELETE /store/order/{text}
                 Проверка: код ответа 400
                 Падающий тест""")
    public void testDeleteOrderByIdNegative() {
        String orderId = "-1";
        String response = Order.deleteOrder(orderId);
        System.out.println("response: " + response);
        Assertions.assertTrue(response.contains("\"code\":400"), "response не содержит '\"code\":400'");
    }
}
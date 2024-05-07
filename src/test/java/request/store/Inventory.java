package request.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import io.qameta.allure.Step;
import request.SendRequest;

public class Inventory {

    private static String responseInventory;

    @Step("Получить inventory")
    public static String getInventory() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ": Получить inventory");

        String method = SendRequest.methodHTTP.GET;
        String endpoint = "/store/inventory";
        Map<String, String> headers = null;
        Map<String, String> params = null;

        responseInventory = SendRequest.sendRequest(method, endpoint, headers, params);

        return responseInventory;
    }

    public static String getResponseInventory() {
        return responseInventory;
    }

    @Step("Вернуть количество товаров '{element}' из inventory")
    public static int getElementCount(String elementName) {
        if (responseInventory == null) {
            getInventory();
        }
        String response = getResponseInventory();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        if (jsonObject.has(elementName)) {
            return jsonObject.get(elementName).getAsInt();
        } else {
            return -1;
        }
    }

    @Step("Вернуть список товаров из inventory")
    public static List<String> getInventoryList() {
        if (responseInventory == null) {
            getInventory();
        }
        String response = getResponseInventory();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        return List.of(jsonObject.keySet().toArray(new String[0]));
    }
}

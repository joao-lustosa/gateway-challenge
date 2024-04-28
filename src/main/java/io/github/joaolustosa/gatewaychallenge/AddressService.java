package io.github.joaolustosa.gatewaychallenge;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class AddressService {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String BASE_URL;

    static {
        Properties properties = new Properties();

        try {
            properties.load(AddressService.class.getClassLoader().getResourceAsStream("config.properties"));

            BASE_URL = properties.getProperty("brasilApiBaseUrl");
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar as propriedades.", e);
        }
    }

    public static Address fetchCepInfo(String cep) {
        try {
            HttpRequest request = buildRequest(cep);
            HttpResponse<String> response = sendRequest(request);

            return parseResponse(response);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao buscar dados do CEP: " + e.getMessage());

            return null;
        }
    }

    private static HttpRequest buildRequest(String cep) {
        return HttpRequest.newBuilder().uri(URI.create(BASE_URL + cep)).build();
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static Address parseResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() == 200) {
            return new Gson().fromJson(response.body(), Address.class);
        } else {
            throw new IOException("Não foi possível buscar o CEP. Código do erro: " + response.statusCode());
        }
    }
}

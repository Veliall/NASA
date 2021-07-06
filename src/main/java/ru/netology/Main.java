package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Получение контента от NASA API
 */
public class Main {

    final static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "https://api.nasa.gov/planetary/apod?api_key=qVHGU1go8Z6OJr7METI6WbGvJf1o8YzygW4ekbBR";

        //Получаем данные и десиарилизуем объект
        CloseableHttpResponse response = getRequest(httpClient, url);
        Content content = mapper.readValue(response.getEntity().getContent(), Content.class);
        response.close();

        //Получаем адрес контента, создаём файл, записываем данные в файл
        String contentUrl = content.getUrl();
        CloseableHttpResponse contentResponse = getRequest(httpClient, contentUrl);
        File file = new File(contentUrl);
        File contentFile = new File(file.getName());
        contentFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(contentFile);
        while (true) {
            int f = contentResponse.getEntity().getContent().read();
            if (f == -1) break;
            fos.write(f);
        }
        fos.close();
        contentResponse.close();
        httpClient.close();
    }

    public static CloseableHttpResponse getRequest(CloseableHttpClient httpClient, String url) throws IOException {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        return response;
    }
}

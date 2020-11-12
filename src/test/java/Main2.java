/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.net.URI;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * dropbox webhook verify mock.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
public class Main2 {

    static String webhooktUrl = System.getenv("VAVI_APPS_WEBHOOK_WEBHOOK_URL");

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost httpPost = new HttpPost(webhooktUrl);
            URI uri = new URIBuilder("http://localhost:5000/webhook/dropbox")
                    .setParameter("challenge", UUID.randomUUID().toString())
                    .build();
            HttpPost httpPost = new HttpPost(uri);

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(EntityUtils.toString(httpResponse.getEntity()));
            } else {
                System.out.println("status: " + httpResponse.getStatusLine().getStatusCode());
            }
        }
    }
}

/* */

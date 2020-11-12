/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 * @see "https://developers.google.com/calendar/v3/push"
 */
@PropsEntity(url = "file:.env")
public class Main1 {

    static String webhooktUrlBase = System.getenv("VAVI_APPS_WEBHOOK_WEBHOOK_URL_BASE");

    @Property
    String VAVI_APPS_WEBHOOK_SECRET;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Main1 app = new Main1();
        PropsEntity.Util.bind(app);
        app.test1();
        app.test2();
    }

    /**
     * google webhook mock.
     */
    void test1() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

//            HttpPost httpPost = new HttpPost(webhooktUrlBase + "google/drive/change");
            HttpPost httpPost = new HttpPost("http://localhost:5000/webhook/google/drive/change");
            httpPost.addHeader("User-Agent", "APIs-Google; (+https://developers.google.com/webmasters/APIs-Google.html)");
            httpPost.addHeader("X-Goog-Channel-ID", "975f3bec-1b48-4e76-ab12-e41c5ae78df2");
            httpPost.addHeader("X-Goog-Channel-Expiration", "Wed, 24 Jan 2018 00:08:23 GMT");
            httpPost.addHeader("X-Goog-Channel-Token", VAVI_APPS_WEBHOOK_SECRET);
            httpPost.addHeader("X-Goog-Resource-State", "change");
            httpPost.addHeader("X-Goog-Message-Number", "84392");
            httpPost.addHeader("X-Goog-Resource-ID", "ddiixCVmASnAg9qWY**********");
            httpPost.addHeader("X-Goog-Resource-URI", "https://www.googleapis.com/drive/v3/changes?includeCorpusRemovals=false&includeRemoved=true&includeTeamDriveItems=false&pageSize=100&pageToken=8762710&restrictToMyDrive=false&spaces=drive&supportsTeamDrives=false&alt=json");
            httpPost.addHeader("Accept-Charset", "UTF-8");
            httpPost.addHeader("X-AppEngine-Country", "ZZ");

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(EntityUtils.toString(httpResponse.getEntity()));
            } else {
                System.out.println("status: " + httpResponse.getStatusLine().getStatusCode());
            }
        }
    }

    /**
     * box webhook v1 mock.
     */
    void test2() throws IOException, URISyntaxException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

//            URI uri = new URIBuilder(webhooktUrlBase + "v1box")
            URI uri = new URIBuilder("http://localhost:5000/webhook/v1box")
                .setParameter("from_user_id", "user123456")
                .setParameter("event_type", "update")
                .setParameter("item__id", "item12345")
                .setParameter("item_parent_folder_id", "item67890")
                .setParameter("new_item_id", "item99999")
                .setParameter("new_item_parent_folder_id", "item00000")
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

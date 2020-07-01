/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook;

import java.io.IOException;

import com.google.api.client.googleapis.extensions.servlet.notifications.NotificationServlet;
import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.util.store.DataStore;


/**
 * MyNotificationServlet.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
public class GoogleNotificationServlet extends NotificationServlet {

    public GoogleNotificationServlet(DataStore<StoredChannel> channelDataStore, WebHookService webHookService) throws IOException {
        super(channelDataStore);
        StoredChannel storedChannel = new StoredChannel(webHookService::processGoogleDriveChange);
        channelDataStore.set(System.getenv(WebHookService.GOOGCLE_CHANNEL_ID), storedChannel);
    }
}

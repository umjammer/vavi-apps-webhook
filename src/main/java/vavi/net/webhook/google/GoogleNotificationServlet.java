/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook.google;

import java.io.IOException;

import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.util.store.DataStore;


/**
 * MyNotificationServlet.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 * @see "https://console.developers.google.com/apis/credentials/domainverification?project=vavi-apps-fuse"
 */
public class GoogleNotificationServlet extends NotificationServlet {

    public GoogleNotificationServlet(DataStore<StoredChannel> channelDataStore) throws IOException {
        super(channelDataStore);
    }
}

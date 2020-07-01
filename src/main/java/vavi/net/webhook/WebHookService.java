/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook;

import java.io.IOException;
import java.io.Serializable;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.googleapis.notifications.UnparsedNotification;


/**
 * WebHookService.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
@Service
public class WebHookService implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(WebHookService.class);

    static final String GOOGCLE_CHANNEL_ID = "GOOGCLE_CHANNEL_ID";

    public void processGoogleDriveChange(StoredChannel storedChannel, UnparsedNotification notification) {
LOG.info("storedChannel: " + storedChannel);
LOG.info("notification: " + notification);
        try {
            MyBeanFactory.getNotificationEndpointInternal().sendNotification(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(Session session, String message) {
LOG.info("session: " + session);
LOG.info("message: " + message);
    }
}

/* */

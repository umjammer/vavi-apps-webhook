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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.googleapis.notifications.UnparsedNotification;
import com.google.api.client.util.store.DataStore;


/**
 * WebHookService.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
@Service
public class WebHookService implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(WebHookService.class);

    static final String VAVI_APPS_WEBHOOK_SECRET = "VAVI_APPS_WEBHOOK_SECRET";

    void processGoogleDriveChange(StoredChannel storedChannel, UnparsedNotification notification) {
LOG.info("storedChannel: " + storedChannel);
LOG.info("notification: " + notification);
        try {
            // this method will be serialized, so using wired way like followings
            MyBeanFactory.getNotificationEndpointInternal().sendNotification(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(Session session, String message) throws IOException {
LOG.info("session: " + session);
LOG.info("message: " + message);
        String[] args = message.split("\\s");
LOG.info("args: " + args);
        String command = args[0];
LOG.info("command: " + command);
        switch (command) {
        case "GOOGLE_DRIVE_CHANGE":
            processGoogleDriveChange(args[1]);
            break;
        }
    }

    @Autowired
    transient DataStore<StoredChannel> channelDataStore;

    /** */
    void processGoogleDriveChange(String channelId) throws IOException {
        StoredChannel storedChannel = new StoredChannel(this::processGoogleDriveChange);
        channelDataStore.set(channelId, storedChannel);
LOG.info("id added: " + channelId);
    }
}

/* */

/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook;

import java.io.IOException;
import java.io.Serializable;

import jakarta.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.googleapis.notifications.UnparsedNotification;
import com.google.api.client.util.store.DataStore;

import vavi.net.webhook.box.BoxNotificationEndpoint;
import vavi.net.webhook.dropbox.DropBoxNotificationEndpoint;
import vavi.net.webhook.google.GoogleNotificationServlet;
import vavi.net.webhook.microsoft.MicrosoftNotificationEndpoint;
import vavi.net.webhook.onedrive.OneDriveNotificationEndpoint;


/**
 * WebHookService.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
@Service
public class WebHookService implements Serializable {

    private transient static final Logger LOG = LoggerFactory.getLogger(WebHookService.class);

    public transient static final String VAVI_APPS_WEBHOOK_SECRET = "VAVI_APPS_WEBHOOK_SECRET";
    public transient static final String DROPBOX_CLIENT_SECRET = "DROPBOX_CLIENT_SECRET";
    public transient static final String BOX_WEBHOOK_PRIMARY_KEY = "BOX_WEBHOOK_PRIMARY_KEY";

    /** */
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

    /** using transient because of this class will be serialized */
    @Autowired
    transient DataStore<StoredChannel> channelDataStore;

    /** */
    public void processGoogleDriveChange(String channelId) throws IOException {
        StoredChannel storedChannel = new StoredChannel(this::processGoogleDriveChange);
        channelDataStore.set(channelId, storedChannel);
LOG.info("id added: " + channelId);
    }

    /** called from {@link GoogleNotificationServlet} */
    private void processGoogleDriveChange(StoredChannel storedChannel, UnparsedNotification notification) {
LOG.info("storedChannel: " + storedChannel);
LOG.info("notification: " + notification);
        try {
            // this method will be serialized, so using wired way like followings
            MyBeanFactory.getNotificationEndpointInternal().sendNotification(notification);
        } catch (IOException e) {
            LOG.error(e.toString(), e);
        }
    }

    @Autowired
    transient DropBoxNotificationEndpoint dropBoxNotificationEndpoint;

    /** */
    public void processDropBoxChange(String notification) throws IOException {
LOG.info("dropbox: " + notification);
        dropBoxNotificationEndpoint.sendNotification(notification);
    }

    @Autowired
    transient BoxNotificationEndpoint boxNotificationEndpoint;

    /** */
    public void processBoxChange(String notification) throws IOException {
LOG.info("box: " + notification);
        boxNotificationEndpoint.sendNotification(notification);
    }

    @Autowired
    transient OneDriveNotificationEndpoint oneDriveNotificationEndpoint;

    /** */
    public void processOneDriveChange(String notification) throws IOException {
LOG.info("onedrive: " + notification);
        oneDriveNotificationEndpoint.sendNotification(notification);
    }

    @Autowired
    transient MicrosoftNotificationEndpoint microsoftNotificationEndpoint;

    /** */
    public void processMicrosoftChange(String notification) throws IOException {
LOG.info("microsoft: " + notification);
        microsoftNotificationEndpoint.sendNotification(notification);
    }
}

/* */

/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    private static final Logger LOG = LoggerFactory.getLogger(GoogleNotificationServlet.class);

    public GoogleNotificationServlet(DataStore<StoredChannel> channelDataStore) throws IOException {
        super(channelDataStore);
    }

    @Autowired
    DataStore<StoredChannel> channelDataStore;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        super.doPost(req, resp);
channelDataStore.keySet().forEach(k -> { try { LOG.debug(k + ": " + channelDataStore.get(k)); } catch (IOException e) {}});
    }
}

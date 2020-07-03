
package vavi.net.webhook.onedrive;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ServerEndpoint(value = "/ws/onedrive",
                configurator = CustomSpringConfigurator.class)
public class OneDriveNotificationEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(OneDriveNotificationEndpoint.class);

    private WebHookService service;

    @Autowired
    public OneDriveNotificationEndpoint(WebHookService service) {
        this.service = service;
    }

    Session session;

    @OnOpen
    public void onOpen(Session session) {
LOG.info("OPEN: " + session);
        this.session = session;
    }

    @OnMessage
    public void handleMessage(Session session, String message) throws IOException {
LOG.info("MESSAGE: " + message);
        service.handle(session, message);
    }

    @OnClose
    public void onClose(Session session) {
LOG.info("CLOSE");
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();;
    }

    public void sendNotification(String notification) throws IOException {
        if (session != null) {
            session.getAsyncRemote().sendText(notification);
        } else {
            LOG.warn("no session");
        }
    }
}


package vavi.net.webhook;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class NotificationEndpoint<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(NotificationEndpoint.class);

    protected WebHookService service;

    protected NotificationEndpoint(WebHookService service) {
        this.service = service;
    }

    // TODO maybe not thread safe
    private Session session;

    protected Session getSession() {
        return session;
    }

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
        t.printStackTrace();
    }

    public abstract void sendNotification(T notification) throws IOException;
}

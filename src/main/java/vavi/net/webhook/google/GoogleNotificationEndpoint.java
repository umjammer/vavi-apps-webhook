
package vavi.net.webhook.google;

import java.io.IOException;
import java.io.InputStream;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.googleapis.notifications.UnparsedNotification;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ServerEndpoint(value = "/ws/google",
                configurator = CustomSpringConfigurator.class,
                decoders = GoogleNotificationEndpoint.MyDecoder.class,
                encoders = GoogleNotificationEndpoint.MyEncoder.class)
public class GoogleNotificationEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleNotificationEndpoint.class);

    static Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return InputStream.class.equals(clazz);
        }
    }).create();

    public static class MyEncoder implements Encoder.Text<UnparsedNotification> {
        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public String encode(UnparsedNotification notification) throws EncodeException {
            return gson.toJson(notification);
        }

        @Override
        public void destroy() {
        }
    }

    public static class MyDecoder implements Decoder.Text<UnparsedNotification> {
        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public boolean willDecode(String s) {
            try {
                gson.fromJson(s, UnparsedNotification.class);
                return true;
            } catch (JsonSyntaxException e) {
                return false;
            }
        }

        @Override
        public UnparsedNotification decode(String s) throws DecodeException {
            try {
                return gson.fromJson(s, UnparsedNotification.class);
            } catch (JsonSyntaxException e) {
                throw new DecodeException(s, e.getMessage(), e);
            }
        }

        @Override
        public void destroy() {
        }
    }

    private WebHookService service;

    @Autowired
    public GoogleNotificationEndpoint(WebHookService service) {
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

    public void sendNotification(UnparsedNotification notification) throws IOException {
        if (session != null) {
            session.getAsyncRemote().sendObject(notification);
        } else {
            LOG.warn("no session");
        }
    }
}

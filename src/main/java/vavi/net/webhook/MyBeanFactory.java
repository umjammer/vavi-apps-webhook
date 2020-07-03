/*
 * https://stackoverflow.com/questions/12537851/accessing-spring-beans-in-static-method
 */

package vavi.net.webhook;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vavi.net.webhook.google.GoogleNotificationEndpoint;


/**
 * gather spring dependencies.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/05/08 umjammer initial version <br>
 */
@Service
public class MyBeanFactory implements InitializingBean {
    private static MyBeanFactory instance;

    @Autowired
    BeanFactory beanFactory;

    @Autowired
    GoogleNotificationEndpoint notificationEndpoint;

    private GoogleNotificationEndpoint getNotificationEndpoint() {
        return notificationEndpoint;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public static GoogleNotificationEndpoint getNotificationEndpointInternal() {
        return instance.getNotificationEndpoint();
    }
}
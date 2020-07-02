/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.webhook;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.MemoryDataStoreFactory;


/**
 * Config.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
@Configuration
public class Config {

    @Bean
    DataStore<StoredChannel> channelDataStore() throws IOException {
        return MemoryDataStoreFactory.getDefaultInstance().getDataStore("googcleChannel");
    }

    @Bean
    ServletRegistrationBean<GoogleNotificationServlet> googleNotificationServletRegistrationBean(
        @Autowired DataStore<StoredChannel> channelDataStore) throws IOException {
        GoogleNotificationServlet servlet = new GoogleNotificationServlet(channelDataStore);
        ServletRegistrationBean<GoogleNotificationServlet> bean = new ServletRegistrationBean<>(servlet);
        bean.addUrlMappings("/webhook/google/drive/change/*");
        return bean;
    }

    @Bean
    FilterRegistrationBean<RequestValidationFilter> requestValidationFilterRegistrationBean() {
        FilterRegistrationBean<RequestValidationFilter> bean = new FilterRegistrationBean<>(new RequestValidationFilter());
        bean.addUrlPatterns("/webhook/google/drive/change/*");
        return bean;
    }

}

/* */

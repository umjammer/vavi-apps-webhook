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

import vavi.net.webhook.box.BoxRequestValidationFilter;
import vavi.net.webhook.dropbox.DropBoxRequestValidationFilter;
import vavi.net.webhook.google.GoogleNotificationServlet;
import vavi.net.webhook.google.GoogleRequestValidationFilter;
import vavi.net.webhook.microsoft.MicrosoftRequestValidationFilter;
import vavi.net.webhook.onedrive.OneDriveRequestValidationFilter;


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
    FilterRegistrationBean<GoogleRequestValidationFilter> requestGoogleValidationFilterRegistrationBean() {
        FilterRegistrationBean<GoogleRequestValidationFilter> bean = new FilterRegistrationBean<>(new GoogleRequestValidationFilter());
        bean.addUrlPatterns("/webhook/google/drive/change/*");
        return bean;
    }

    @Bean
    FilterRegistrationBean<DropBoxRequestValidationFilter> requestDropBoxValidationFilterRegistrationBean() {
        FilterRegistrationBean<DropBoxRequestValidationFilter> bean = new FilterRegistrationBean<>(new DropBoxRequestValidationFilter());
        bean.addUrlPatterns("/webhook/dropbox");
        return bean;
    }

    @Bean
    FilterRegistrationBean<BoxRequestValidationFilter> requestBoxValidationFilterRegistrationBean() {
        FilterRegistrationBean<BoxRequestValidationFilter> bean = new FilterRegistrationBean<>(new BoxRequestValidationFilter());
        bean.addUrlPatterns("/webhook/box");
        return bean;
    }

    @Bean
    FilterRegistrationBean<OneDriveRequestValidationFilter> requestOneDriveValidationFilterRegistrationBean() {
        FilterRegistrationBean<OneDriveRequestValidationFilter> bean = new FilterRegistrationBean<>(new OneDriveRequestValidationFilter());
        bean.addUrlPatterns("/webhook/onedrive");
        return bean;
    }

    @Bean
    FilterRegistrationBean<MicrosoftRequestValidationFilter> requestMicrosoftValidationFilterRegistrationBean() {
        FilterRegistrationBean<MicrosoftRequestValidationFilter> bean = new FilterRegistrationBean<>(new MicrosoftRequestValidationFilter());
        bean.addUrlPatterns("/webhook/microsoft");
        return bean;
    }
}

/* */

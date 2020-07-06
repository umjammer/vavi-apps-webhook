/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vavi.net.webhook;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;


@Controller
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    WebHookService webHookService;

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @PostMapping("/webhook/dropbox")
    @ResponseBody
    String dropboxWebhook(@RequestBody String notification) throws IOException {
        webHookService.processDropBoxChange(notification);
        return "";
    }

    @PostMapping("/webhook/box")
    @ResponseBody
    String boxWebhook(@RequestBody String notification) throws IOException {
        webHookService.processBoxChange(notification);
        return "";
    }

    @PostMapping("/webhook/onedrive")
    @ResponseBody
    String onedriveWebhook(@RequestBody String notification) throws IOException {
        webHookService.processOneDriveChange(notification);
        return "";
    }

    @PostMapping("/webhook/microsoft")
    @ResponseBody
    String microsoftWebhook(@RequestBody String notification) throws IOException {
        webHookService.processMicrosoftChange(notification);
        return "";
    }

    static Gson gson = new Gson();

    @PostMapping("/webhook/v1box")
    @ResponseBody
    String v1boxWebhook(@RequestParam Map<String, String> allRequestParams) throws IOException {
        webHookService.processBoxChange(gson.toJson(allRequestParams));
        return "";
    }

}

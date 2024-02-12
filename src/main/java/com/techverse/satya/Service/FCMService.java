package com.techverse.satya.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techverse.satya.Model.PushNotificationRequest;
/*
@Service
public class FCMService {
    private Logger logger = LoggerFactory.getLogger(FCMService.class);


    public void sendMessageToToken(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response+ " msg "+jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }


    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic).build()).build();
    }
    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }
    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        Notification notification = Notification.builder()
                                        .setTitle(request.getTitle())
                                        .setBody(request.getBody())
                                        .build();
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(notification);
    }
}*/
@Service
public class FCMService {

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAekAU7bQ:APA91bFRV6cjIyxlNrZY8xn_PEG1lnSIfmBweeB8VuLbA59g62p3pPljbhV4ZgNM825BlfWZ2Q-Fj_ZJ2e2hFXInp8kRgUPeykvXgw-YpQBeDvP6TVOWK8dEpKU2cPEUzc_soxiYxBiJ"; // Replace with your server key

    public ResponseEntity<String> sendPushNotification1(String deviceToken, String title, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + SERVER_KEY);

        String payload = "{"
                + "\"to\":\"" + deviceToken + "\","
                + "\"notification\":{"
                + "\"title\":\"" + title + "\","
                + "\"body\":\"" + message + "\""
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(FCM_API, request, String.class);
    }
    public ResponseEntity<String> sendPushNotification(String deviceToken, String title, String message,String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + SERVER_KEY);

        String payload = "{"
                + "\"to\":\"" + deviceToken + "\","
                + "\"notification\":{"
                + "\"title\":\"" + title + "\","
                + "\"body\":\"" + message + "\","
                + "\"type\":\"" + type + "\""
                + "}"
                + "}";
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(FCM_API, request, String.class);
    }
}
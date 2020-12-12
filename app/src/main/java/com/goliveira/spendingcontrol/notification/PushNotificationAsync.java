package com.goliveira.spendingcontrol.notification;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PushNotificationAsync implements Runnable {

    private String buddyId;
    private String Message;

    public PushNotificationAsync(String buddyId, String Message){
        this.buddyId = buddyId;
        this.Message = Message;
    }

    @Override
    public void run() {
        PushNotificationService pushService = new PushNotificationService();

        pushService.NotifyBuddy(buddyId, Message);
    }
}

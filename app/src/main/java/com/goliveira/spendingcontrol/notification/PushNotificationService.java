package com.goliveira.spendingcontrol.notification;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PushNotificationService {
    private static final String API_KEY = "NWY5N2ZhZWMtYmE1Mi00MWM1LTllNzItZDA2OTliZDk0ZWI4";
    private static final String APP_ID = "3bb7570d-33c4-436a-bd6e-1561c6252b0b";
    private static final String URL_ENDPOINT = "https://onesignal.com/api/v1/notifications";

    public PushNotificationService(){

    }

    public void NotifyBuddy(String BuddyId, String Message){
        try {
            String jsonResponse;

            URL url = new URL(URL_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + API_KEY);
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"" + APP_ID + "\","
                    +   "\"include_external_user_ids\": [\"" + BuddyId + "\"],"
                    +   "\"channel_for_external_user_ids\": \"push\","
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"" + Message + "\"}"
                    + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}

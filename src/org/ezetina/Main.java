package org.ezetina;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.logging.Logger;


public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void CloudFunction() {

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                            new FileInputStream("resources/credentials/credentials.json"))
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/chat.bot"));
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
            HangoutsChat chatService = new HangoutsChat.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    requestInitializer)
                    .setApplicationName("CF to chat")
                    .build();

            String roomId = "";
            String messageText = "Hello World!";

            Message message = new Message();
            message.setText(messageText);

            HangoutsChat.Spaces.Messages.Create request =
                    chatService.spaces().messages().create(roomId, message);
            Message response = request.execute();
            LOGGER.info("Message sent: " + message.getText());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
package org.ezetina;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.pubsub.v1.PubsubMessage;

import java.util.logging.Logger;

public class PubSubToChatFunction implements BackgroundFunction<PubsubMessage> {

    private static final Logger LOGGER = Logger.getLogger(PubSubToChatFunction.class.getName());
    private static final String ROOM_ID = System.getenv("ROOM_ID");

    @Override
    public void accept(PubsubMessage pubsubMessage, Context context) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        try {
            HangoutsChat chatService = new HangoutsChat.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    requestInitializer)
                    .setApplicationName("MyApp")
                    .build();

            String messageTxt = pubsubMessage.getData().toStringUtf8();
            Message chatMessage = new Message();
            chatMessage.setText(messageTxt);

            chatService.spaces().messages().create(ROOM_ID, chatMessage).execute();
            LOGGER.info("Message sent to Chat: " + messageTxt);
        } catch (Exception e) {
            LOGGER.info("Error:" + e.getMessage());
        }
    }
}
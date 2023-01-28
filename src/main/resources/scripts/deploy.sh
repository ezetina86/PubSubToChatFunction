#!/bin/bash

# Set the project ID and function name
PROJECT_ID=ezetina-gcp-project
FUNCTION_NAME=pubsub-to-chat-function
PUBSUB_TOPIC=chat-topic
REGION=us-east4

# Build the function
mvn clean package

# Deploy the function
gcloud functions deploy $FUNCTION_NAME \
  --gen2
  --project $PROJECT_ID \
  --runtime java11 \
  --entry-point org.ezetina.PubSubToChatFunction \
  --trigger-topic $PUBSUB_TOPIC \
  --region $REGION \
  --set-env-vars ROOM_ID=https://chat.google.com/room/AAAAsEj3_JE \
  --allow-unauthenticated

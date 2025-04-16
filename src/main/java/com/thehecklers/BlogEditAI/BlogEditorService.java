package com.thehecklers.BlogEditAI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

public class BlogEditorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ChatClient client;

    public BlogEditorService(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    
}

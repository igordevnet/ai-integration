package com.springintegrations.multimodelagent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient(
            OpenAiChatModel openAiChatModel,
            SimpleLoggerAdvisor simpleLoggerAdvisor
    ) {
        return ChatClient
                .builder(openAiChatModel)
                .defaultAdvisors(simpleLoggerAdvisor)
                .build();
    }

    @Bean
    public ChatClient ollamaChatClient(
            OllamaChatModel ollamaChatModel,
            SimpleLoggerAdvisor simpleLoggerAdvisor
    ) {
        ChatClient.Builder chatBuilder = ChatClient.builder(ollamaChatModel);
        return chatBuilder
                .defaultSystem("You are an staff Java engineer.")
                .defaultAdvisors(simpleLoggerAdvisor)
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }
}

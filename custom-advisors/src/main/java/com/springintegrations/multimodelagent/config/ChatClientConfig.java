package com.springintegrations.multimodelagent.config;

import com.springintegrations.multimodelagent.advisors.TokenUsageAuditAdvisor;
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
            SimpleLoggerAdvisor simpleLoggerAdvisor,
            TokenUsageAuditAdvisor tokenUsageAuditAdvisor
    ) {
        return ChatClient
                .builder(openAiChatModel)
                .defaultAdvisors(
                        simpleLoggerAdvisor,
                        tokenUsageAuditAdvisor
                )
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

    @Bean
    public TokenUsageAuditAdvisor tokenUsageAuditAdvisor() {
        return new TokenUsageAuditAdvisor();
    }
}

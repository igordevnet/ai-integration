package com.springintegrations.multimodelagent.config;

import com.springintegrations.multimodelagent.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Value("${custom.openai.model}")
    private String openAiModel;

    @Value("${custom.openai.temperature}")
    private double openAiTemperature;

    @Value("${custom.openai.maxTokens}")
    private int openAiMaxTokens;

    @Value("${custom.openai.frequencyPenalty}")
    private double openAiFrequency;

    @Value("${custom.openai.presencePenalty}")
    private double openAiPresence;

    @Value("${custom.openai.topP}")
    private double openAiTopP;

    @Value("${custom.openai.topK}")
    private int openAiTopK;

    @Value("${custom.openai.stopSequences}")
    private List<String> openAiStopSequences;

    @Bean
    public ChatClient openAiChatClient(
            OpenAiChatModel openAiChatModel,
            SimpleLoggerAdvisor simpleLoggerAdvisor,
            TokenUsageAuditAdvisor tokenUsageAuditAdvisor
    ) {
        var options = OpenAiChatOptions.builder()
                .model(openAiModel)
                .temperature(openAiTemperature)
                .maxCompletionTokens(openAiMaxTokens)
                .frequencyPenalty(openAiFrequency)
                .presencePenalty(openAiPresence)
                .topP(openAiTopP)
                .topK(openAiTopK)
                .stopSequences(openAiStopSequences);

        return ChatClient
                .builder(openAiChatModel)
                .defaultOptions(options)
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

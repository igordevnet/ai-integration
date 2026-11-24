package com.springintegrations.multimodelagent.config;

import com.springintegrations.multimodelagent.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
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
            OpenAiChatModel openAiChatModel
    ) {
        return ChatClient
                .builder(openAiChatModel)
                .defaultOptions(buildOptions())
                .defaultAdvisors(buildAdvisors())
                .build();
    }

    @Bean
    public ChatClient ollamaChatClient(
            OllamaChatModel ollamaChatModel
    ) {
        ChatClient.Builder chatBuilder = ChatClient.builder(ollamaChatModel);
        return chatBuilder
                .defaultSystem("You are an staff Java engineer.")
                .build();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    public ChatMemory buildChatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(100)
                .build();
    }

    private OpenAiChatOptions.Builder buildOptions() {
        return OpenAiChatOptions.builder()
                .model(openAiModel)
                .temperature(openAiTemperature)
                .maxCompletionTokens(openAiMaxTokens)
                .frequencyPenalty(openAiFrequency)
                .presencePenalty(openAiPresence)
                .topP(openAiTopP)
                .topK(openAiTopK)
                .stopSequences(openAiStopSequences);
    }

    private List<Advisor> buildAdvisors() {
        var advisors = new ArrayList<Advisor>();
        advisors.add(simpleLoggerAdvisor());
        advisors.add(tokenUsageAuditAdvisor());

        return advisors;
    }

    private SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    private TokenUsageAuditAdvisor tokenUsageAuditAdvisor() {
        return new TokenUsageAuditAdvisor();
    }
}

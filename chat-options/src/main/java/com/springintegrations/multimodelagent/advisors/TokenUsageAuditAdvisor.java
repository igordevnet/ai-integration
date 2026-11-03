package com.springintegrations.multimodelagent.advisors;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
public class TokenUsageAuditAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(
            @NonNull ChatClientRequest chatClientRequest,
            CallAdvisorChain callAdvisorChain
    ) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();

        if (chatResponse == null) {
            return chatClientResponse;
        }

        var metadata = chatResponse.getMetadata();

        if(metadata != null) {
            Usage usage = metadata.getUsage();

            if(usage != null) {
                log.info("Token usage details: {}.", usage.toString());
            }
        }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

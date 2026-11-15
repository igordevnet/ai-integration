package com.springintegrations.multimodelagent.domains;

import java.time.LocalDateTime;

public record Audit(
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        LocalDateTime versionAsTimestamp
) {
}

package com.springintegrations.multimodelagent.controllers.dtos;

import com.springintegrations.multimodelagent.domains.Audit;

public record StockRecordResponse(
        String itemId,
        long quantity,
        boolean isLocked,
        String taskId,
        String containerId,
        Audit Audit
) {
}

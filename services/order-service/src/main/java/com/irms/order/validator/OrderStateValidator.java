package com.irms.order.validator;

import com.irms.order.domain.OrderStatus;
import com.irms.order.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class OrderStateValidator {

    public void validateTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }

        boolean isValid = switch (currentStatus) {
            case DRAFT -> newStatus == OrderStatus.PENDING || newStatus == OrderStatus.CANCELLED;
            case PENDING -> newStatus == OrderStatus.COOKING || newStatus == OrderStatus.CANCELLED;
            case COOKING -> newStatus == OrderStatus.READY_TO_SERVE; // Cannot cancel directly from COOKING
            case READY_TO_SERVE -> newStatus == OrderStatus.SERVED;
            case SERVED -> newStatus == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false; // Terminal states
        };

        if (!isValid) {
            throw new InvalidStateTransitionException(
                    String.format("Cannot transition order from %s to %s", currentStatus, newStatus)
            );
        }
    }
}

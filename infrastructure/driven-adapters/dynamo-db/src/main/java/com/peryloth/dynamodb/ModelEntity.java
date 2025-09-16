package com.peryloth.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/**
 * Entity que representa la tabla LoanCounters en DynamoDB.
 */
@DynamoDbBean
public class ModelEntity {

    private String counterId;    // Partition Key
    private Long count;          // total de préstamos
    private Double totalAmount;  // monto acumulado

    public ModelEntity() {
    }

    public ModelEntity(String counterId, Long count, Double totalAmount) {
        this.counterId = counterId;
        this.count = count;
        this.totalAmount = totalAmount;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("counterId")
    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    @DynamoDbAttribute("count")
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @DynamoDbAttribute("totalAmount")
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

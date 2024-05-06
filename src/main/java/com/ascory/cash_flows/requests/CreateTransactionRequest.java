package com.ascory.cash_flows.requests;

import com.ascory.cash_flows.models.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {
    private Double amount;
    private TransactionCategory transactionCategory;
    private String transactionDescription;
    private String transactionName;
}

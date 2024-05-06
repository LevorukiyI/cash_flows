package com.ascory.cash_flows.responses;

import com.ascory.cash_flows.models.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTransactionResponse {
    private Long transactionId;
    private double amount;
    private Date transactionDate;
    private Long transactionPerformerId;
    private String transactionName;
    private String transactionDescription;
    private TransactionCategory transactionCategory;
}

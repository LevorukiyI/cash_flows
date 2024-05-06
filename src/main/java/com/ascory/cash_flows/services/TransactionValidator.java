package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.TransactionCategory;
import com.ascory.cash_flows.requests.CreateTransactionRequest;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {
    public void validateTransactionName(String transactionName){
        if(transactionName.isBlank()){
            throw new IllegalArgumentException("transactionName can't be blank");
        }
    }

    public void validateTransactionDescription(String transactionDescription){
         if(transactionDescription.isBlank()){
             throw new IllegalArgumentException("transactionDescription can't be blank");
         }
    }

    public void validateTransactionCategory(TransactionCategory transactionCategory){
        if(transactionCategory == null){
            throw new IllegalArgumentException("transactionCategory can't be null.");
        }
    }

    public void validateCreateTransactionRequest(CreateTransactionRequest createTransactionRequest){
        validateTransactionCategory(createTransactionRequest.getTransactionCategory());
        validateTransactionName(createTransactionRequest.getTransactionName());
        validateTransactionDescription(createTransactionRequest.getTransactionDescription());
    }
}

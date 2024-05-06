package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.TransactionEntity;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.TransactionRepository;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.requests.CreateTransactionRequest;
import com.ascory.cash_flows.responses.GetAllTransactionsResponse;
import com.ascory.cash_flows.responses.GetTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final UserRepository userRepository;

    public void createTransaction(CreateTransactionRequest createTransactionRequest,
                                  Authentication authentication){
        if (authentication == null) {
            throw new AccessDeniedException("Access is denied. User is unauthenticated or did not provide a JWT token.");
        }
        transactionValidator.validateCreateTransactionRequest(createTransactionRequest);
        User user = userRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(()->new UsernameNotFoundException("User with such authentication were not found"));
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(createTransactionRequest.getAmount())
                .transactionPerformer(user)
                .transactionDate(new Date())
                .transactionName(createTransactionRequest.getTransactionName())
                .transactionDescription(createTransactionRequest.getTransactionDescription())
                .transactionCategory(createTransactionRequest.getTransactionCategory())
                .build();
        transactionRepository.save(transactionEntity);
    }

    public ResponseEntity<?> getAllTransactions(){
        List<TransactionEntity> transactionEntities = transactionRepository.findAll();
        ArrayList<GetTransactionResponse> getTransactionResponses = new ArrayList<>();
        for(TransactionEntity transactionEntity: transactionEntities){
            GetTransactionResponse getTransactionResponse =
                    GetTransactionResponse.builder()
                            .transactionId(transactionEntity.getId())
                            .amount(transactionEntity.getAmount())
                            .transactionDate(transactionEntity.getTransactionDate())
                            .transactionPerformerId(transactionEntity.getTransactionPerformer().getId())
                            .transactionName(transactionEntity.getTransactionName())
                            .transactionDescription(transactionEntity.getTransactionDescription())
                            .transactionCategory(transactionEntity.getTransactionCategory())
                            .build();
            getTransactionResponses.add(getTransactionResponse);
        }
        GetAllTransactionsResponse getAllTransactionsResponse =
                GetAllTransactionsResponse.builder()
                        .transactionResponses(getTransactionResponses)
                        .build();
        return ResponseEntity.ok(getAllTransactionsResponse);
    }
}

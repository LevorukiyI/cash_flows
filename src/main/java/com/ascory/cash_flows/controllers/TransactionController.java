package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.CreateTransactionRequest;
import com.ascory.cash_flows.services.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionsService transactionsService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(
            @RequestBody CreateTransactionRequest createTransactionRequest,
            Authentication authentication){
        transactionsService.createTransaction(createTransactionRequest, authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllTransactions(){
        return transactionsService.getAllTransactions();
    }

    @GetMapping("/get-transaction")
    public ResponseEntity<?> getTransaction(@RequestParam Long transactionId){
        return transactionsService.getTransaction(transactionId);
    }
}

package com.ascory.cash_flows.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private double amount;

    @Column
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User transactionPerformer;

    @Column
    private String transactionDescription;

    @Column
    private String transactionName;

    @Enumerated(EnumType.STRING)
    @Column
    private TransactionCategory transactionCategory;
}

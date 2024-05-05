package com.ascory.cash_flows.models;

import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_pass_verification_token_entity")
public class EmailPassVerificationTokenEntity {
    @Id
    private String email;

    @Column(unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "register_request_id")
    private EmailPassRegisterRequestEntity registerRequestEntity;

    @Column
    private Long userId;
}

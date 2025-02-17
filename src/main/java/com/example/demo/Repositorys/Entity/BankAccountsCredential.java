package com.example.demo.Repositorys.Entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "bank_accounts_credentials", indexes = {
        @Index(name = "credentials_id", columnList = "credentials_id")
})
public class BankAccountsCredential {
    @EmbeddedId
    private BankAccountsCredentialId id;

    @MapsId("bankAccountId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @MapsId("credentialsId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "credentials_id", nullable = false)
    private Credential credentials;

}
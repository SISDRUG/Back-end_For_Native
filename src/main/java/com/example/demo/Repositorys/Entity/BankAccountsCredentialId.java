package com.example.demo.Repositorys.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BankAccountsCredentialId implements Serializable {
    private static final long serialVersionUID = -4276180749218155987L;
    @NotNull
    @Column(name = "bank_account_id", nullable = false)
    private Long bankAccountId;

    @NotNull
    @Column(name = "credentials_id", nullable = false)
    private Long credentialsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BankAccountsCredentialId entity = (BankAccountsCredentialId) o;
        return Objects.equals(this.bankAccountId, entity.bankAccountId) &&
                Objects.equals(this.credentialsId, entity.credentialsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankAccountId, credentialsId);
    }

}
package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Bank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    @Override
    Optional<Bank> findById(Long bankId);
}

package com.zoo.account;

import com.zoo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByEmail(String email);
    Account findByEmail(String email);

    Account findByName(String name);
}

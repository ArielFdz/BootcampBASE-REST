package com.bancobase.bootcamp.services;

import com.bancobase.bootcamp.dto.AccountDTO;
import com.bancobase.bootcamp.repositories.AccountRepository;
import com.bancobase.bootcamp.schemas.*;
import com.bancobase.bootcamp.utils.Utils;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Optional<AccountSchema> account = accountRepository
                .findById(accountNumber);

        return account.map(AccountDTO::getFromSchema).orElse(null);
    }

    public List<AccountDTO> getAllAccountsByCustomerId(Long customerId) {
        return accountRepository
                .findByCustomerCustomerId(customerId)
                .stream()
                .map(AccountDTO::getFromSchema)
                .toList();
    }

    public List<AccountSchema> createAccount(CustomerSchema customer) {
        AccountSchema account = new AccountSchema();

        account.setAccountNumber(Utils.generateAccountNumber());
        account.setCustomer(customer);

        AccountSchema savedAccount = accountRepository.save(account);

        return List.of(savedAccount);
    }
}

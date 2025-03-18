package com.thgeek.banking.transaction.controller;

import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction Controller
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/17 23:20
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<?> query(TransactionQuery query) {
        return ResponseEntity.ok(transactionService.query(query));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateTransactionReq req) {
        return ResponseEntity.ok(transactionService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateTransactionReq req) {
        return ResponseEntity.ok(transactionService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}

package com.authen.sec.controller;

import com.authen.sec.model.blockchain.Block;
import com.authen.sec.model.blockchain.Blockchain;
import com.authen.sec.model.blockchain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blockchain")
@PreAuthorize("isAuthenticated()")
public class BlockchainController {

    private final Blockchain blockchain;

    @Autowired
    public BlockchainController(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @GetMapping("/status")
    public ResponseEntity<List<Block>> getBlockchainStatus() {
        return ResponseEntity.ok(blockchain.getChain());
    }

    @GetMapping("/latest")
    public ResponseEntity<Block> getLatestBlock() {
        return ResponseEntity.ok(blockchain.getLatestBlock());
    }

    @GetMapping("/block/{index}")
    public ResponseEntity<Block> getBlockByIndex(@PathVariable int index) {
        List<Block> chain = blockchain.getChain();
        if (index >= 0 && index < chain.size()) {
            return ResponseEntity.ok(chain.get(index));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/difficulty")
    public ResponseEntity<Integer> getDifficulty() {
        return ResponseEntity.ok(blockchain.getDifficulty());
    }

    @PostMapping("/difficulty")
    public ResponseEntity<String> setDifficulty(@RequestParam int difficulty) {
        blockchain.setDifficulty(difficulty);
        return ResponseEntity.ok("Difficulty updated to " + difficulty);
    }

    @GetMapping("/details")
    public ResponseEntity<List<BlockDetails>> getBlockchainDetails() {
        List<BlockDetails> blockDetails = blockchain.getChain().stream()
            .map(block -> new BlockDetails(
                block.getPreviousHash(),
                block.getHash(),
                block.getTransactions(),
                block.getData()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(blockDetails);
    }

 // Inner class to represent block details
 private static class BlockDetails {
    private final String previousHash;
    private final String hash;
    private final List<Transaction> transactions;
    private final String data;

    public BlockDetails(String previousHash, String hash, List<Transaction> transactions, String data) {
        this.previousHash = previousHash;
        this.hash = hash;
        this.transactions = transactions;
        this.data = data;
    }

        // Getters
    @SuppressWarnings("unused")
    public String getPreviousHash() { return previousHash; }
    @SuppressWarnings("unused")
    public String getHash() { return hash; }
    @SuppressWarnings("unused")
    public List<Transaction> getTransactions() { return transactions; }
    @SuppressWarnings("unused")
    public String getData() { return data; }
    }
}
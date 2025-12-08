package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.PostingRuleRequest;
import com.atharva.erp_telecom.dto.PostingRuleResponse;
import com.atharva.erp_telecom.service.PostingRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config/finance/accounting/posting-rules")
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
public class PostingRuleController {

    private final PostingRuleService service;

    public PostingRuleController(PostingRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PostingRuleResponse> create(@RequestBody PostingRuleRequest dto) {
        return ResponseEntity.ok(service.createPostingRule(dto));
    }

    @GetMapping
    public ResponseEntity<List<PostingRuleResponse>> getAll() {
        return ResponseEntity.ok(service.getAllPostingRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostingRuleResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostingRuleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostingRuleResponse> update(
            @PathVariable Long id,
            @RequestBody PostingRuleRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Order Deleted for Id: "+id);
    }
}

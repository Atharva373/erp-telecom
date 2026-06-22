package com.atharva.erp_telecom.accounting.api;

import com.atharva.erp_telecom.accounting.dto.PostingRuleRequest;
import com.atharva.erp_telecom.accounting.dto.PostingRuleResponse;
import com.atharva.erp_telecom.accounting.service.PostingRuleService;
import org.springframework.http.HttpStatus;
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
        PostingRuleResponse created = service.createPostingRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
    public ResponseEntity<PostingRuleResponse> update(@PathVariable Long id, @RequestBody PostingRuleRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Order Deleted for Id: "+id);
    }
}

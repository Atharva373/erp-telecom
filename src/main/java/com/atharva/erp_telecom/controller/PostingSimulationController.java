package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.accounting.PostingContext;
import com.atharva.erp_telecom.entity.accounting.JournalEntry;
import com.atharva.erp_telecom.service.accounting.PostingEngine;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting/posting")
@PreAuthorize("hasAnyRole('ADMIN','FINANCE')")
public class PostingSimulationController {

    private final PostingEngine postingEngine;

    public PostingSimulationController(PostingEngine postingEngine) {
        this.postingEngine = postingEngine;
    }

    @PostMapping("/simulate")
    public JournalEntry preview(@RequestBody PostingContext ctx) {
        System.out.println("Context:"+ctx);
        return postingEngine.simulate(ctx);
    }
}

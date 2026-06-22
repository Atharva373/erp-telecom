package com.atharva.erp_telecom.accounting.api.simulation;

import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.engine.PostingEngine;
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
    public JournalEntryEntity preview(@RequestBody PostingContext ctx) {
        System.out.println("Context:"+ctx);
        return postingEngine.simulate(ctx);
    }
}

package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.domain.PostingRule;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.engine.resolver.PostingRuleResolver;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingRuleRepository;
import com.atharva.erp_telecom.testfactory.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/*
@ExtendWith(MockitoExtension.class)

What it does:
Enables Mockito in JUnit 5.

Without it:
@Mock won’t work

@InjectMocks won’t work
Think of it as:
“Turn Mockito ON for this test”
 */
@ExtendWith(MockitoExtension.class)
class PostingRuleResolverTest {
    /*
        @Mock
        Creates a fake object instead of the real dependency.

        @Mock
        private PostingRuleRepository repository;

        Instead of connecting to DB, this becomes:
        repository = fake object

        You control its behavior using:
        when(repository.findApplicableRules(...)).thenReturn(...)
        So the test is predictable.
     */
    @Mock
    private PostingRuleRepository repository;

    @InjectMocks
    private PostingRuleResolver resolver;

//    @Test
//    void should_return_ar_invoice_rule_when_event_is_ar_invoice() {
//
//        // given
//        PostingContext context = TestDataFactory.arInvoiceContext();
//        PostingRule expectedRule = TestDataFactory.arInvoiceRule();
//
//        when(repository.findActiveRuleForCompany(
//                context.getEventType(),
//                Long.parseLong(context.getCompanyCode()),
//                context.getPostingDate()
//        )).thenReturn(Optional.of(expectedRule));
//
//        // when
//        PostingRule rule = resolver.resolve(context);
//
//        // then
//        assertThat(rule).isEqualTo(expectedRule);
//    }
}

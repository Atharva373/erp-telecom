package com.atharva.erp_telecom.unit.accounting;

import com.atharva.erp_telecom.accounting.dto.PostingContext;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.service.PostingRuleResolver;
import com.atharva.erp_telecom.testfactory.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class PostingRuleEntityResolverTest {
    private PostingRuleResolver resolver;

    @BeforeEach
    void setup() {
        resolver = new PostingRuleResolver();
    }

    @Test
    void should_resolve_ar_invoice_rule() {

        // given
        PostingContext ctx = TestDataFactory.arInvoiceContext();

        // when
        PostingRuleEntity rule = resolver.resolve(ctx);

        // then
        assertThat(rule).isNotNull();
        assertThat(rule.getRuleCode()).isEqualTo("AR_STANDARD_INVOICE");
    }
}

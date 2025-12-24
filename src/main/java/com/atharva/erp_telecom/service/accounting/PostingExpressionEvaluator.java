package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PostingExpressionEvaluator {

    // SPeL is an expression interpreter. It converts expressions in String format to parsed, mapped and operable Java objects.

    private final ExpressionParser parser = new SpelExpressionParser();

    public Object evaluate(String expression, PostingContext context) {
        if (expression == null || expression.isBlank()) return null;

        StandardEvaluationContext spelContext = new StandardEvaluationContext();
        spelContext.setVariables(context.getVariables());

        return parser.parseExpression(expression).getValue(spelContext);
    }

    public BigDecimal evaluateAmount(String expression, PostingContext context) {
        Object value = evaluate(expression, context);
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }

    public Boolean evaluateCondition(String expression, PostingContext context) {
        if (expression == null || expression.isBlank()) return true;
        Object result = evaluate(expression, context);
        return Boolean.TRUE.equals(result);
    }
}

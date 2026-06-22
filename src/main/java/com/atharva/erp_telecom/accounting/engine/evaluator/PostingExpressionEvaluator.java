package com.atharva.erp_telecom.accounting.engine.evaluator;

import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import java.math.BigDecimal;
import com.atharva.erp_telecom.accounting.engine.context.PostingVariableKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Functional class to evaluate and parse the SPeL expression from the PostingRule
 * and evaluate amounts and boolean expressions
 */

@Slf4j
@Component
public class PostingExpressionEvaluator {

    private final ExpressionParser parser = new SpelExpressionParser();
    public Object evaluate(String expression,PostingContext context) {

        if (expression == null || expression.isBlank()) return null;

        StandardEvaluationContext spelContext = new StandardEvaluationContext();

        for(Map.Entry<PostingVariableKey,Object> entry : context.getVariables().entrySet()) {
            spelContext.setVariable(entry.getKey().name(), entry.getValue());
        }

        Object result = parser.parseExpression(expression).getValue(spelContext);
        log.debug(
                "Evaluated expression={}, result={}",
                expression,
                result
        );
        return result;
    }

    public BigDecimal evaluateAmount(String expression,PostingContext context) {
        Object value = evaluate(expression,context);
        return value == null ? BigDecimal.ZERO: new BigDecimal(value.toString());
    }


    public boolean evaluateCondition(String expression,PostingContext context) {
        if (expression == null || expression.isBlank()) return true;
        Object result = evaluate(expression,context);
        return Boolean.TRUE.equals(result);
    }
}

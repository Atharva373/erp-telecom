package com.atharva.erp_telecom.accounting.engine.factory;

import com.atharva.erp_telecom.accounting.domain.event.AccountingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostingContextFactoryResolver {

    private final Map<Class<? extends AccountingEvent>,PostingContextFactory> factoryMap;

    public PostingContextFactoryResolver(List<PostingContextFactory> factories) {
        this.factoryMap = factories.stream()
                .collect(Collectors.toUnmodifiableMap(
                        PostingContextFactory::supports,
                        Function.identity(),
                        (existing, duplicate) -> {
                            throw new IllegalStateException("Duplicate PostingContextFactory found for event type: "+ existing.supports().getSimpleName());
                        }
                ));
        log.info(
                "Registered {} PostingContextFactories: {}",
                factoryMap.size(),
                factoryMap.keySet()
        );
    }

    public PostingContextFactory resolve(AccountingEvent event) {
        PostingContextFactory factory = factoryMap.get(event.getClass());
        if (factory == null) {
            throw new IllegalArgumentException("No PostingContextFactory registered for event: "+ event.getClass().getSimpleName());
        }
        return factory;
    }
}
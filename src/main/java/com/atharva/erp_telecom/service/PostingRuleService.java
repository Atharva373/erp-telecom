package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.PostingRuleRequest;
import com.atharva.erp_telecom.dto.PostingRuleResponse;
import com.atharva.erp_telecom.entity.PostingRule;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.repository.ChartOfAccountRepository;
import com.atharva.erp_telecom.repository.PostingRuleRepository;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostingRuleService {

    private final PostingRuleRepository postingRuleRepository;
    private final PostingRuleValidator postingRuleValidator;
    private final ChartOfAccountRepository chartOfAccountRepository;

    public PostingRuleService(PostingRuleRepository postingRuleRepository, PostingRuleValidator postingRuleValidator, ChartOfAccountRepository chartOfAccountRepository) {
        this.postingRuleRepository = postingRuleRepository;
        this.postingRuleValidator = postingRuleValidator;
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    /**
    *   Method: createPostingRule <br><br>
    *   Purpose: Create a new Posting Rule and return the response DTO.
    *   @param request Type {@code PostingRuleRequest} - Request DTO for a new Posting rule.
    *   @return {@code PostingRuleResponse} - Response DTO for the newly created rule.
    *   @author Atharva
    */


    public PostingRuleResponse createPostingRule(PostingRuleRequest request){
        PostingRule rule = EntityDtoMappers.mapPostingRuleRequestToPostingRuleEntity(request,chartOfAccountRepository);
        postingRuleValidator.validate(rule);
        PostingRule saved = postingRuleRepository.save(rule);
        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(saved);
    }


    public List<PostingRuleResponse> getAllPostingRules() {
        return postingRuleRepository.findAll()
                .stream()
                .map(EntityDtoMappers::mapPostingRuleToPostingRuleResponse)
                .toList();
    }


    public PostingRuleResponse getPostingRuleById(Long id) {
        PostingRule rule = postingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalPostingRuleException("Posting rule not found"));

        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(rule);
    }

    public PostingRuleResponse update(Long id, PostingRuleRequest request) {
        PostingRule existing = postingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalPostingRuleException("Posting rule not found"));

        PostingRule parsedRequest = EntityDtoMappers.mapPostingRuleRequestToPostingRuleEntity(request,chartOfAccountRepository);

        postingRuleValidator.validate(parsedRequest);

        PostingRule updated = postingRuleRepository.save(existing);

        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(updated);
    }

    public void delete(Long id) {
        postingRuleRepository.deleteById(id);
    }

}

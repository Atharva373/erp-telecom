package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingRuleRequest;
import com.atharva.erp_telecom.dto.accounting.PostingRuleResponse;
import com.atharva.erp_telecom.entity.accounting.PostingRule;
import com.atharva.erp_telecom.entity.accounting.PostingRuleLine;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.repository.accounting.ChartOfAccountRepository;
import com.atharva.erp_telecom.repository.finance.CompanyRepository;
import com.atharva.erp_telecom.repository.accounting.PostingRuleRepository;
import com.atharva.erp_telecom.utils.CrudUtils;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PostingRuleService {

    private final PostingRuleRepository postingRuleRepository;
    private final PostingRuleValidator postingRuleValidator;
    private final ChartOfAccountRepository chartOfAccountRepository;
    private final CompanyRepository companyRepository;


    public PostingRuleService(PostingRuleRepository postingRuleRepository, PostingRuleValidator postingRuleValidator, ChartOfAccountRepository chartOfAccountRepository, CompanyRepository companyRepository) {
        this.postingRuleRepository = postingRuleRepository;
        this.postingRuleValidator = postingRuleValidator;
        this.chartOfAccountRepository = chartOfAccountRepository;
        this.companyRepository = companyRepository;
    }

    /**
    *   Method: createPostingRule <br><br>
    *   Purpose: Create a new Posting Rule and return the response DTO.
    *   @param request Type {@code PostingRuleRequest} - Request DTO for a new Posting rule.
    *   @return {@code PostingRuleResponse} - Response DTO for the newly created rule.
    *   @author Atharva
    */

    @Transactional
    public PostingRuleResponse createPostingRule(PostingRuleRequest request){
        PostingRule rule = EntityDtoMappers.mapPostingRuleRequestToPostingRuleEntity(request,chartOfAccountRepository,companyRepository);

        if (rule.getLines() != null) {
            rule.getLines().forEach(line -> line.setPostingRule(rule));
        }
        postingRuleValidator.validate(rule);
        // Lesson: Always use modifiableList when using .sort() as the default Streams.toList() returns an unModifiableList which throws an error.
        rule.getLines().sort(Comparator.comparing(PostingRuleLine::getSortOrder));
        PostingRule saved = postingRuleRepository.save(rule);
        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(saved);
    }

    /**
     * Method to GET all the PostingRules.
     * @return {@code List<PostingRuleResponse>}
     */
    public List<PostingRuleResponse> getAllPostingRules() {
        return postingRuleRepository.findAll()
                .stream()
                .map(EntityDtoMappers::mapPostingRuleToPostingRuleResponse)
                .toList();
    }

    /**
     * Method to GET Posting Rules by Id.
     * @param id
     * @return PostingRuleResponse
     */
    public PostingRuleResponse getPostingRuleById(Long id) {
        PostingRule rule = postingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalPostingRuleException("Posting rule not found"));

        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(rule);
    }

    /**
     * Method to Update Posting Rules by Id.
     * @param id
     * @param request
     * @return PostingRuleResponse
     */
    @Transactional
    public PostingRuleResponse update(Long id, PostingRuleRequest request) {
        PostingRule existing = postingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalPostingRuleException("Posting rule not found"));

        PostingRule incoming = EntityDtoMappers.mapPostingRuleRequestToPostingRuleEntity(request,chartOfAccountRepository,companyRepository);

        postingRuleValidator.validate(incoming);

        CrudUtils.updateIfNotNull(existing::setPostingRuleCode,incoming.getPostingRuleCode());
        CrudUtils.updateIfNotNull(existing::setDescription,incoming.getDescription());
        CrudUtils.updateIfNotNull(existing::setEventType,incoming.getEventType());
        CrudUtils.updateIfNotNull(existing::setCompany,incoming.getCompany());
        CrudUtils.updateIfNotNull(existing::setEffectiveFrom,incoming.getEffectiveFrom());
        CrudUtils.updateIfNotNull(existing::setEffectiveTo,incoming.getEffectiveTo());
        CrudUtils.updateIfNotNull(existing::setActive,incoming.isActive());
        CrudUtils.updateIfNotNull(existing::setHeaderConditionExpression,incoming.getHeaderConditionExpression());

        // Replace Lines
        existing.getLines().clear();
        for (PostingRuleLine newLine : incoming.getLines()) {
            newLine.setPostingRule(existing);
            existing.getLines().add(newLine);
        }

        PostingRule saved = postingRuleRepository.save(existing);
        return EntityDtoMappers.mapPostingRuleToPostingRuleResponse(saved);
    }

    public void delete(Long id) {
        postingRuleRepository.deleteById(id);
    }

}

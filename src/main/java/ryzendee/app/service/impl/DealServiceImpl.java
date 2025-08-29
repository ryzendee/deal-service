package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.mapper.DealAppMapper;
import ryzendee.app.models.Deal;
import ryzendee.app.models.DealStatus;
import ryzendee.app.repository.DealRepository;
import ryzendee.app.repository.DealStatusRepository;
import ryzendee.app.service.DealService;
import ryzendee.app.util.exporter.DealDetailsExporter;
import ryzendee.app.util.exporter.ExportResult;
import ryzendee.app.util.specification.DealSpecificationBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_CACHE_MANAGER;
import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;
import static ryzendee.app.constants.CacheNameConstants.DEAL_CACHE;
import static ryzendee.app.constants.CacheNameConstants.DEAL_STATUS_METADATA_CACHE;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private static final String ROLE_BORROWER = "BORROWER";
    private static final String ROLE_WARRANTY = "WARRANITY";
    private static final String DRAFT_STATUS = "DRAFT";

    private final DealRepository dealRepository;
    private final DealStatusRepository dealStatusRepository;
    private final DealAppMapper dealAppMapper;
    private final DealDetailsExporter dealDetailsExporter;

    @Transactional
    @CacheEvict(
            value = DEAL_CACHE,
            key = "#request.id",
            condition = "#request.id != null",
            cacheManager = DEAL_CACHE_MANAGER
    )
    @Override
    public DealDetails saveOrUpdate(DealSaveRequest request) {
        Deal deal;

        if (request.id() != null) {
            deal = findById(request.id());
            dealAppMapper.updateFromRequest(request, deal);
        } else {
            deal = createDeal(request);
        }

        dealRepository.save(deal);

        return dealAppMapper.toDetails(deal);
    }

    @Transactional
    @CacheEvict(value = DEAL_STATUS_METADATA_CACHE, key = "'all'", cacheManager = DEAL_METADATA_CACHE_MANAGER)
    @Override
    public void changeDealStatus(DealStatusChangeRequest request) {
        DealStatus status = dealStatusRepository.findById(request.statusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status with given id does not exists"));
        Deal deal = findById(request.dealId());
        deal.setStatus(status);
        dealRepository.save(deal);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = DEAL_CACHE, key = "#id", cacheManager = DEAL_CACHE_MANAGER)
    @Override
    public DealDetails getDealById(UUID id) {
        Deal deal = dealRepository.findByIdWithGraph(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal with given id not found"));
        return dealAppMapper.toDetails(deal);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DealDetails> searchDeals(DealSearchFilter filter) {
        return findDealDetailsPage(filter);
    }

    @Transactional(readOnly = true)
    @Override
    public ExportResult exportDetails(DealSearchFilter filter) {
        Page<DealDetails> page = findDealDetailsPage(filter);
        return dealDetailsExporter.export(page.getContent());
    }

    private Deal findById(UUID id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal with given id not found"));
    }

    private Deal createDeal(DealSaveRequest request) {
        Deal newDeal = dealAppMapper.toModel(request);
        DealStatus status = dealStatusRepository.findByName(DRAFT_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Status with given name does not exists"));
        newDeal.setStatus(status);

        return newDeal;
    }

    private Page<DealDetails> findDealDetailsPage(DealSearchFilter filter) {
        Pageable pageable = PageRequest.of(filter.page(), filter.size());
        Specification<Deal> spec = buildSpecification(filter);
        return dealRepository.findAll(spec, pageable)
                .map(dealAppMapper::toDetails);
    }

    private Specification<Deal> buildSpecification(DealSearchFilter filter) {
        BigDecimal sumValue = filter.sum() != null ? filter.sum().value() : null;
        String sumCurrency = filter.sum() != null ? filter.sum().currency() : null;

        return new DealSpecificationBuilder()
                .active()
                .withId(filter.dealId())
                .withDescription(filter.description())
                .withAgreementNumber(filter.agreementNumber())
                .withAgreementDateBetween(filter.agreementDateFrom(), filter.agreementDateTo())
                .withAvailabilityDateBetween(filter.availabilityDateFrom(), filter.availabilityDateTo())
                .withTypeIn(filter.typeList())
                .withStatusIn(filter.statusList())
                .withCloseDateBetween(filter.closeDateFrom(), filter.closeDateTo())
                .withContractorSearch(filter.borrowerSearch(), ROLE_BORROWER)
                .withContractorSearch(filter.warranitySearch(), ROLE_WARRANTY)
                .withSumFilter(sumValue, sumCurrency)
                .build();
    }
}

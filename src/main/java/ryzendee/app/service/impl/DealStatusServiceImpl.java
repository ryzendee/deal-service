package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.DealStatusDetails;
import ryzendee.app.mapper.DealStatusAppMapper;
import ryzendee.app.repository.DealStatusRepository;
import ryzendee.app.service.DealStatusService;

import java.util.List;

import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;
import static ryzendee.app.constants.CacheNameConstants.DEAL_STATUS_METADATA_CACHE;

@Service
@RequiredArgsConstructor
public class DealStatusServiceImpl implements DealStatusService {

    private final DealStatusRepository dealStatusRepository;
    private final DealStatusAppMapper dealStatusAppMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = DEAL_STATUS_METADATA_CACHE, key = "'all'", cacheManager = DEAL_METADATA_CACHE_MANAGER)
    @Override
    public List<DealStatusDetails> getAllStatuses() {
        return dealStatusRepository.findAll().stream()
                .map(dealStatusAppMapper::toDetails)
                .toList();
    }
}

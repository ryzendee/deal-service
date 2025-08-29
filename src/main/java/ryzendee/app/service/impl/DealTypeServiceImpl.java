package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.DealTypeDetails;
import ryzendee.app.dto.DealTypeSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.mapper.DealTypeAppMapper;
import ryzendee.app.models.DealType;
import ryzendee.app.repository.DealTypeRepository;
import ryzendee.app.service.DealTypeService;

import java.util.List;

import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;
import static ryzendee.app.constants.CacheNameConstants.DEAL_TYPE_METADATA_CACHE;

@Service
@RequiredArgsConstructor
public class DealTypeServiceImpl implements DealTypeService {

    private final DealTypeRepository dealTypeRepository;
    private final DealTypeAppMapper dealTypeAppMapper;

    @Transactional
    @CacheEvict(value = DEAL_TYPE_METADATA_CACHE, key = "'all'", cacheManager = DEAL_METADATA_CACHE_MANAGER)
    @Override
    public DealTypeDetails saveOrUpdate(DealTypeSaveRequest request) {
        DealType type;

        if (request.id() != null) {
            type = getById(request.id());
            dealTypeAppMapper.updateFromRequest(request, type);
        } else {
            type = dealTypeAppMapper.toModel(request);
        }

        dealTypeRepository.save(type);

        return dealTypeAppMapper.toDetails(type);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = DEAL_TYPE_METADATA_CACHE, key = "'all'", cacheManager = DEAL_METADATA_CACHE_MANAGER)
    @Override
    public List<DealTypeDetails> getAllTypes() {
        return dealTypeRepository.findAll().stream()
                .map(dealTypeAppMapper::toDetails)
                .toList();
    }

    private DealType getById(String id) {
        return dealTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal type with given id does not exists: " + id));
    }
}

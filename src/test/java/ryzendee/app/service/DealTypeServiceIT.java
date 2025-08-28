package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import ryzendee.app.dto.DealTypeDetails;
import ryzendee.app.dto.DealTypeSaveRequest;
import ryzendee.app.models.DealType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;
import static ryzendee.app.constants.CacheNameConstants.DEAL_TYPE_METADATA_CACHE;
import static ryzendee.app.testutils.FixtureUtil.dealTypeFixture;

public class DealTypeServiceIT extends AbstractServiceIT {

    private static final String CACHE_KEY = "all";

    @Autowired
    private DealTypeService dealTypeService;

    private DealType existingDealType;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();
        existingDealType = dealTypeFixture();
        databaseUtil.save(existingDealType);
    }

    @Test
    void saveOrUpdate_withCachedDealTypes_shouldClearCacheAfterSaving() {
        List<DealTypeDetails> details = List.of(buildDealTypeDetailsWithId(existingDealType.getId()));
        cacheUtil.putInCache(DEAL_METADATA_CACHE_MANAGER, DEAL_TYPE_METADATA_CACHE, CACHE_KEY, details);
        DealTypeSaveRequest request = new DealTypeSaveRequest(existingDealType.getId(), "test");

        dealTypeService.saveOrUpdate(request);

        Cache.ValueWrapper cachedValue =
                cacheUtil.getFromCache(DEAL_METADATA_CACHE_MANAGER, DEAL_TYPE_METADATA_CACHE, CACHE_KEY);
        assertThat(cachedValue).isNull();
    }

    @Test
    void getAll_cacheIsEmpty_shouldPutTypesInCache() {
        // Act
        List<DealTypeDetails> dealTypeDetailsList = dealTypeService.getAllTypes();

        // Assert
        Cache.ValueWrapper cachedValue =
                cacheUtil.getFromCache(DEAL_METADATA_CACHE_MANAGER, DEAL_TYPE_METADATA_CACHE, CACHE_KEY);

        assertThat(cachedValue.get()).isInstanceOf(List.class);

        List<DealTypeDetails> cachedDetails = (List<DealTypeDetails>) cachedValue.get();
        assertThat(cachedDetails).containsExactlyElementsOf(dealTypeDetailsList);
    }

    private DealTypeDetails buildDealTypeDetailsWithId(String id) {
        return new DealTypeDetails(id, "builded-deal-type-details");
    }
}

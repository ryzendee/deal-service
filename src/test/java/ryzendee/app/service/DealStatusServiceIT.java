package ryzendee.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import ryzendee.app.dto.DealStatusDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;
import static ryzendee.app.constants.CacheNameConstants.DEAL_STATUS_METADATA_CACHE;
import static ryzendee.app.testutils.FixtureUtil.dealStatusFixture;

public class DealStatusServiceIT extends AbstractServiceIT {

    private static final String CACHE_KEY = "all";

    @Autowired
    private DealStatusService dealStatusService;

    @Test
    void getAll_cacheIsEmpty_shouldPutStatusesInCache() {
        // Arrange
        databaseUtil.save(dealStatusFixture());

        // Act
        List<DealStatusDetails> dealStatusDetails = dealStatusService.getAllStatuses();

        Cache.ValueWrapper cachedValue =
                cacheUtil.getFromCache(DEAL_METADATA_CACHE_MANAGER, DEAL_STATUS_METADATA_CACHE, CACHE_KEY);

        assertThat(cachedValue.get()).isInstanceOf(List.class);

        List<DealStatusDetails> cachedDetails = (List<DealStatusDetails>) cachedValue.get();
        assertThat(cachedDetails).containsExactlyElementsOf(dealStatusDetails);
    }
}

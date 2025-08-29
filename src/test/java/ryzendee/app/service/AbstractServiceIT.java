package ryzendee.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ryzendee.app.AbstractTestcontainers;
import ryzendee.app.testutils.CacheUtil;
import ryzendee.app.testutils.DatabaseUtil;
import ryzendee.app.testutils.testcontainers.EnableTestcontainers;

@EnableTestcontainers
@Import({DatabaseUtil.Config.class, CacheUtil.Config.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
abstract class AbstractServiceIT extends AbstractTestcontainers {

    @Autowired
    protected DatabaseUtil databaseUtil;

    @Autowired
    protected CacheUtil cacheUtil;
}
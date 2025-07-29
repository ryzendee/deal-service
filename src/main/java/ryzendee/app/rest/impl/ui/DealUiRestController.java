package ryzendee.app.rest.impl.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.rest.api.base.DealApi;
import ryzendee.app.rest.api.ui.DealUiApi;
import ryzendee.app.service.DealService;
import ryzendee.app.util.exporter.ExportResult;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DealUiRestController implements DealUiApi {

    private final DealService dealService;

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public DealDetails saveOrUpdateDeal(DealSaveRequest request) {
        return dealService.saveOrUpdate(request);
    }

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public void changeDealStatus(DealStatusChangeRequest request) {
        dealService.changeDealStatus(request);
    }

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public DealDetails getDealById(UUID id) {
        return dealService.getDealById(id);
    }

    @PreAuthorize("@dealAccessRules.canSearchDeals(authentication, #filter.typeList())")
    @Override
    public Page<DealDetails> searchDeals(DealSearchFilter filter) {
        return dealService.searchDeals(filter);
    }

    @PreAuthorize("@dealAccessRules.canReadDeals(authentication)")
    @Override
    public ResponseEntity<byte[]> exportDealsToExcel(DealSearchFilter filter) {
        ExportResult result = dealService.exportDetails(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.filename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(result.content());
    }
}

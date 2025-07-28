package ryzendee.app.rest.impl.base;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.rest.api.base.DealApi;
import ryzendee.app.service.DealService;
import ryzendee.app.util.exporter.ExportResult;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DealRestController implements DealApi {

    private final DealService dealService;
    @Override
    public DealDetails saveOrUpdateDeal(DealSaveRequest request) {
        return dealService.saveOrUpdate(request);
    }

    @Override
    public void changeDealStatus(DealStatusChangeRequest request) {
        dealService.changeDealStatus(request);
    }

    @Override
    public DealDetails getDealById(UUID id) {
        return dealService.getDealById(id);
    }

    @Override
    public Page<DealDetails> searchDeals(DealSearchFilter filter) {
        return dealService.searchDeals(filter);
    }

    @Override
    public ResponseEntity<byte[]> exportDealsToExcel(DealSearchFilter filter) {
        ExportResult result = dealService.exportDetails(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.filename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(result.content());
    }
}

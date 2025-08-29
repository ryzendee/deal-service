package ryzendee.app.rest.impl.base;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealStatusDetails;
import ryzendee.app.rest.api.base.DealStatusApi;
import ryzendee.app.service.DealStatusService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealStatusRestController implements DealStatusApi {

    private final DealStatusService dealStatusService;

    @Override
    public List<DealStatusDetails> getAllDealStatuses() {
        return dealStatusService.getAllStatuses();
    }
}

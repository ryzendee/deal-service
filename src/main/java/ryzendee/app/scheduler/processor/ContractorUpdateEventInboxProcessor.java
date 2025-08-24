package ryzendee.app.scheduler.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.common.dto.ContractorUpdateEvent;
import ryzendee.app.common.exception.MappingException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.models.Inbox;
import ryzendee.app.models.DealContractor;
import ryzendee.app.repository.DealContractorRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContractorUpdateEventInboxProcessor implements InboxProcessor {

    private final DealContractorRepository dealContractorRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void process(Inbox entity) {
        ContractorUpdateEvent event = toEvent(entity.getPayload());
        DealContractor contractor = dealContractorRepository.findById(UUID.fromString(event.getContractorId()))
                .orElseThrow(() -> new ResourceNotFoundException("Deal contractor with given id does not exists"));

        if (isEventNewerThanContractor(contractor, event)) {
            contractor.setInn(event.getInn());
            contractor.setName(event.getName());
            dealContractorRepository.save(contractor);
        }
    }

    private boolean isEventNewerThanContractor(DealContractor contractor, ContractorUpdateEvent event) {
        return contractor.getModifyDate() == null || contractor.getModifyDate().isBefore(event.getCreateDate());
    }

    private ContractorUpdateEvent toEvent(String payload) {
        try {
            return objectMapper.readValue(payload, ContractorUpdateEvent.class);
        } catch (JsonProcessingException ex) {
            throw new MappingException("Failed to map payload", ex);
        }
    }
}

package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.mapper.DealContractorAppMapper;
import ryzendee.app.models.Deal;
import ryzendee.app.models.DealContractor;
import ryzendee.app.repository.DealContractorRepository;
import ryzendee.app.repository.DealRepository;
import ryzendee.app.service.DealContractorService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealContractorServiceImpl implements DealContractorService {

    private final DealContractorAppMapper dealContractorAppMapper;
    private final DealContractorRepository dealContractorRepository;
    private final DealRepository dealRepository;

    @Transactional
    @Override
    public ContractorDetails saveOrUpdate(ContractorSaveRequest request) {
        DealContractor contractor;

        if (request.id() != null) {
            contractor = findById(request.id());
            dealContractorAppMapper.updateFromRequest(request, contractor);
        } else {
            contractor = dealContractorAppMapper.toModel(request);
        }

        Deal deal = dealRepository.findById(request.dealId())
                .orElseThrow(() -> new ResourceNotFoundException("Deal with given id does not exists"));
        contractor.setDeal(deal);
        dealContractorRepository.save(contractor);

        return dealContractorAppMapper.toDetails(contractor);

    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        DealContractor contractor = findById(id);
        contractor.setIsActive(false);
        dealContractorRepository.save(contractor);
    }

    private DealContractor findById(UUID id) {
        return dealContractorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contractor with given id does not exists"));
    }
}

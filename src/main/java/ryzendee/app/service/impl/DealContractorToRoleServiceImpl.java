package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.models.ContractorRole;
import ryzendee.app.models.ContractorToRoleId;
import ryzendee.app.models.ContractorToRoleRelation;
import ryzendee.app.models.DealContractor;
import ryzendee.app.repository.ContractorRoleRepository;
import ryzendee.app.repository.ContractorToRoleRepository;
import ryzendee.app.repository.DealContractorRepository;
import ryzendee.app.service.DealContractorToRoleService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealContractorToRoleServiceImpl implements DealContractorToRoleService {

    private final ContractorRoleRepository contractorRoleRepository;
    private final DealContractorRepository dealContractorRepository;
    private final ContractorToRoleRepository contractorToRoleRepository;

    @Transactional
    @Override
    public void addRoleToContractor(DealContractorRoleAddRequest request) {
        DealContractor contractor = dealContractorRepository.findById(request.dealContractorId())
                .orElseThrow(() -> new ResourceNotFoundException("Contractor with given id does not exists"));

        ContractorRole role = contractorRoleRepository.findById(request.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role with given id does not exists"));

        ContractorToRoleId id = buildId(contractor.getId(), role.getId());
        ContractorToRoleRelation existingRelation = contractorToRoleRepository.findById(id)
                .orElseGet(() -> buildContractorToRole(contractor, role));
        existingRelation.setActive(true);
        contractorToRoleRepository.save(existingRelation);
    }

    @Transactional
    @Override
    public void deleteRole(DealContractorRoleRemoveRequest request) {
        ContractorToRoleId id = buildId(request.dealContractorId(), request.roleId());
        ContractorToRoleRelation contractorToRoleRelation = contractorToRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with given parameters not found"));
        contractorToRoleRelation.setActive(false);
        contractorToRoleRepository.save(contractorToRoleRelation);
    }

    private ContractorToRoleRelation buildContractorToRole(DealContractor contractor, ContractorRole role) {
        return ContractorToRoleRelation.builder()
                .dealContractor(contractor)
                .contractorRole(role)
                .contractorToRoleId(buildId(contractor.getId(), role.getId()))
                .build();
    }

    private ContractorToRoleId buildId(UUID contractorId, String roleId) {
        return ContractorToRoleId.builder()
                .contractorId(contractorId)
                .roleId(roleId)
                .build();
    }
}

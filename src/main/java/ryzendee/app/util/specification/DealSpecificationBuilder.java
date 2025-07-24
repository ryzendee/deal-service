package ryzendee.app.util.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ryzendee.app.models.ContractorRole;
import ryzendee.app.models.ContractorToRoleRelation;
import ryzendee.app.models.Deal;
import ryzendee.app.models.DealContractor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

public class DealSpecificationBuilder {

    private final List<Specification<Deal>> specs = new ArrayList<>();

    public DealSpecificationBuilder active() {
        specs.add((root, query, cb) -> cb.isTrue(root.get("isActive")));
        return this;
    }

    public DealSpecificationBuilder withId(UUID id) {
        if (id != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("id"), id));
        }
        return this;
    }

    public DealSpecificationBuilder withDescription(String desc) {
        if (hasText(desc)) {
            specs.add((root, query, cb) -> cb.equal(cb.lower(root.get("description")), desc.toLowerCase()));
        }
        return this;
    }

    public DealSpecificationBuilder withAgreementNumber(String num) {
        if (hasText(num)) {
            specs.add((root, query, cb) -> cb.like(cb.lower(root.get("agreementNumber")), "%" + num.toLowerCase() + "%"));
        }
        return this;
    }

    public DealSpecificationBuilder withAgreementDateBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            specs.add((root, query, cb) -> cb.between(root.get("agreementDate"), from, to));
        }
        return this;
    }

    public DealSpecificationBuilder withAvailabilityDateBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            specs.add((root, query, cb) -> cb.between(root.get("availabilityDate"), from, to));
        }
        return this;
    }

    public DealSpecificationBuilder withCloseDateBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            specs.add((root, query, cb) -> cb.between(root.get("closeDt"), from, to));
        }
        return this;
    }

    public DealSpecificationBuilder withTypeIn(List<String> types) {
        if (types != null && !types.isEmpty()) {
            specs.add((root, query, cb) -> root.get("type").get("id").in(types));
        }
        return this;
    }

    public DealSpecificationBuilder withStatusIn(List<String> statuses) {
        if (statuses != null && !statuses.isEmpty()) {
            specs.add((root, query, cb) -> root.get("status").get("id").in(statuses));
        }
        return this;
    }

    public DealSpecificationBuilder withSumFilter(BigDecimal value, String currency) {
        if (value != null || hasText(currency)) {
            specs.add((root, query, cb) -> {
                Join<Object, Object> sumJoin = root.join("sums", JoinType.LEFT);
                List<Predicate> predicates = new ArrayList<>();
                if (value != null) {
                    predicates.add(cb.equal(sumJoin.get("sum"), value));
                }
                if (hasText(currency)) {
                    predicates.add(cb.equal(sumJoin.get("currency"), currency));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            });
        }
        return this;
    }

    public DealSpecificationBuilder withContractorSearch(String text, String roleCategory) {
        if (hasText(text) && hasText(roleCategory)) {
            specs.add((root, query, cb) -> {
                Join<Deal, DealContractor> contractorJoin = root.join("contractors");
                Join<DealContractor, ContractorToRoleRelation> roleRelationJoin = contractorJoin.join("roles");
                Join<ContractorToRoleRelation, ContractorRole> roleJoin = roleRelationJoin.join("contractorRole");

                Predicate rolePredicate = cb.equal(roleJoin.get("category"), roleCategory);
                String likePattern = "%" + text.toLowerCase() + "%";
                Predicate idLike = cb.like(cb.lower(contractorJoin.get("contractorId")), likePattern);
                Predicate nameLike = cb.like(cb.lower(contractorJoin.get("name")), likePattern);
                Predicate innLike = cb.like(cb.lower(contractorJoin.get("inn")), likePattern);
                Predicate orFields = cb.or(idLike, nameLike, innLike);
                Predicate isActive = cb.isTrue(contractorJoin.get("isActive"));

                return cb.and(rolePredicate, orFields, isActive);
            });
        }
        return this;
    }

    public Specification<Deal> build() {
        return specs.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}

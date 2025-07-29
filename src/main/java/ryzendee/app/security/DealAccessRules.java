package ryzendee.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ryzendee.starter.jwt.auth.AbstractReferenceAccessRules;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("dealAccessRules")
public class DealAccessRules extends AbstractReferenceAccessRules {

    public boolean canEditDeals(Authentication authentication) {
        return isSuperUser(authentication);
    }

    public boolean canReadDeals(Authentication authentication) {
        return isSuperUser(authentication);
    }

    public boolean canSearchDeals(Authentication authentication, List<String> typeList) {
        Set<String> typeSet = new HashSet<>(typeList);
        return isSuperUser(authentication) ||
                isOverdraftUserAndDealType(authentication, typeSet) ||
                isCreditUserAndDealType(authentication, typeSet);
    }

    @Override
    protected boolean canEditReference(Authentication authentication) {
        return isSuperUser(authentication);
    }

    private boolean isSuperUser(Authentication authentication) {
        return hasRole(authentication, AuthRole.SUPERUSER, AuthRole.DEAL_SUPERUSER);
    }

    private boolean isOverdraftUserAndDealType(Authentication authentication, Set<String> typeSet) {
        return hasRole(authentication, AuthRole.OVERDRAFT_USER) && typeSet.contains("OVERDRAFT");
    }

    private boolean isCreditUserAndDealType(Authentication authentication, Set<String> typeSet) {
        return hasRole(authentication, AuthRole.CREDIT_USER) && typeSet.contains("OVERDRAFT");
    }
}

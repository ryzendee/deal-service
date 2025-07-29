package ryzendee.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DealAccessRulesTest {

    private DealAccessRules accessRules;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        accessRules = new DealAccessRules();
        authentication = mock(Authentication.class);
    }

    @Test
    void canEditDeals_withSuperUserRole_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.SUPERUSER)).when(authentication).getAuthorities();

        boolean result = accessRules.canEditDeals(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canEditDeals_withoutSuperUserRole_returnsFalse() {
        doReturn(toGrantedAuthorities(AuthRole.CREDIT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canEditDeals(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isFalse();
    }

    @Test
    void canReadDeals_withDealSuperUserRole_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.DEAL_SUPERUSER)).when(authentication).getAuthorities();

        boolean result = accessRules.canReadDeals(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canReadDeals_withoutSuperUserRole_returnsFalse() {
        doReturn(toGrantedAuthorities(AuthRole.OVERDRAFT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canReadDeals(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isFalse();
    }

    @Test
    void canSearchDeals_withSuperUserRole_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.SUPERUSER)).when(authentication).getAuthorities();

        boolean result = accessRules.canSearchDeals(authentication, List.of("ANY"));

        verify(authentication).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canSearchDeals_withOverdraftUserAndOverdraftType_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.OVERDRAFT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canSearchDeals(authentication, List.of("OVERDRAFT"));

        verify(authentication, atLeastOnce()).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canSearchDeals_withOverdraftUserAndOtherType_returnsFalse() {
        doReturn(toGrantedAuthorities(AuthRole.OVERDRAFT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canSearchDeals(authentication, List.of("CREDIT"));

        verify(authentication, atLeastOnce()).getAuthorities();
        assertThat(result).isFalse();
    }

    @Test
    void canSearchDeals_withCreditUserAndOverdraftType_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.CREDIT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canSearchDeals(authentication, List.of("OVERDRAFT"));

        verify(authentication, atLeastOnce()).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canSearchDeals_withCreditUserAndOtherType_returnsFalse() {
        doReturn(toGrantedAuthorities(AuthRole.CREDIT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canSearchDeals(authentication, List.of("INSTALLMENT"));

        verify(authentication, atLeastOnce()).getAuthorities();
        assertThat(result).isFalse();
    }

    @Test
    void canEditReference_withDealSuperUser_returnsTrue() {
        doReturn(toGrantedAuthorities(AuthRole.DEAL_SUPERUSER)).when(authentication).getAuthorities();

        boolean result = accessRules.canEditReference(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isTrue();
    }

    @Test
    void canEditReference_withoutSuperUserRole_returnsFalse() {
        doReturn(toGrantedAuthorities(AuthRole.OVERDRAFT_USER)).when(authentication).getAuthorities();

        boolean result = accessRules.canEditReference(authentication);

        verify(authentication).getAuthorities();
        assertThat(result).isFalse();
    }

    private Collection<? extends GrantedAuthority> toGrantedAuthorities(AuthRole... roles) {
        return Stream.of(roles)
                .map(AuthRole::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

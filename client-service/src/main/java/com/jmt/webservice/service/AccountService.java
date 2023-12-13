package com.jmt.webservice.service;

import com.jmt.webservice.model.PasswordChangeRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final GatewayRequest gatewayRequest;

    public Mono<Void> updatePassword(OAuth2AuthenticationToken oauthToken, PasswordChangeRequest passwordChangeRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/password", passwordChangeRequest).then();
    }
    public Mono<Void> updateEmail(OAuth2AuthenticationToken oauthToken, String updateEmailRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/email", updateEmailRequest).then();
    }
    public Mono<Void> updatePhoneNumber(OAuth2AuthenticationToken oauthToken, String updatePhoneNumberRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/phone-number", updatePhoneNumberRequest).then();
    }
    public Mono<Void> updateAddress(OAuth2AuthenticationToken oauthToken, String updateAddressRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/address", updateAddressRequest).then();
    }
    public Mono<Void> updateFirstName(OAuth2AuthenticationToken oauthToken, String updateFirstNameRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/first-name", updateFirstNameRequest).then();

    }
    public Mono<Void> updateLastName(OAuth2AuthenticationToken oauthToken, String updateLastNameRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/last-name", updateLastNameRequest).then();
    }
    private Mono<Void> updateProfilePicture( OAuth2AuthenticationToken oauthToken, String updateProfilePictureRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/profile-picture", updateProfilePictureRequest).then();
    }
    private Mono<Void> accountStatus( OAuth2AuthenticationToken oauthToken, String accountStatusRequest) {
        return gatewayRequest.post(oauthToken, "/management/update/account-status", accountStatusRequest).then();
    }
}

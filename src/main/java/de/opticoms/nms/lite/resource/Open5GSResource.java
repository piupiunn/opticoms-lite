package de.opticoms.nms.lite.resource;

import de.opticoms.nms.lite.config.OpticomsConfig;
import de.opticoms.nms.lite.controller.model.CoreSim;
import de.opticoms.nms.lite.resource.model.*;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static de.opticoms.nms.lite.resource.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class Open5GSResource {

    final RestTemplate restTemplate;
    final OpticomsConfig opticomsConfig;

    @Autowired
    public Open5GSResource(RestTemplate restTemplate, OpticomsConfig opticomsConfig) {
        this.restTemplate = restTemplate;
        this.opticomsConfig = opticomsConfig;
    }

    public FirstSessionToken getCsrfSessionToken() {
        try {
            val response = restTemplate.exchange(CSRF_TOKEN_URL, HttpMethod.GET, null, CsrfToken.class);
            if (Objects.isNull(response.getBody())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSRF token not received");
            }
            return FirstSessionToken.builder()
                    .csrfToken(response.getBody().getCsrfToken())
                    .cookie(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0))
                    .build();
        } catch (final HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "OPEN5GS API EXCEPTION: ".concat(ExceptionUtils.getStackTrace(e)));
        }
    }


    public String loginUser() {
        val csrfSessionToken = getCsrfSessionToken();
        try {
            val user = AuthUser.builder()
                    .username(opticomsConfig.getUser())
                    .password(opticomsConfig.getPassword())
                    .build();

            val header = new HttpHeaders();
            header.setContentType(APPLICATION_JSON);
            header.set("X-CSRF-TOKEN", csrfSessionToken.getCsrfToken());
            header.set("Cookie", csrfSessionToken.getCookie());

            val response = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, new HttpEntity<>(user, header), String.class);
            if (Objects.isNull(response.getBody())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed");
            }

            return response.getStatusCode().equals(HttpStatus.FOUND)
                    ? Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0)
                    : "false";
        } catch (final HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OPEN5GS API EXCEPTION: ".concat(ExceptionUtils.getStackTrace(e)));
        }
    }

    public Tokens getNewTokens() {
        val res = loginUser();
        if (res.equals("false")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user login to OPEN5GS");
        }
        try {
            val header = new HttpHeaders();
            header.setContentType(APPLICATION_JSON);
            header.set("Cookie", res);

            val response = restTemplate.exchange(REFRESH_TOKEN_URL, HttpMethod.GET, new HttpEntity<>(header), AuthModel.class);
            if (Objects.isNull(response.getBody())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "New tokens not received");
            }
            return Tokens.builder()
                    .authToken(response.getBody().getAuthToken())
                    .csrfToken(response.getBody().getCsrfToken())
                    .cookie(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0))
                    .build();
        } catch (final HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OPEN5GS API EXCEPTION: ".concat(ExceptionUtils.getStackTrace(e)));
        }
    }

    public Object saveNewSimUser(CoreSim newSimUser) {
        val tokens = getNewTokens();
        val header = new HttpHeaders();
        header.setContentType(APPLICATION_JSON);
        header.set("X-CSRF-TOKEN", tokens.getCsrfToken());
        header.set("Authorization", "Bearer " + tokens.getAuthToken());
        header.set("Cookie", tokens.getCookie());

        try {
            val response = restTemplate.exchange(SAVE_NEW_SIM_URL, HttpMethod.POST, new HttpEntity<>(newSimUser, header), CoreSim.class);
            if (Objects.isNull(response.getBody())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed save new sim");
            }
            return response.getBody();
        } catch (final HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OPEN5GS API EXCEPTION: ".concat(ExceptionUtils.getStackTrace(e)));
        }
    }

    public String deleteSim(String imsi) {
        val tokens = getNewTokens();
        val header = new HttpHeaders();
        header.setContentType(APPLICATION_JSON);
        header.set("X-CSRF-TOKEN", tokens.getCsrfToken());
        header.set("Authorization", "Bearer " + tokens.getAuthToken());
        header.set("Cookie", tokens.getCookie());
        try {
            val url = DELETE_SIM_URL.replace("IMSI", imsi);
            val response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(header), String.class);

            return response.getStatusCode().equals(HttpStatus.NO_CONTENT)
                    ? "Imsi deleted: ".concat(imsi)
                    : "Error deleting imsi: ".concat(imsi);
        } catch (final HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OPEN5GS API EXCEPTION: ".concat(ExceptionUtils.getStackTrace(e)));
        }
    }
}

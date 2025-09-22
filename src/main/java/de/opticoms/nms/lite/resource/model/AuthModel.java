package de.opticoms.nms.lite.resource.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthModel {
    Integer clientMaxAge;
    String csrfToken;
    String AuthToken;
    User user;
}

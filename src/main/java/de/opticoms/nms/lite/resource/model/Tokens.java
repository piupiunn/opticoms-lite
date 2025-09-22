package de.opticoms.nms.lite.resource.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {
    String csrfToken;
    String authToken;
    String cookie;
}

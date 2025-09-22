package de.opticoms.nms.lite.resource.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsrfToken {
    String csrfToken;
}

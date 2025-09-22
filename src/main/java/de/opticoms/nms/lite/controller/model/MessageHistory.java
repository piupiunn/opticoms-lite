package de.opticoms.nms.lite.controller.model;

import lombok.*;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageHistory {
    Integer id;
    String body;
    String type;
    String from;
    String to;
    Long created;
    Boolean seen;
}

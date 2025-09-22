package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String from;
    String to;
    String text;
}

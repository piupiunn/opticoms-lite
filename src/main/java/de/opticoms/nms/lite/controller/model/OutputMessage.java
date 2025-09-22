package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage {
    Integer messageHistoryId;
    String from;
    String text;
    String time;
    String type;
}

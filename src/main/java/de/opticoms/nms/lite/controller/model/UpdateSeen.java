package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSeen {
    String to;
    Integer messageHistoryId;
    Boolean seen;
}

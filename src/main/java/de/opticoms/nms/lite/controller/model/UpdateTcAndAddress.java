package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTcAndAddress {
    Integer id;
    String tc;
    String address;
}

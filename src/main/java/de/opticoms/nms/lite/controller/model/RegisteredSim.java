package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredSim {
    Integer id;
    String imsi;
    String msisdn;
    String tc;
    String address;
    String fullname;
    String role;
    String username;
    Long created;
    Long deactivated;
}

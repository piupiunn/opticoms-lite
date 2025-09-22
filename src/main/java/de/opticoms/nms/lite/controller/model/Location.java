package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    Integer id;
    String imsi;
    Float latitude;
    Float longitude;
    Float altitude;
    Float accuracy;
    Float altitudeAccuracy;
    Float heading;
    Float speed;
    String reason;
}

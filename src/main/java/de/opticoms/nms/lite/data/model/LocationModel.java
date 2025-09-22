package de.opticoms.nms.lite.data.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location", schema = "nms_lite")
@Entity
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String imsi;
    Float latitude;
    Float longitude;
    Float altitude;
    Float accuracy;
    @Column(name = "altitude_accuracy")
    Float altitudeAccuracy;
    Float heading;
    Float speed;
    String reason;
}

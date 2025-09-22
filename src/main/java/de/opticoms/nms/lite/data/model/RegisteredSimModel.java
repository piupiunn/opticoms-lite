package de.opticoms.nms.lite.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registered_sim", schema = "nms_lite")
@Entity
public class RegisteredSimModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String imsi;
    String msisdn;
    String tc;
    String address;
    @Column(name = "full_name")
    String fullname;
    String role;
    @Column(name = "user_name")
    String username;
    Date created;
    Date deactivated;
}

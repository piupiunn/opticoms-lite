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
@Table(name = "message_history", schema = "nms_lite")
@Entity
public class MessageHistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String body;
    @Column(name = "message_type")
    String type;
    @Column(name = "message_from")
    String from;
    @Column(name = "message_to")
    String to;
    Date created;
    Boolean seen;
}

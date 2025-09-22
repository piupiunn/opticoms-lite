package de.opticoms.nms.lite.resource.model;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    ArrayList<String> roles;
    String _id;
    String username;
    Integer _v;
}

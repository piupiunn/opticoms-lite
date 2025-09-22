package de.opticoms.nms.lite.controller.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreSim {
    String imsi;
    Security security;
    Ambr ambr;
    Integer subscriber_status;
    Integer operator_determined_barring;
    List<Slice> slice;

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slice {
        Integer sst;
        Boolean default_indicator;
        List<Session> session;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Session {
        String name;
        Integer type;
        Ambr ambr;
        Qos qos;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Qos {
        Integer index;
        Arp arp;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Arp {
        Integer priority_level;
        Integer pre_emption_capability;
        Integer pre_emption_vulnerability;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Security {
        String k;
        String amf;
        Integer op_type;
        String op_value;
        Object op;
        String opc;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ambr {
        DownLink downlink;
        UpLink uplink;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpLink {
        Integer value;
        Integer unit;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DownLink {
        Integer value;
        Integer unit;
    }
}

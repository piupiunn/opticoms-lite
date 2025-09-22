package de.opticoms.nms.lite.controller.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyMessage {
    private String from;      // zorunlu: kim acil durum çağrısı yapıyor
    private String text;      // serbest: “acil durum” açıklaması / kısa mesaj

}

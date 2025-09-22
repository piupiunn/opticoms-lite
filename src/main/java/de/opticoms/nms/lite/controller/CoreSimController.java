package de.opticoms.nms.lite.controller;

import de.opticoms.nms.lite.controller.model.CoreSim;
import de.opticoms.nms.lite.controller.model.ResponseMessage;
import de.opticoms.nms.lite.resource.Open5GSResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.util.List;

@Slf4j
@RestController
public class CoreSimController {

    final Open5GSResource open5GSResource;

    @Autowired
    public CoreSimController(Open5GSResource open5GSResource) {
        this.open5GSResource = open5GSResource;
    }

    @RequestMapping(value = "/v1/open/sim/save", method = RequestMethod.POST)
    private ResponseEntity<Object> saveNewSim(@RequestBody CoreSim newSim) {
        log.info("Calling: saveNewSim >> ".concat(newSim.toString()));

        val simToSave = CoreSim.builder()
                .imsi(newSim.getImsi())
                .security(CoreSim.Security.builder()
                        .k(newSim.getSecurity().getK())
                        .amf("8000")
                        .op_type(0)
                        .op_value("E8ED289D EBA952E4 283B54E8 8E6183CA")
                        .op(null)
                        .opc(newSim.getSecurity().getOpc())
                        .build())
                .ambr(CoreSim.Ambr.builder()
                        .downlink(CoreSim.DownLink.builder()
                                .value(1)
                                .unit(3)
                                .build())
                        .uplink(CoreSim.UpLink.builder()
                                .value(1)
                                .unit(3)
                                .build())
                        .build())
                .subscriber_status(0)
                .operator_determined_barring(0)
                .slice(List.of(CoreSim.Slice.builder()
                        .sst(1)
                        .default_indicator(true)
                        .session(List.of(CoreSim.Session.builder()
                                .name("internet")
                                .type(3)
                                .ambr(CoreSim.Ambr.builder()
                                        .downlink(CoreSim.DownLink.builder()
                                                .value(1)
                                                .unit(3)
                                                .build())
                                        .uplink(CoreSim.UpLink.builder()
                                                .value(1)
                                                .unit(3)
                                                .build())
                                        .build())
                                        .qos(CoreSim.Qos.builder()
                                                .index(9)
                                                .arp(CoreSim.Arp.builder()
                                                        .priority_level(8)
                                                        .pre_emption_capability(1)
                                                        .pre_emption_vulnerability(1)
                                                        .build())
                                                .build())
                                .build()))
                        .build()))
                .build();

        val response = open5GSResource.saveNewSimUser(simToSave);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/v1/open/sim/delete/{imsi}", method = RequestMethod.DELETE)
    private ResponseEntity<ResponseMessage> deleteSim(@PathVariable String imsi) {
        log.info("Calling: deleteSim >> ".concat(imsi));

        val response = open5GSResource.deleteSim(imsi);

        return ResponseEntity.ok(ResponseMessage.builder().message("Imsi deleted: ".concat(imsi)).build());
    }
}

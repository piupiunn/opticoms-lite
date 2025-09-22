package de.opticoms.nms.lite.controller;

import de.opticoms.nms.lite.controller.model.RegisteredSim;
import de.opticoms.nms.lite.resource.ServerHealthService;
import de.opticoms.nms.lite.resource.model.ServerHealth;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ServerHealthController {

    final ServerHealthService serverHealthService;

    @Autowired
    public ServerHealthController(ServerHealthService serverHealthService) {
        this.serverHealthService = serverHealthService;
    }

    @RequestMapping(value = "/v1/serverHealth/get_info", method = RequestMethod.GET)
    private ResponseEntity<ServerHealth> getServerInfo() {
        log.info("Calling: getServerInfo");

        return ResponseEntity.ok(serverHealthService.getServerHealthInfo());
    }
}

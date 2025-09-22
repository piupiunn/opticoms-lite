package de.opticoms.nms.lite.resource.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerHealth {
    Double cpuLoad;

    Double totalMemory;
    Double usedMemory;
    Double freeMemory;
    Double memoryUsage;

    Double totalDiskSpace;
    Double usedDiskSpace;
    Double freeDiskSpace;

    Double cpuTemperature;
}

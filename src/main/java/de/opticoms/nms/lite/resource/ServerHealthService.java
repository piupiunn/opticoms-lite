package de.opticoms.nms.lite.resource;
import de.opticoms.nms.lite.resource.model.ServerHealth;
import lombok.val;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;

@Component
public class ServerHealthService {

    public ServerHealth getServerHealthInfo() {
        try {
            val health = ServerHealth.builder().build();
            val systemInfo = new SystemInfo();
            val hal = systemInfo.getHardware();
            val os = systemInfo.getOperatingSystem();

            // CPU Metrics
            val processor = hal.getProcessor();
            val prevTicks = processor.getSystemCpuLoadTicks();
            Thread.sleep(1000); // Wait a second to calculate the load
            health.setCpuLoad(processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100);

            // Memory Metrics
            val memory = hal.getMemory();
            val totalMemory = (double) memory.getTotal();
            val availableMemory = (double) memory.getAvailable();
            val usedMemory = totalMemory - availableMemory;
            val memoryUsagePercentage = (usedMemory * 100) / totalMemory;

            health.setTotalMemory(totalMemory / 1_048_576); // Convert to MB
            health.setUsedMemory(usedMemory / 1_048_576); // Convert to MB
            health.setFreeMemory(availableMemory / 1_048_576); // Convert to MB
            health.setMemoryUsage(memoryUsagePercentage);

            // Disk Usage
            val fileStores = os.getFileSystem().getFileStores();
            double totalDiskSpace = 0.0;
            double freeDiskSpace = 0.0;

            for (val fileStore : fileStores) {
                totalDiskSpace += fileStore.getTotalSpace();
                freeDiskSpace += fileStore.getUsableSpace();
            }

            val usedDiskSpace = totalDiskSpace - freeDiskSpace;

            health.setTotalDiskSpace(totalDiskSpace / 1_073_741_824); // Convert to GB
            health.setUsedDiskSpace(usedDiskSpace / 1_073_741_824); // Convert to GB
            health.setFreeDiskSpace(freeDiskSpace / 1_073_741_824); // Convert to GB

            // Temperature
            val cpuTemperature = hal.getSensors().getCpuTemperature();
            health.setCpuTemperature(cpuTemperature);

            return health;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}

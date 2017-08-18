package scutum.scanner.endpointprotection.contracts;

import java.time.LocalDateTime;
import java.util.Collection;

public class MachineData {
    private String id;
    private String customerId;

    private int version;
    private int scannerType = 1;
    private LocalDateTime timeStamp;
    private Collection<Process> processes;


    public MachineData(String id,
                       String customerId,
                       int version,
                       int scannerType,
                       LocalDateTime timeStamp,
                       Collection<Process> processes) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setVersion(version);
        this.setScannerType(scannerType);
        this.setTimeStamp(timeStamp);
        this.setProcesses(processes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getScannerType() {
        return scannerType;
    }

    public void setScannerType(int scannerType) {
        this.scannerType = scannerType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Collection<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(Collection<Process> processes) {
        this.processes = processes;
    }
}

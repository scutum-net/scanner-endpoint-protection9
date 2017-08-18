package scutum.scanner.endpointprotection.providers;

import scutum.scanner.endpointprotection.contracts.IDataScanner;
import scutum.scanner.endpointprotection.contracts.MachineData;
import scutum.scanner.endpointprotection.contracts.Process;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Java class that monitors processes and their parents
 */
//todo: add hash
//todo: add file size
//todo: make it more efficient
//todo: add linux functionality ps -e -> https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java
public class DataScannerWindows implements IDataScanner {

    private String hostName;
    private String customer;
    private int version;
    private int scanType;

    public DataScannerWindows(String hostName, String customer, int version, int scanType){
        this.hostName = hostName;
        this.customer = customer;
        this.version = version;
        this.scanType = scanType;
    }

    @Override
    public MachineData scan() {
        MachineData machineData = new MachineData("id", "customerId", 1, 3, LocalDateTime.now(), new ArrayList<>());
        try {
            // add processes from task manager
            Collection<Process> processes = getPlainProcesses();

            // add details - parent process id and full path on disk
            fillParentProcess(processes);

            // add all to the final model
            processes.forEach(process -> machineData.getProcesses().add(process));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return machineData;
    }

    private Collection<Process> getPlainProcesses() throws IOException {
        Collection<Process> processes = new ArrayList<>();
        Collection<String> lines = runCommand("tasklist /v /fi \"PID gt 1000\" /fo csv");
        lines.forEach(line -> {
            List<String> vals = Arrays.stream(line.split(",")).map(x -> x.replace("\"", "")).collect(Collectors.toList());
            Process process1 = new Process(Integer.valueOf(vals.get(1)), -1, vals.get(6), vals.get(0), "123456l", 34234);
            processes.add(process1);
        });
        return processes;
    }

    private void fillParentProcess(Collection<Process> processes) throws IOException {

        Set<String> lines = new HashSet<>();
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            for (char c : alphabet) {
                String cmd = "cmd /c wmic process get processid,parentprocessid,executablepath|find \""+c+"\"";
                Collection<String> currentLines = runCommand(cmd);
                currentLines.stream().filter(line->!line.isEmpty()).forEach(lines::add);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.forEach(line -> {
            String[] vals = line.trim().replaceAll(" +", ",").split(",");
            Optional<Process> poptional = processes.stream().filter(process -> process.getId() == Integer.valueOf(vals[vals.length-1])).findFirst();
            if (poptional.isPresent()) {
                poptional.get().setParentId(Integer.valueOf(vals[vals.length-2]));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < vals.length-2; i++) {
                    sb.append(vals[i]);
                    if (i < vals.length-3) {
                        sb.append(" ");
                    }
                }
                poptional.get().setPath(sb.toString());
            }
        });
    }


    private Collection<String> runCommand(String runCommand) throws IOException {
        Collection<String> lines = new ArrayList<>();
        java.lang.Process process = Runtime.getRuntime().exec(runCommand);
        Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
        boolean isNoFirstLine = false;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            System.out.println(line);

            if (isNoFirstLine) {
                lines.add(line);
            }
            isNoFirstLine = true;
        }
        scanner.close();
        return lines;
    }

    //String name = ManagementFactory.getRuntimeMXBean().getName();
    //System.out.println(name);
    //String pid = name.split("@")[0];
    //System.out.println("Pid is:" + pid);

//    private void processNative() {
//        WinNT winNT = (WinNT) Native.loadLibrary(WinNT.class, W32APIOptions.UNICODE_OPTIONS);
//
//        WinNT.HANDLE snapshot = winNT.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
//
//        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
//
//        while (winNT.Process32Next(snapshot, processEntry)) {
//            System.out.println(processEntry.th32ProcessID + "\t" + Native.toString(processEntry.szExeFile));
//        }
//
//        winNT.CloseHandle(snapshot);
//    }
}

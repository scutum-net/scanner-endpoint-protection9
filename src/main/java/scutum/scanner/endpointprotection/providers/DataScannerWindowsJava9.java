package scutum.scanner.endpointprotection.providers;

import scutum.core.contracts.endpointprotection.IDataScanner;
import scutum.core.contracts.endpointprotection.MachineData;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

//import com.sun.jna.Native;
//import com.sun.jna.platform.win32.*;
//import com.sun.jna.win32.W32APIOptions;

public class DataScannerWindowsJava9 implements IDataScanner {
    @Override
    public MachineData scan() {

        //Get the handle for current process i.e the JVM process
        //ProcessHandle currentProcess = ProcessHandle.current();

        //ProcessHandle.allProcesses().map(ProcessHandle::info).filter(info -> info.user().filter(name -> name.contains(userName)).isPresent()).sorted(Comparator.comparing(info -> info.totalCpuDuration().orElse(Duration.ZERO))).forEach(info -> info.command().ifPresent(command -> info.totalCpuDuration().ifPresent(duration -> System.out.println(command + " has been running for " + duration.toMinutes()))));
        //ProcessHandle.allProcesses().forEach(processHandle -> {
        //            System.out.println("**** Current process info ****");
        //            ProcessDemoUtil.printProcessDetails(processHandle);
        //        });

        List<ProcessHandle> processes = ProcessHandle.allProcesses().collect(Collectors.toList());
        List<ProcessHandle.Info> infos = processes.stream().map(ProcessHandle::info).collect(Collectors.toList());
        //processes.stream().map(x->x.children());
        //.filter(ph -> ph.info().command().isPresent()).forEach(p -> System.out.println(p.pid() + " " + p.info().commandLine()));
        System.out.print("Enter something:");
        String input = System.console().readLine();

        return null;
    }

    public static void printProcessDetails(ProcessHandle currentProcess){
        //Get the instance of process info
        ProcessHandle.Info currentProcessInfo = currentProcess.info();
        //if ( currentProcessInfo.command().orElse("").equals("")){
        //    return;
        //}
        //Get the process id
        System.out.println("Process id: " + currentProcess.pid());
        //Get the command pathname of the process
        System.out.println("Command: " + currentProcessInfo.command().orElse(""));
        //Get the arguments of the process
        String[] arguments = currentProcessInfo.arguments().orElse(new String[]{});
        if ( arguments.length != 0){
            System.out.print("Arguments: ");
            for(String arg : arguments){
                System.out.print(arg + " ");
            }
            System.out.println();
        }
        //Get the start time of the process
        System.out.println("Started at: " + currentProcessInfo.startInstant().orElse(Instant.now()).toString());
        //Get the time the process ran for
        System.out.println("Ran for: " + currentProcessInfo.totalCpuDuration().orElse(Duration.ofMillis(0)).toMillis() + "ms");
        //Get the owner of the process
        System.out.println("Owner: " + currentProcessInfo.user().orElse(""));
    }
}

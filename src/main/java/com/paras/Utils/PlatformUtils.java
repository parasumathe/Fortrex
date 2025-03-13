package com.paras.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class PlatformUtils {
    public enum SUPPORTED_OS {
        LINUX,
        UNKNOWN_OS
    }

    private static SUPPORTED_OS supportedOS = SUPPORTED_OS.UNKNOWN_OS;

    private static ArrayList<String> linuxCommandPath = new ArrayList<>(Arrays.asList("/bin", "/sbin", "/usr/bin", "/usr/sbin"));

    public static SUPPORTED_OS getSupportedOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.equalsIgnoreCase("linux")) {
            supportedOS = SUPPORTED_OS.LINUX;
        }
        return supportedOS;
    }

    public static String findCommand(String command) {

        for (String path: linuxCommandPath) {
            Path p = Paths.get(path + "/" + command);
            if (Files.exists(p))
                return p.toAbsolutePath().toString();
        }
        return null;
    }

    public static ArrayList<String> executeCommandAndGetStdout(String[] command) throws IOException {
        ArrayList<String> ret = new ArrayList<>();
        BufferedReader stdOut = null;
        try {
            Process child = Runtime.getRuntime().exec(command);
            stdOut = new BufferedReader(new InputStreamReader(child.getInputStream()));
            while (child.isAlive()) {
                String line = stdOut.readLine();
                if (line != null)
                    ret.add(line);
            }
            /* If the process died before we could read its stdout, drain the remaining output */
            String line = stdOut.readLine();
            while (line != null) {
                ret.add(line);
                line = stdOut.readLine();
            }
        }catch (IOException ioException){
            throw new IOException(ioException);
        }
        finally {
            if(stdOut != null){
                stdOut.close();
            }
        }
        return ret;
    }

    public static String executeCommandAndGetStdoutAsString(String[] command) throws IOException {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> lines = executeCommandAndGetStdout(command);
        for (String line: lines) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}

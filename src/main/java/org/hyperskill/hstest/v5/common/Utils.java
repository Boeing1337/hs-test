package org.hyperskill.hstest.v5.common;

import org.hyperskill.hstest.v5.testcase.Process;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Utils {
    public static final String CURRENT_DIR = System.getProperty("user.dir") + File.separator;
    private static final String TEMP_FILE_PREFIX = "hyperskill-temp-file-";

    private static final Set<String> RETURNED_NONEXISTENT_FILES = new HashSet<>();

    private Utils() {}

    public static void createFiles(Map<String, String> files) throws IOException {
        for (Map.Entry<String, String> fileEntry : files.entrySet()) {
            String filename = fileEntry.getKey();
            String content = fileEntry.getValue();
            Files.write(Paths.get(CURRENT_DIR + filename), content.getBytes());
        }
    }

    public static void deleteFiles(Map<String, String> files) throws IOException {
        for (Map.Entry<String, String> fileEntry : files.entrySet()) {
            String filename = fileEntry.getKey();
            Files.deleteIfExists(Paths.get(CURRENT_DIR + filename));
        }
    }

    private static String normalizeFileExtension(final String extension) {
        if (extension == null || extension.isEmpty()) {
            return "";
        }

        if (extension.charAt(0) != '.') {
            return "." + extension;
        }

        return extension;
    }

    public static String getNonexistentFilePath(String extension) {
        extension = normalizeFileExtension(extension);

        int i = 0;

        while (true) {
            final String fileName = TEMP_FILE_PREFIX + i + extension;
            final Path path = Paths.get(CURRENT_DIR + fileName);

            if (!RETURNED_NONEXISTENT_FILES.contains(fileName) && Files.notExists(path)) {
                RETURNED_NONEXISTENT_FILES.add(fileName);
                return path.toAbsolutePath().toString();
            } else {
                ++i;
            }
        }
    }

    public static String getNonexistentFilePath() {
        return getNonexistentFilePath(null);
    }

    public static String readFile(String name) {
        try {
            Path path = Paths.get(CURRENT_DIR + name);
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            //return Files.readString(path); <- Java 11
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public static ExecutorService startThreads(List<Process> processes) {
        int poolSize = processes.size();
        if (poolSize == 0) {
            return null;
        }
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (Process process : processes) {
            process.start();
            executor.submit(process);
            while (!process.isStarted()) {
                sleep(10);
            }
        }
        return executor;
    }

    public static void stopThreads(List<Process> processes, ExecutorService executor) {
        if (executor == null) {
            return;
        }
        try {
            for (Process process : processes) {
                process.stop();
            }
            executor.shutdown();
            boolean terminated = executor.awaitTermination(100, TimeUnit.MILLISECONDS);
            if (!terminated) {
                executor.shutdownNow();
                terminated = executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
                if (!terminated) {
                    System.err.println("SOME PROCESSES ARE NOT TERMINATED");
                }
            }
            for (int i = 1; i <= processes.size(); i++) {
                Process process = processes.get(i - 1);
                if (!process.isStopped()) {
                    System.err.println("PROCESS #" + i + " IS NOT TERMINATED");
                }
            }
        } catch (InterruptedException ex) {
            // ignored
        }
    }

    public static String getUrlPage(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String nextLine;
            String newLine = System.getProperty("line.separator");
            while ((nextLine = reader.readLine()) != null) {
                stringBuilder.append(nextLine);
                stringBuilder.append(newLine);
            }
            return normalizeLineEndings(stringBuilder.toString()).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String normalizeLineEndings(String str) {
        return str
            .replaceAll("\r\n", "\n")
            .replaceAll("\r", "\n");
    }
}

package com.yuhuan.processor;

import com.yuhuan.entity.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class GradlePackagingProcessor {

    public void compileScripts() throws IOException, InterruptedException {
        String command = Constants.gradle_bat_path.toAbsolutePath() + " packaging";
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.directory(new File(String.valueOf(Constants.gradle_path.toAbsolutePath())));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println("******************** 开始编译脚本文件 ********************");
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("exitCode=" + exitCode);
        System.out.println("******************** 脚本文件编译完成 ********************");
    }

    public void copyOapFileToCompileDirectory(Path targetPath) throws IOException {
        try (Stream<Path> stream = Files.walk(Constants.oap_file_path)) {
            stream.filter(p -> p.toString().endsWith(".oap"))
                    .forEach(p -> {
                        try {
                            Files.copy(p, targetPath.resolve(p.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}


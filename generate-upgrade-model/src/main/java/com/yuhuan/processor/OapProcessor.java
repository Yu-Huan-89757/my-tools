package com.yuhuan.processor;

import com.yuhuan.entity.Constants;
import com.yuhuan.utils.PacketOperateUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OapProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(OapProcessor.class);

    public void processorOapFile(Path sourcePath) throws Exception {
        LOG.info("******************** 开始处理脚本文件 ********************");
        cleanScriptAppsDirectory();
        copyFileToCompilePath(sourcePath);
        modifyScriptProjectXmlFile();
        LOG.info("******************** 脚本文件已处理完成 ********************");
    }

    private void cleanScriptAppsDirectory() throws IOException {
        try (Stream<Path> paths = Files.walk(Constants.script_apps_path, 1)) {
            paths.filter(Files::isDirectory)
                    .filter(path -> !path.equals(Constants.script_apps_path)) // 排除当前目录
                    .forEach(path -> {
                        try {
                            FileUtils.deleteDirectory(path.toFile());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private void copyFileToCompilePath(Path codePath) throws IOException {
        if (!Files.isDirectory(codePath)) {
            throw new IllegalArgumentException("Source must be a directory.");
        }

        File dir = new File(String.valueOf(codePath));

        // 列出当前目录下的所有文件和文件夹
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String destDirPath = String.valueOf(Constants.script_apps_path.resolve(file.getName()).toAbsolutePath());
                    File destDir = new File(destDirPath);
                    destDir.mkdirs();
                    FileUtils.copyDirectory(file.getAbsoluteFile(), destDir.getAbsoluteFile());
//                    OAP_FILE_PATH_MAP.put(file.getName(), codeCompilePath);
                }
            }
        }
    }

    private void modifyScriptProjectXmlFile() throws Exception {
        // 列出当前目录下的所有子文件夹
        List<Path> scriptDirectoryList;
        String projectXml = "project.xml";
        String verify = "VERIFY";
        try (Stream<Path> paths = Files.walk(Constants.script_apps_path, 1)) {
            scriptDirectoryList = paths.filter(Files::isDirectory)
                    .filter(path -> Files.exists(path.resolve(projectXml)))
                    .collect(Collectors.toList());

            for (Path path : scriptDirectoryList) {
                LOG.info("开始处理 " + path.getFileName() + " 脚本");
                PacketOperateUtils.encryptStringToFile(path.getFileName().toString(), new File(path + File.separator+ verify));
                LOG.info(path.getFileName() + " 脚本已处理完成，脚本名称为 " + path.getFileName() + "\n");
            }
        }
    }

}


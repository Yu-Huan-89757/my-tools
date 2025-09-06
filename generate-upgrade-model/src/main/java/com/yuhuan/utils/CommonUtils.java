package com.yuhuan.utils;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    public static boolean isJarEnvironment() {
        ProtectionDomain protectionDomain = CommonUtils.class.getProtectionDomain();
        if (protectionDomain != null) {
            // 获取CodeSource
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                if (location != null) {
                    String path = location.getPath();
                    if (path.endsWith("jar")) {
                        LOG.info("Running from JAR file.");
                        return true;
                    }
                } else {
                    LOG.info("CodeSource location is null.");
                }
            } else {
                LOG.info("CodeSource is null.");
            }
        } else {
            LOG.info("ProtectionDomain is null.");
        }
        return false;
    }

    public static String substringBeforeSecondLastUnderscore(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        int lastUnderscoreIndex = s.lastIndexOf('_');
        if (lastUnderscoreIndex == -1) {
            return s; // 没有 "_" 的情况
        }

        int secondLastUnderscoreIndex = s.lastIndexOf('_', lastUnderscoreIndex - 1);
        if (secondLastUnderscoreIndex == -1) {
            return s; // 只有一个 "_" 的情况
        }

        return s.substring(0, secondLastUnderscoreIndex);
    }
}

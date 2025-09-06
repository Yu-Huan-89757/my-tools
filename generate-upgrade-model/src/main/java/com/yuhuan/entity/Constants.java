package com.yuhuan.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import com.yuhuan.utils.CommonUtils;

public class Constants {
    public static Path gradle_bat_path;
    public static Path gradle_path;
    public static Path oap_file_path;
    public static Path script_apps_path;

    static {
       if(CommonUtils.isJarEnvironment()){
           gradle_bat_path = Paths.get( "gradle-8.13", "bin", "gradle.bat");
           gradle_path = Paths.get("gradle");
           oap_file_path = Paths.get( "gradle", "build", "version", "encrypt");
       } else {
            // gradle_bat_path = Paths.get("gradle-8.13", "bin", "gradle.bat");
            // gradle_path = Paths.get("gradle");
            // oap_file_path = Paths.get("gradle", "build", "version", "encrypt");
        
            try {
                gradle_bat_path = Path.of(new URI("file:///D:/soft/OSD_OSE/gradle-8.13/bin/gradle.bat"));
                gradle_path = Path.of(new URI("file:///D:/soft/OSD_OSE/gradle"));
                oap_file_path = Path.of(new URI("file:///D:/soft/OSD_OSE/gradle/build/version/encrypt"));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
       }
        script_apps_path = gradle_path.resolve("script_apps");
    }


    public static final ObservableList<String> DEVICE_SERIES = FXCollections.observableArrayList(
            "ZXCTN 61(V3)",
            "ZXCTN 61(V5)",
            "ZXCTN 61(V5.1)",
            "ZXR10 230",
            "ZXR10 230G+",
            "ZXR10 260",
            "ZXR10 280",
            "ZXR10 1800-2S",
            "ZXR10 2800-4",
            "ZXR10 3800-8",
            "ZXR10 5800",
            "ZXR10 6800E",
            "ZXR10 900E",
            "ZXR10 2800-4X",
            "ZXR10 2800-EX",
            "ZXR10 3800-8X",
            "ZXR10 5200F",
            "ZXR10 5260-S",
            "ZXR10 5260",
            "ZXR10 5800X",
            "ZXR10 5900D",
            "ZXR10 5900E",
            "ZXR10 5900G",
            "ZXR10 5950-E",
            "ZXR10 5950-H",
            "ZXR10 5950-L",
            "ZXR10 5950-S",
            "ZXR10 5950",
            "ZXR10 5960-HC",
            "ZXR10 5960",
            "ZXR10 5960M",
            "ZXR10 5960X",
            "ZXR10 5970",
            "ZXCTN 6700",
            "ZXR10 6800-6X",
            "ZXR10 6800-10X",
            "ZXONE 7000E",
            "ZXR10 8900E-H",
            "ZXR10 8900E",
            "ZXCTN 9000E",
            "ZXONE 9700E",
            "ZXR10 9900",
            "ZXR10 9900X&5960X-ES",
            "ZXONE 16700",
            "ZXONE 19700",
            "ZXCTN 9000-2E8",
            "ZXR10 C89E",
            "ZXMP M721E",
            "ZXR10 M6000-S",
            "ZXR10 M6000-2S(IPN)",
            "ZXR10 M6000-2S",
            "ZXR10 T8000",
            "ZXR10 V6000"
    );

    private static LinkedHashMap<String, Integer> UPGRADE_SCENARIO_SORTED = new LinkedHashMap<>();
    public static ObservableMap<String, Integer> UPGRADE_SCENARIO = FXCollections.observableMap(UPGRADE_SCENARIO_SORTED);

    public static final ObservableList<String> UPGRADE_TYPE = FXCollections.observableArrayList("version", "hotPatch", "coldPatch", "patchSet", "version&hotPatch", "version&coldPatch", "version&patchSet", "hotpatch&patchset", "coldpatch&patchset", "hotpatch&coldpatch", "hotpatch&coldpatch&patchset", "version&hotpatch&patchset", "version&coldpatch&patchset", "version&hotpatch&coldpatch&patchset", "version&hotpatch&coldpatch");

    public static final ObservableList<String> PROFESSIONAL_NET = FXCollections.observableArrayList("IPN", "ITN", "OTN");

    private static LinkedHashMap<String, Integer> COMPARE_SCENE_CONFIG_SORTED = new LinkedHashMap<>();

    static {
        UPGRADE_SCENARIO_SORTED.put("0-单机或堆叠场景", 0);
        UPGRADE_SCENARIO_SORTED.put("1-MC-LAG成对升级", 1);
        UPGRADE_SCENARIO_SORTED.put("2-中断业务升级", 2);
        UPGRADE_SCENARIO_SORTED.put("3-不中断业务升级（ISSU）", 3);
        UPGRADE_SCENARIO_SORTED.put("4-一键升级", 4);
        UPGRADE_SCENARIO_SORTED.put("5-不中断业务软复位升级（SOFTISSU）", 5);
        UPGRADE_SCENARIO_SORTED.put("7-vBRAS升级", 7);
        UPGRADE_SCENARIO_SORTED.put("8-MC-LAG单边升级", 8);
        UPGRADE_SCENARIO_SORTED.put("9-双机升级", 9);

        COMPARE_SCENE_CONFIG_SORTED.put("不展示对比场景", 0);
        COMPARE_SCENE_CONFIG_SORTED.put("对比场景单选", 1);
        COMPARE_SCENE_CONFIG_SORTED.put("对比场景多选", 2);
    }


    public static ObservableMap<String, Integer> COMPARE_SCENE_CONFIG = FXCollections.observableMap(COMPARE_SCENE_CONFIG_SORTED);
}

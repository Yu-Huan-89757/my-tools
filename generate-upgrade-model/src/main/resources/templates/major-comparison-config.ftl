{
    "icp-template": "/M6000-S/cmp/Major Operation Comparison/info.rb",
    "compare-scenes": [
        {
            "compare-scene": "healthCheck",
            "display-name": {
                "zh-CN": "监控检查",
                "en-US": "healthCheck"
            },
            "default-options": [
                "/M6000-S/cmp/Major Operation Comparison/healthCheck/14CheckShowSynchronization.rb",
                "/M6000-S/cmp/Major Operation Comparison/healthCheck/26CheckCurrentBoardAlarms.rb",
                "/M6000-S/cmp/Major Operation Comparison/healthCheck/6CheckVersetDirectory.rb"
            ]
        },
        {
            "compare-scene": "BRAS",
            "display-name": {
                "zh-CN": "BRAS场景",
                "en-US": "BRAS"
            },
            "default-options": [
                "/M6000-S/cmp/Major Operation Comparison/BRAS/73CheckAllUserStatistics.rb",
                "/M6000-S/cmp/Major Operation Comparison/BRAS/74CheckPppoxUserStatistics.rb",
                "/M6000-S/cmp/Major Operation Comparison/BRAS/75CheckIpoxUserStatistics.rb"
            ]
        },
        {
            "compare-scene": "SR",
            "display-name": {
                "zh-CN": "SR场景",
                "en-US": "SR"
            },
            "default-options": [
                "/M6000-S/cmp/Major Operation Comparison/SR/37CheckTotalArpQuantity.rb",
                "/M6000-S/cmp/Major Operation Comparison/SR/38CheckRoutesNumber.rb",
                "/M6000-S/cmp/Major Operation Comparison/SR/39CheckUplinkRoutesNumber.rb",
                "/M6000-S/cmp/Major Operation Comparison/SR/41CheckTcpConnectionStatus.rb"
            ]
        },
        {
            "compare-scene": "basic",
            "display-name": {
                "zh-CN": "基础场景",
                "en-US": "basic"
            },
            "default-options": [
                "/M6000-S/cmp/Major Operation Comparison/basic/12CheckSingleBoardCpuStatus.rb",
                "/M6000-S/cmp/Major Operation Comparison/basic/13CheckTemperatureDetail.rb",
                "/M6000-S/cmp/Major Operation Comparison/basic/15CheckDeviceClock.rb",
                "/M6000-S/cmp/Major Operation Comparison/basic/16CheckNtpSynchronizationStatus.rb"
            ]
        }
    ]
}

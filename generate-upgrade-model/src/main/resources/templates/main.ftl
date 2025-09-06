from nemo.log import logger
import time
import json

# 初始化 logger
logger = logger()

success_info = {
    "tipInfo": {
        "zh_CN": "该步骤已成功执行",
        "en_US": "This step is executed successfully."
    }
}

fail_info = {
    "tipInfo": {
        "zh_CN": "该步骤执行失败",
        "en_US": "This step is executed failed."
    }
}

def formatJson(data):
    return json.dumps(data, ensure_ascii=False)

def execute(**kw):
    logger.info(f'script log: ${project_name}')
    start_time = time.time()  # 记录起始时间
    progress = 0.0
    while True:
        progress_info = {
            "progress": f"{progress:.1f}%",
            "additionalInfo": {
                "en-US": "English Prompt: Here's the prompt in English",
                "zh-CN": "中文提示：这里是中文提示内容"
            }
        }
        
        if progress <= 98.0:
            progress += 1.0
        logger.info(f"progress-info: {formatJson(progress_info)}")
        time.sleep(1)
        if (time.time() - start_time) >= ${cost_time}:  # 和起始时间比较，看下是否已经达到预期耗时时间。如果达到就返回
            break

    if ${expect_result}:
        return formatJson(success_info)
    else:
        raise Exception(formatJson(fail_info))


import re
import time
import ftplib
import json
from nemo.log import logger
from api.connection import ConnectionUtils
from api.common import *
from api.i18n import I18n
from api import utils

log = logger()
i18n = I18n()

def execute(**kw):
    log.info(str(kw))
    ne_info_list = kw['ne_info']
    log.info(str(ne_info_list))
    ne_check_result = []
    # 这里假设ne_info_list是一个字符串，将其转换为JSON对象
    ne_info_list_json = json.loads(ne_info_list)
    log.info(type(ne_info_list_json))
    log.info(str(ne_info_list_json))
    for ne_info in ne_info_list_json['ne_info']:#拿到任务下所有分组
        log.info(str(ne_info))
        one_group_result = {}
        one_group_result['group_id'] = ne_info['group_id']
        one_group_result['ne_info_list'] = []
        #拿到分组下所有网元
        for ne_ip in ne_info['ne_ip_str']:
            one_ne_check_result = {}
            one_ne_check_result['ne_ip'] = ne_ip
            one_ne_check_result['ne_upgrade_order'] = 1 #自己设置升级顺序
            one_ne_check_result['ne_ip_type'] = 'master' #自己设置升级角色 master、slave、not_master_slave
            one_group_result['ne_info_list'].append(one_ne_check_result)
        ne_check_result.append(one_group_result)
    log.info(str(ne_check_result))
    result = {'ne_check_result':ne_check_result}
    log.info(str(result))
    return json.dumps(result)

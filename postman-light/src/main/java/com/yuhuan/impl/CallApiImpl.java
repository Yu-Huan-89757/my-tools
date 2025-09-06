package com.yuhuan.impl;

import com.yuhuan.api.ICallApi;
import com.yuhuan.utils.HttpClientUtils;
import com.yuhuan.helper.HttpMethod;
import com.yuhuan.entity.SshInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class CallApiImpl implements ICallApi {
    private final static Logger LOG = LoggerFactory.getLogger(CallApiImpl.class);

    private SshInfo sshInfo;
    public CallApiImpl(SshInfo sshInfo) {
        this.sshInfo = sshInfo;
    }

    @Override
    public String doRequest(HttpMethod httpMethod, String url, String params, Map<String, String> headers) throws IOException {
        LOG.info("doRequest-httpMethod:{}, url: {}, params: {}", httpMethod.name(), url, params);
        String res = null;
        switch (httpMethod) {
            case GET:
                res = HttpClientUtils.doGet(url, params, headers, sshInfo);
                break;
            case POST:
                res = HttpClientUtils.doPost(url, params, headers, sshInfo);
                break;
            case DELETE:
                res = HttpClientUtils.doDelete(url, params, headers, sshInfo);
                break;
            case PUT:
                res = HttpClientUtils.doPut(url, params, headers,sshInfo);
                break;
            default:
                System.out.println("不支持的HTTP请求方式！！！");
                break;
        }
        return res;
    }

}

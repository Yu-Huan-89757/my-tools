package com.yuhuan.impl;

import com.yuhuan.entity.SshInfo;
import com.yuhuan.helper.HttpMethod;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Map;

public class PostmanCallApiImpl extends CallApiImpl{
    private TextArea logArea;

    public PostmanCallApiImpl(SshInfo sshInfo, TextArea logArea) {
        super(sshInfo);
        this.logArea = logArea;
    }

    @Override
    public String doRequest(HttpMethod httpMethod, String url, String params, Map<String, String> headers) throws IOException {
        StringBuilder sb = new StringBuilder(logArea.getText().toString());
        logArea.setText(sb.append("doRequest-httpMethod: "+httpMethod.name()+", url: "+url+", params: \n"+params).toString());
        return super.doRequest(httpMethod, url, params, headers);
    }
}

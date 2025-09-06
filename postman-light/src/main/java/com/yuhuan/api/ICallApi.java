package com.yuhuan.api;

import com.yuhuan.helper.HttpMethod;

import java.io.IOException;
import java.util.Map;

public interface ICallApi {
    String doRequest(HttpMethod httpMethod, String url, String params, Map<String, String> headers) throws IOException;
}

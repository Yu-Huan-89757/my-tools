package com.yuhuan.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhuan.entity.SshInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtils {
    private final static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    // 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
    static {
        httpClient = createSSLClientDefault();
    }

    private HttpClientUtils() {}

    public static String getLoginToken(SshInfo sshInfo) throws IOException {
        String url ="https://" + sshInfo.getIp() + ":28001/api/oauth2/v1/security/authentication/token";
        Map<String,String> params = new HashMap<>();
        params.put("userName", sshInfo.getUsername());
        params.put("value", sshInfo.getPwd());
        ObjectMapper om = new ObjectMapper();
        String s = doPost(url, om.writeValueAsString(params), null,null);
        if(StringUtils.isNotBlank(s)){
            JsonNode jsonNode = om.readTree(s);
            return jsonNode.get("X-Subject-Token").asText();
        }
        throw new RuntimeException("获取token信息失败，请检查ip地址、用户名、密码是否匹配！");
    }


    public static String doPost(String url, String params, Map<String, String> headers, SshInfo sshInfo) throws IOException {
        LOG.info("doPost-url: {}, params: {}, headers: {}", url, params, headers);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        if(StringUtils.isNotBlank(params)) {
            httpPost.setEntity(new StringEntity(params, CHARSET));
        }
        setHeaders(sshInfo, headers, httpPost);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort();
            throw new RuntimeException("HttpClient error status code: " + statusCode + ", url: "+url);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        return result;
    }

    /**
     * HTTPS Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param sshInfo 登录信息
     * @return 页面内容
     */
    public static String doGet(String url, String params, Map<String, String> headers, SshInfo sshInfo) throws IOException {
        LOG.info("doGet-url: {}, params: {}", url, params);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if(StringUtils.isNotBlank(params)) {
            url += "?" + params;
        }
        HttpGet httpGet = new HttpGet(url);
        setHeaders(sshInfo, headers, httpGet);
        // https  注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort();
            throw new RuntimeException("HttpClient error status code: " + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    public static String doDelete(String url, String params, Map<String, String> headers, SshInfo sshInfo) throws IOException {
        LOG.info("doDelete-url: {}, params: {}", url, params);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if(StringUtils.isNotBlank(params)) {
            url += "?" + params;
        }
        HttpDelete httpDelete = new HttpDelete(url);
        setHeaders(sshInfo, headers, httpDelete);
        // https  注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 204) {
            httpDelete.abort();
            throw new RuntimeException("HttpClient error status code: " + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    public static String doPut(String url, String params, Map<String, String> headers, SshInfo sshInfo) throws IOException {
        LOG.info("doDelete-url: {}, params: {}", url, params);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if(StringUtils.isNotBlank(params)) {
            url += "?" + params;
        }
        HttpPut httpPut = new HttpPut(url);
        setHeaders(sshInfo, headers, httpPut);
        // https  注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
        CloseableHttpResponse response = httpClient.execute(httpPut);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 204) {
            httpPut.abort();
            throw new RuntimeException("HttpClient error status code: " + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }


    private static void setHeaders(SshInfo sshInfo, Map<String, String> headers, HttpRequestBase httpRequestBase) throws IOException {
        if(null!= sshInfo) {
            if(sshInfo.isUseCookie()) {
                httpRequestBase.addHeader(new BasicHeader("cookie", sshInfo.getCookie()));
                httpRequestBase.addHeader(new BasicHeader("forgerydefense", sshInfo.getForgerydefense()));
            } else {//token
                httpRequestBase.setHeader("X-Subject-Token", getLoginToken(sshInfo));
            }
        }
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> {
                httpRequestBase.setHeader(new BasicHeader(k, v));
            });
        }else {
            httpRequestBase.setHeader("content-type", "application/json;charset=utf-8");
        }
    }

    /**
     * 创建忽略整数验证的 CloseableHttpClient 对象
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            // 信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return HttpClients.createDefault();
    }

}

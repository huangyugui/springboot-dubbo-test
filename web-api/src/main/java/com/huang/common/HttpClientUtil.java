package com.huang.common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * http工具
 *
 * @author 张贺
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    private static final CloseableHttpClient httpClient = createHttpClient();


    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient createHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(30000)
                .build();
        return HttpClients.custom().setDefaultRequestConfig(config)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build();
    }


    private static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, String> headers,
                                        Map<String, String> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);
        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, String> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), param.getValue());
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    /**
     * 表单方式提交
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, String> headers,
                                         Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, String> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), param.getValue());
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }


    /**
     * post方式,调用restful服务.
     *
     * @param url
     * @param json
     * @return
     */
    public static String httpPostRestRequest(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, HTTP.UTF_8);
        entity.setContentType(Content_Types.application_x_www_form_urlencoded);
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }


    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, String> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        return pairs;
    }

    /***
     * form表单上传文件
     * @param binParams
     * @param txtParams
     * @return
     */
    public static String imgFileUpload(String url, Map<String, byte[]> binParams, Map<String, String> txtParams) {
        logger.info("上传文件：url=" + url + " input " + txtParams);
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //批量的图片文件
        for (Map.Entry<String, byte[]> binParam : binParams.entrySet()) {
            String key = binParam.getKey();
            String name = key.split("\\$")[0];
            String fileName = key.split("\\$")[1];
            //2进制内容
            byte[] bytes = binParam.getValue();
            builder.addBinaryBody(name, bytes, ContentType.DEFAULT_BINARY, fileName);
        }


        for (Map.Entry<String, String> txtParam : txtParams.entrySet()) {
            String key = txtParam.getKey();
            //文本内容
            String message = txtParam.getValue();
            builder.addTextBody(key, message, ContentType.TEXT_PLAIN);
        }
        //entity构造
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        return getResult(post);
    }

    /**
     * 长亮影像上传的参数
     *
     * @param appNo
     * @return
     */
    public static Map<String, String> txtParam(String appNo) {
        Map<String, String> txtParams = new HashMap<String, String>();
        txtParams.put("appNo", appNo);
        txtParams.put("if_patch_bolt", "Y");
        txtParams.put("sysName", "mj");
        return txtParams;
    }


    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        try (CloseableHttpResponse response = httpClient.execute(request);) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return result;
            }
        } catch (Exception e) {
            logger.error(" httpClient execute error :", e);
        }
        return EMPTY_STR;
    }

    /**
     * post方式,调用restful服务.
     *
     * @param url
     * @param json
     * @return
     */
    public static String httpPostJsonRestRequest(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, HTTP.UTF_8);
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

    /**
     * POST方式,JSON参数调用服务.
     *
     * @param url  调用地址
     * @param json JSON参数
     * @return response结果
     */
    public static String httpPostJsonRequest(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

}  
package cn.wolfcode.sso.util;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {

    public static String sendHttpRequest(String httpURL, Map<String, String> params) throws Exception {
        //建立URL连接对象
        URL url = new URL(httpURL);
        //建立连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求的方式 需要是大写的
        connection.setRequestMethod("POST");
        //设置需要输出
        connection.setDoOutput(true);
        if (params != null && params.size()>0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            //sb.substring(1)去除最前面的&
            connection.getOutputStream().write(sb.substring(1).toString().getBytes("utf-8"));
        }
        //发送请求到服务器
        connection.connect();
        //获取远程响应的内容
        String responseContent = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("utf-8"));
        connection.disconnect();
        return responseContent;
    }

    /**
     * 模拟浏览器的请求
     * @param httpURL
     * @param jsessionId
     * @return
     */
    public static void sendHttpRequest(String httpURL, String jsessionId) throws Exception {
        //建立URL 连接对象
        URL url = new URL(httpURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Cookie","JSESSION="+jsessionId);
        connection.connect();
        connection.getInputStream();
        connection.disconnect();
    }

    public static void sendHttpRequest(String httpUrl) throws Exception {
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.getInputStream();
        connection.disconnect();
    }
}

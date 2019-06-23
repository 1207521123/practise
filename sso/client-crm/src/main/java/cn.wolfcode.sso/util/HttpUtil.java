package cn.wolfcode.sso.util;

import org.springframework.util.StreamUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {

    /**
     * 模拟浏览器的请求
     * @param httpURL 发送请求的地址
     * @param params  请求参数
     * @return
     */
    public static String sendHttpRequest(String httpURL, Map<String,String> params) throws Exception {
        //建立URL连接对象
        URL url = new URL(httpURL);
        //创建连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求的方式 需要大写
        connection.setRequestMethod("POST");
        //设置需要输出
        connection.setDoOutput(true);
        //判断是否有参数
        if( params != null && params.size()>0){
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String,String> entry:params.entrySet()){
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            //sb.substring(1)去掉最前面的&
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
     * @param httpURL 发生请求的地址
     * @param jessionId 会话 Id
     *
     */
    public static void sendHttpRequest(String httpURL, String jessionId) throws Exception {
       //建立URL连接对象
       URL url = new URL(httpURL);
       //创建连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求的方式  需要大写
        connection.setRequestMethod("POST");
        //设置需要输出
        connection.setDoOutput(true);
        connection.addRequestProperty("Cookie","JSESSIONID="+jessionId);
        //发送请求到服务期
        connection.connect();
        connection.getInputStream();
        connection.disconnect();
    }
}

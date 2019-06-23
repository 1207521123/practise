package cn.wolfcode.sso.listener;

import cn.wolfcode.sso.util.HttpUtil;
import cn.wolfcode.sso.util.MockDatabaseUtil;
import cn.wolfcode.sso.vo.ClientInfoVo;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

public class MySessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        String token = (String) session.getAttribute("token");
        //删除 t_token 表中的数据
        MockDatabaseUtil.T_TOKEN.remove(token);
        List<ClientInfoVo> clientInfoVoList = MockDatabaseUtil.T_CLIENT_INFO.remove(token);
        try {
            if (clientInfoVoList != null) {
                for (ClientInfoVo vo : clientInfoVoList) {
                    //获取出注册的子系统，依次调用子系统的登出方法
                    HttpUtil.sendHttpRequest(vo.getClientUrl(),vo.getJsessionId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

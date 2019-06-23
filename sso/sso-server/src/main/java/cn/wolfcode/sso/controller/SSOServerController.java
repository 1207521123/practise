package cn.wolfcode.sso.controller;

import cn.wolfcode.sso.util.HttpUtil;
import cn.wolfcode.sso.util.MockDatabaseUtil;
import cn.wolfcode.sso.vo.ClientInfoVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class SSOServerController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/checkLogin")
    public String checkLogin(String redirectUrl, HttpSession session, Model model) {
        //1.判断是否有全局的会话
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)) {
            //表示没有全局会话
            //跳转到统一认证中心的登录页面
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        } else {
            //有全局会话
            //取出令牌信息，重定向到redirectUrl,把令牌带上 http://www.wms.com:8089/main?token
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl;
        }
    }

    /**
     * 登录功能
     * @return
     */
    @RequestMapping("/user_login")
    public String login(String username, String password, String redirectUrl, HttpSession httpSession, Model model){
        if ("admin".equals(username)&&"123456".equals(password)) {
            //账号密码匹配
            //1.创建令牌信息
            String token = UUID.randomUUID().toString();
            //2.创建全局的会话，把令牌信息放入会话中，
            httpSession.setAttribute("token",token);
            //3.需要把令牌信息放到数据库中。
            MockDatabaseUtil.T_TOKEN.add(token);

            if (StringUtils.isEmpty(redirectUrl)) {
                return "redirect:/";
            }
            //4.重定向到redirectUrl,把令牌信息带上。 http://www.crm.com:8088/main?token=
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl;
        }
        //如果账号密码有误
        if(!StringUtils.isEmpty(redirectUrl)){
            model.addAttribute("redirectUrl",redirectUrl);
        }
        return "redirect:login";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token, String clientUrl, String jsessionId) {
        if (MockDatabaseUtil.T_TOKEN.contains(token)) {
            //把客户端的登出地址记录
            List<ClientInfoVo> clientInfoVoList = MockDatabaseUtil.T_CLIENT_INFO.get(token);
            if (clientInfoVoList == null) {
                clientInfoVoList = new ArrayList<ClientInfoVo>();
                MockDatabaseUtil.T_CLIENT_INFO.put(token,clientInfoVoList);
            }
            ClientInfoVo vo = new ClientInfoVo();
            vo.setClientUrl(clientUrl);
            vo.setJsessionId(jsessionId);
            clientInfoVoList.add(vo);
            //说明令牌有效，返回true
            return "true";
        }
        return "false";
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        //销毁全局会话
//        String token = (String) session.getAttribute("token");
        session.invalidate();
//        List<ClientInfoVo> list= MockDatabaseUtil.T_CLIENT_INFO.get(token);
//        if(list != null){
//            for(ClientInfoVo clientInfoVo : list){
//                try {
//                    HttpUtil.sendHttpRequest(clientInfoVo.getClientUrl());
//                } catch (Exception e) {
//                    System.out.println(clientInfoVo);
//                    e.printStackTrace();
//                }
//            }
//        }
        return "redirect:login";
    }
}

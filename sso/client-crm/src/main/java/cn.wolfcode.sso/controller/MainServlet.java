package cn.wolfcode.sso.controller;

import cn.wolfcode.sso.util.SSOClientUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="mainServlet",urlPatterns = "/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
        this.doPost(req,res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
        req.setAttribute("serverLogOutUrl", SSOClientUtil.getServerLogOutUrl());
        req.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(req,res);
    }
}

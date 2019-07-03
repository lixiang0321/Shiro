package com.yootk.shiro.servlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout.servlet")
public class MemberLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String pash = "/login.jsp";
       try {
           Subject subject = SecurityUtils.getSubject();
           System.out.println(subject+"subject");
           subject.logout();;
       }catch (Exception e){
            e.printStackTrace();
       }
       req.getRequestDispatcher(pash).forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}

package com.sina.controller;
import com.sina.dao.ManagerMapper;
import com.sina.entity.Manager;
import com.sina.service.ManagerService;
import com.sina.status.PasswordStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("manager")
@Slf4j
public class ManagerController {
    @Resource
    private ManagerService managerService;
    @Resource
    private ManagerMapper managerMapper;
    @RequestMapping("login")
    public String login( boolean rememberMe, String rcode, HttpServletRequest request, String name, String password) throws UnsupportedEncodingException {
        System.out.println("进入登录");
        //这是我后加的
            //首先是获取验证码
        HttpSession session = request.getSession();
        String code =(String) session.getAttribute("code");

        if(rcode.equals(code)){
            Manager manager = managerMapper.selectOneByUsername(name);
            session.setAttribute("manager",manager);
            //获取主体对象
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(name,password,rememberMe));
                System.out.println("跳转前");
                session.setAttribute("username",name);
                return "redirect:/main/main.jsp";

        }else{
            return "redirect:/login.jsp";
        }
    }
    //退出
    @RequestMapping("exitLogin")
    public String exitLogin(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login.jsp";
    }
    //密码修改
    @RequestMapping("confirm")
    @ResponseBody
    public PasswordStatus confirm(HttpServletRequest request,String pwd,String newpwd){
        System.out.println(pwd+"     "+newpwd);
        PasswordStatus passwordStatus = new PasswordStatus();
        HttpSession session = request.getSession();
        Manager manager =(Manager) session.getAttribute("manager");
        if(pwd.equals(manager.getPassword())){
            System.out.println(manager);
            manager.setPassword(newpwd);
            System.out.println(manager);
            managerService.change(manager);
            passwordStatus.setStatus(true);
            passwordStatus.setStatusmessage("修改密码成功");
            System.out.println(passwordStatus);
            return passwordStatus;
        }else{
            passwordStatus.setStatus(false);
            passwordStatus.setStatusmessage("验证密码输入不正确");
            return passwordStatus;
        }
    }

}

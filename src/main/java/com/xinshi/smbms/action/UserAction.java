package com.xinshi.smbms.action;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionSupport;
import com.xinshi.smbms.pojo.Page;
import com.xinshi.smbms.pojo.Role;
import com.xinshi.smbms.pojo.User;
import com.xinshi.smbms.service.UserService;
import com.xinshi.smbms.service.impl.UserServiceImpl;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@Controller("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport {
    private String userCode;        //用户名
    private String userPassword;    //密码
    //实现用户业务层
    @Resource(name = "userService")
    private UserService userService;
    //接收分页集合
    private Page<User> userList;
    //接收用户名
    private String queryname;
    //获取下一页的值
    private int pageIndex=1;
    //声明用户对象
    private  User user=null;
    //接收角色集合
    private List<Role> roleList;
    //接收用户角色
    private int queryUserRole;
    private int uid;

    /**
     * 登录功能
     * @return
     */
    public String loginUser(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ctx.getBeanDefinitionNames();
        User userCodeOrPwd = userService.findUserCodeOrPwd(userCode, userPassword);
        if(userCodeOrPwd!=null){
            ServletActionContext.getRequest().getSession().setAttribute("userSession",userCodeOrPwd);
            ServletActionContext.getRequest().getSession().setMaxInactiveInterval(3*60);
            User  u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
            return  SUCCESS;
        }
        return LOGIN;
    }

    /**
     * 退出登录
     * @return
     */
    public String logoutUser(){
        ServletActionContext.getRequest().getSession().removeAttribute("userSession");
        return LOGIN;
    }

    /**
     * 修改密码
     * @return
     */
    public String updatepassWordByUser(){
        User  u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
        if(userService.updatePassWord(newpassword,u.getId())  != 0){
            return LOGIN;
        };
        return null;
    }
    private String oldpassword; //旧密码
    private String newpassword; //新密码

    /**
     * 验证旧密码是否正确
     *
     * @return
     */
    public String verifyBywornPwdUser(){
        User  u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            User result = userService.findByPassWord(oldpassword,u.getId());
           if(oldpassword.equals("")  || oldpassword == null){
                writer.write("{\"result\":\"error\"}");
           }else{
               if(result != null ){
                   writer.write("{\"result\":\"true\"}");
               }else if(result  == null){
                   writer.write("{\"result\":\"false\"}");
               }else{
                   writer.write("{\"result\":\"sessionerror\"}");
               }
           }
           writer.flush();
           writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户分页
     * @return
     */
    public String pageUser(){
        user=new User();
        user.setUsername(queryname);
        user.setUserrole(queryUserRole);
        userList = userService.findPageUsers(user, pageIndex, 5);
        //查询角色全部的值
        roleList = userService.findAllRoleName();
        if(userList!=null){
            return "find";
        }
       return SUCCESS;
    }
    /**
     * 查找单个用户信息
     * @return
     */
    public String findByIDUser(){
        user= userService.selectByPrimaryKey(uid);
        if(user!=null){
            return "view";
        }
        return "find";
    }

    /**
     * 根据用户ID 删除
     * @return
     */
    public String deleteByIDUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            StringBuffer sb=new StringBuffer();
            if(userService.deleteByIDUser(user.getId())>0){
                sb.append("{\"delResult\":\"true\"}");
            }else{
                sb.append("{\"delResult\":\"false\"}");
            }
            writer.print(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据用户 ID 查看 显示修改信息
     * @return
     */
    public String queryByIDUser(){
        user = userService.selectByPrimaryKey(uid);
        //查询角色全部的值
        roleList = userService.findAllRoleName();
        return "query";
    }

    /**
     * 修改用户信息
     * @return
     */
    public String updateByUser(){
        User  u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
        if(u!=null){
            user.setModifyby(u.getId());
        }
        user.setModifydate(new Date());
       if(userService.uodateByUser(user)>0){
           return pageUser();
       }
       return queryByIDUser();
    }

    /**
     *  查询所有的角色信息  使用ajax 输出
     * @return
     */
    public String RoleListUser(){
        roleList = userService.findAllRoleName();
        //把roleList转换成json对象输出
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        //把roleList转换成json对象输出
        PrintWriter outPrintWriter = null;
        try {
            outPrintWriter = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
        return "";
    }

    /**
     *增加用户
     * @return
     */
    public String addByUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        System.out.println(user);
        user.setCreationdate(new Date());
        if(userService.addUser(user)>0){
            return pageUser();
        };
        return "registerUser";
    }

    /**
     * 验证用户编码是否存在
     * @return
     */
    public String findByuserCodeUser(){
        User byUserCode = userService.findByUserCode(user.getUsercode());
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            StringBuffer sb=new StringBuffer();
            if(byUserCode!=null){
                sb.append("{\"userCode\":\"exist\"}");
            }else{
                sb.append("{\"userCode\":\"false\"}");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getQueryUserRole() {
        return queryUserRole;
    }

    public void setQueryUserRole(int queryUserRole) {
        this.queryUserRole = queryUserRole;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Page<User> getUserList() {
        return userList;
    }

    public void setUserList(Page<User> userList) {
        this.userList = userList;
    }

    public String getQueryname() {
        return queryname;
    }

    public void setQueryname(String queryname) {
        this.queryname = queryname;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

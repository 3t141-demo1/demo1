package com.xinshi.smbms.action;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.xinshi.smbms.pojo.Bill;
import com.xinshi.smbms.pojo.Page;
import com.xinshi.smbms.pojo.Provider;
import com.xinshi.smbms.pojo.User;
import com.xinshi.smbms.service.BillService;
import com.xinshi.smbms.service.ProviderService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("providerAction")
@Scope("prototype")
public class ProviderAction extends ActionSupport {
    //业务层供应商
    @Autowired
    @Qualifier("providerService")
    private ProviderService providerService;
    @Resource
    private BillService billService;
    //接收所有的供应商的数据
    private Page<Provider> providerList;
    //供应商对象
    private Provider provider;
    private int pageIndex=1;

    /**
     * 根据供应商ID 查看单个供应商的信息
     * @return
     */
    public String queryByIDUpdateProvider(){
        System.out.println(provider);
        provider = providerService.findProById(provider);
        return "update";
    }

    /**
     * 修改供应商信息
     * @return
     */
    public String updateByProvider(){
        User u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
        provider.setModifydate(new Date());
        provider.setModifyby(u.getId());
        //不等于0 就成功
        if(providerService.updateByPrimaryKeySelective(provider) != 0){
            provider =null;     //清空原有供应商的数据
            return allByProvider(); //刷新数据
        };
        //失败显示修改页面并查询根据供应商ID查显示到页面
        return  queryByIDUpdateProvider();
    }

    /**
     * 删除供应商
     * @return
     */
    public String deleteByProvider()  {
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = null;
            try {
                writer = response.getWriter();
                //判断有没有存在供应商
                if(!StringUtils.isNullOrEmpty(Integer.valueOf(provider.getId()).toString())){
                    Bill bill=new Bill();
                    bill.setProviderId(provider.getId());
                    //查询订单是否存在供应商ID  如果存在显示订单有多少订单，否则删除
                    int count = billService.countRowBill(bill);
                    if(count == 0){
                        int result = providerService.deleteByPrimaryKey(provider.getId());
                        if( result ==  1){      //删除成功
                            writer.write("{\"delResult\":\"true\"}");
                        }else if(result == -1){     //删除失败
                            writer.write("{\"delResult\":\"false\"}");
                        }
                    }else{
                        writer.write("{\"delResult\":\""+count+"\"}");
                    }
                }else{
                    writer.write("{\"delResult\":\"notexit\"}");
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return "";
    }

    /**
     * 增加供应商
     * @return
     */
    public String addByProvider(){
        User u =(User) ServletActionContext.getRequest().getSession().getAttribute("userSession");
        //创建时间
        provider.setCreationdate(new Date());
        //创建者
        provider.setCreatedby(u.getId());
        if(providerService.insertSelectiveProvider(provider) !=  0){
            provider = null;     //清空原有供应商的数据
            return "reload";
        }
        return "add";
    }

    /**
     * 查看单个供应商信息
     * @return
     */
    public String queryByIdProvider(){
        provider  = providerService.findProById(provider);
        return "list";
    }

    /**
     * 查询所有的供应商数据
     * @return
     */
    public  String allByProvider(){
        providerList= providerService.findProByCodeAndProName(provider,pageIndex,5);
        return SUCCESS;
    }


    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public Page<Provider> getProviderList() {
        return providerList;
    }

    public void setProviderList(Page<Provider> providerList) {
        this.providerList = providerList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}

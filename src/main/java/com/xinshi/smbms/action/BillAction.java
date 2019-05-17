package com.xinshi.smbms.action;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.xinshi.smbms.pojo.Bill;
import com.xinshi.smbms.pojo.Page;
import com.xinshi.smbms.pojo.Provider;
import com.xinshi.smbms.service.BillService;
import com.xinshi.smbms.service.ProviderService;
import com.xinshi.smbms.service.impl.BillServiceImpl;
import com.xinshi.smbms.service.impl.ProviderServiceimpl;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@Controller("billAction")
@Scope("prototype")
public class BillAction extends ActionSupport {
    //订单业务层
    @Autowired
    @Qualifier("billService")
    private BillService billService;
    //分页数据
    private Page<Bill> billPage;
    //分页的索引的值
    private int pageIndex=1;
    //订单对象
    private Bill bill;
    @Autowired
    @Qualifier("providerService")
    private ProviderService providerService;
    //接收所有的供应商信息
    private List<Provider> providerList;

    /**
     *  增加订单功能
     * @return
     */
    public String addBill(){
        //赋值创建时间
        bill.setCreationDate(new Date());
        //调用订单添加方法 > 0 成功 否则失败
        if(billService.addByBill(bill)!= 0){
            bill=null;  //清空原有订单数据
            return allPageBill();
        }
        return "add";
    }

    /**
     * 删除订单信息
     * @return
     */
    public String deleteByBill(){
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer =null;
        try {
            StringBuffer sb=new StringBuffer();
            //响应式获取输出流
            writer = response.getWriter();
            //设置编码格式
            response.setContentType("text/html;charset=utf-8");
            //执行删除方法等于0就成功
           if(billService.deleteBill(bill.getId())>0){
               //声明JSON 格式
               sb.append("{\"delResult\":\"true\"}");
           }else {
               sb.append("{\"delResult\":\"false\"}");
           }
           //输出拼接sb
           writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.flush();
            writer.close();
        }
        return null;
    }

    /**
     *  查询订单并显示修改页面
     * @return
     */
    public String findByIDShowBill(){
        bill = billService.findByIDBill(bill.getId());
        return "update";
    }

    /**
     * 修改订单信息
     * @return
     */
    public String updateByIDBill(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        if(billService.updateByBill(bill) != 0 ){
            bill=null;
            return allPageBill();
        }
        return findByIDBill();
    }

    /**
     * 使用ajax加载供应商信息
     * @return
     */
    public String  queryByBill(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            providerList = providerService.allProvider();
            String jsonString = JSON.toJSONString(providerList);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 根据订单ID 查询 订单详情
     * @return
     */
    public String findByIDBill(){
        bill = billService.findByIDBill(bill.getId());
        return "find";
    }
    /**
     * 订单分页
     * @return
     */
    public String allPageBill(){
        //根据条件查询订单信息 、 实现订单分页
        billPage = billService.findPageBill(bill,pageIndex , 5);
        //查询所有的供应商数据
        providerList = providerService.allProvider();
        if(billPage!=null){
            return SUCCESS;
        }
        return "error";
    }


    public ProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public List<Provider> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<Provider> providerList) {
        this.providerList = providerList;
    }

    public BillService getBillService() {
        return billService;
    }

    public void setBillService(BillService billService) {
        this.billService = billService;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Page<Bill> getBillPage() {
        return billPage;
    }

    public void setBillPage(Page<Bill> billPage) {
        this.billPage = billPage;
    }
}

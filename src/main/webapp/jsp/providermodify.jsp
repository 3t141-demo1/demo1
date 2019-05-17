<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/head.jsp"%>

  <div class="right">
      <div class="location">
          <strong>你现在所在的位置是:</strong>
          <span>供应商管理页面 >> 供应商修改页</span>
      </div>
      <div class="providerAdd">
          <form id="providerForm" name="providerForm" method="get" action="${pageContext.request.contextPath }/jsp/updateByProvider.action">
              <input type="hidden" name="provider.id" id="id" value="${provider.id }"/>
              <!--div的class 为error是验证错误，ok是验证成功-->
              <div class="">
                  <label for="proCode">供应商编码：</label>
                  <input type="text" name="provider.procode" id="proCode" value="${provider.procode }" readonly="readonly">
              </div>
              <div>
                  <label for="proName">供应商名称：</label>
                 <input type="text" name="provider.proname" id="proName" value="${provider.proname }">
			<font color="red"></font>
              </div>
              
              <div>
                  <label for="proContact">联系人：</label>
                  <input type="text" name="provider.procontact" id="proContact" value="${provider.procontact }">
			<font color="red"></font>
              </div>
              
              <div>
                  <label for="proPhone">联系电话：</label>
                  <input type="text" name="provider.prophone" id="proPhone" value="${provider.prophone }">
			<font color="red"></font>
              </div>
              
              <div>
                  <label for="proAddress">联系地址：</label>
                  <input type="text" name="provider.proaddress" id="proAddress" value="${provider.proaddress }">
              </div>
              
              <div>
                  <label for="proFax">传真：</label>
                  <input type="text" name="provider.profax" id="proFax" value="${provider.profax }">
              </div>
              
              <div>
                  <label for="proDesc">描述：</label>
                  <input type="text" name="provider.prodesc" id="proDesc" value="${provider.prodesc }">
              </div>
              <div class="providerAddBtn">
                  <input type="button" name="save" id="save" value="保存">
				  <input type="button" id="back" name="back" value="返回" >
              </div>
          </form>
      </div>
  </div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/providermodify.js"></script>
<!DOCTYPE html>
<html lang="en">
<#include "../common/head.ftl">
<body>

<div id="wrapper" class="toggled">
    <#--sidebar-->
    <#include "../common/nav.ftl">
    <#--主题内容-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <#--上部分表格-->
                <div class="col-md-6 column">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>订单id</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${orderDTO.orderId}</td>
                            <td>${orderDTO.orderAmount}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <#--下部分表格-->
                <div class="col-md-12 column">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>商品id</th>
                            <th>商品名称</th>
                            <th>价格</th>
                            <th>数量</th>
                            <th>总额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list orderDTO.orderDetailList as orderDetail>
                            <tr>
                            <td>${orderDetail.productId}</td>
                            <td>${orderDetail.productName}</td>
                            <td>${orderDetail.productPrice}</td>
                            <td>${orderDetail.productQuantity}</td>
                            <td>${orderDetail.productPrice * orderDetail.productQuantity}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-12 column">
                    <#if orderDTO.orderStatus==0>
                        <a type="button" class="btn btn-success btn-sm" href="/sell/seller/order/finish?orderId=${orderDTO.orderId}">完成订单</a>
                        <a type="button" class="btn btn-danger btn-sm" href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}">取消订单</a>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
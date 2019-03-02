<!DOCTYPE html>
<html lang="en">
<#include "head.ftl">
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-danger">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4>
                    成功！！
                </h4> <strong>${msg!''}</strong>3s后跳转~~~<a href="${returnUrl}" class="alert-link">点击跳转</a>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    setTimeout('location.href="${returnUrl}"', 3000);
</script>
</html>
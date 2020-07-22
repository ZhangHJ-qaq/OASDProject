<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="libraries/animate.css/animate.min.css">
    <link rel="stylesheet" href="libraries/sysalert/syalert.min.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/register_login.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="libraries/JavaScriptMD5/md5.js?version=2020-07-114"></script>
    <script src="js/class/RegisterPage.class.js"></script>
    <script src="js/register.js"></script>
</head>
<body>

<main class="card">
    <h4 class="card-header">注册</h4>
    <form class="card-body flex-container" id="form">
        <label class="flex-1">用户名</label>
        <input type="text" name="username" class="flex-1 form-control" id="userNameInput">
        <div id="usernameInfoArea" class="flex-1"></div>
        <label class="flex-1">电子邮箱</label>
        <input type="email" name="email" class="flex-1 form-control" id="emailInput">
        <div id="emailInfArea" class="flex-1"></div>
        <label class="flex-1">密码</label>
        <input type="password" name="password1" class="flex-1 form-control" id="password1Input">
        <label class="flex-16-24">密码安全性</label>
        <div id="password1ProgressArea" class="flex-1"></div>
        <label class="flex-1">确认密码</label>
        <input type="password" name="password2" class="flex-1 form-control" id="password2Input">
        <div id="password2InfoArea" class="flex-1"></div>
        <label class="flex-1">验证码</label>
        <div class="flex-1" id="captchaArea">
            <img src="getCaptcha" alt="captcha">
        </div>
        <input type="email" name="captcha" class="flex-1 form-control" id="captchaInput">
        <button type="button" class="btn btn-info flex-1" id="submitButton">注册</button>
        <div>已有帐号？<a href="login">登录！</a></div>
    </form>
</main>

<script src="libraries/sysalert/syalert.min.js"></script>

<div class="sy-alert sy-alert-tips animated" sy-enter="zoomIn" sy-leave="zoomOut" sy-type="tips" sy-mask="false"
     id="alertUsername">
    <div class="sy-content">用户名必须是4-15位</div>
</div>
<div class="sy-alert sy-alert-tips animated" sy-enter="zoomIn" sy-leave="zoomOut" sy-type="tips" sy-mask="false"
     id="alertEmail">
    <div class="sy-content">邮箱格式不符合要求</div>
</div>
<div class="sy-alert sy-alert-tips animated" sy-enter="zoomIn" sy-leave="zoomOut" sy-type="tips" sy-mask="false"
     id="alertPassword">
    <div class="sy-content">密码必须为6-15位，且两次输入的密码必须一致！</div>
</div>
</body>
</html>
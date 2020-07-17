<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/register_login.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="libraries/JavaScriptMD5/md5.js"></script>
    <script src="js/login.js"></script>
</head>
<body>

<main class="card">
    <h4 class="card-header">登录</h4>
    <form class="card-body flex-container" id="form">
        <label class="flex-1">用户名</label>
        <input type="text" name="username" class="flex-1 form-control">
        <label class="flex-1">密码</label>
        <input type="password" name="password" class="flex-1 form-control">
        <label class="flex-1">验证码</label>
        <img src="getCaptcha" alt="captcha">
        <input type="text" name="captcha" class="flex-1 form-control">
        <button type="button" class="btn btn-info flex-1" id="loginButton">登录</button>

    </form>
</main>

</body>
</html>
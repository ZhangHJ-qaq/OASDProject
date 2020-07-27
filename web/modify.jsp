<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>修改</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/upload_edit.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="js/utils/util.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="js/class/UploadPage.class.js"></script>
    <script src="js/class/ModifyPage.class.js"></script>
    <script src="js/modify.js"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">
        <img src="images/logo.JPG" style="width: 30px;height: 30px">
        Haojie的旅游图片分享站
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="index">主页</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="search">搜索</a>
            </li>
            <li class="nav-item dropdown">
                <c:if test="${requestScope.user!=null}">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                            ${requestScope.user.username}的个人中心
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="myfavor">我的收藏</a>
                        <a class="dropdown-item" href="upload">上传</a>
                        <a class="dropdown-item" href="myphoto">我的图片</a>
                        <a class="dropdown-item" href=addfriend>加好友</a>
                        <a class="dropdown-item" href="myfriend">我的好友</a>
                        <a class="dropdown-item" href="logout">退出登录</a>
                    </div>
                </c:if>
                <c:if test="${requestScope.user==null}">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        未登录
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="login">登录</a>
                    </div>
                </c:if>
            </li>
        </ul>
    </div>
</nav>

<main class="flex-container">
    <div class="card flex-1">
        <div class="card-header">
            修改图片
        </div>
        <form class="card-body flex-container" id="form">
            <div id="photoPreviewArea" class="flex-1">

            </div>
            <input id="photoInput" type="file" name="photo" accept="image/jpeg,image/png,image/gif" class="flex-1">
            <label class="flex-1">标题</label>
            <input type="text" class="flex-1 form-control" name="title">
            <label class="flex-1">主题</label>
            <input type="text" class="flex-1 form-control" name="content">
            <label class="flex-1">描述</label>
            <input type="text" class="flex-1 form-control" name="description">
            <div class="flex-1 flex-container">
                <select class="flex-8-24 form-control" name="country" id="countrySelect">
                    <option value="">选择国家</option>
                </select>
                <select class="flex-8-24 form-control" name="city" id="citySelect">
                    <option value="">选择城市</option>
                </select>
            </div>
            <button type="button" class="flex-1 btn btn-info" id="submitButton">确认修改</button>
        </form>
    </div>
</main>
</body>
</html>
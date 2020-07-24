<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>好友列表</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/friendList.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="js/class/PageWithPagination.js"></script>
    <script src="js/class/AddFriendPage.class.js"></script>
    <script src="js/addfriend.js"></script>
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
                        <a class="dropdown-item" href="setting">个人设置</a>
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
    <div class="card flex-24-24">
        <div class="card-header">
            按用户名搜索
        </div>
        <div class="card-body flex-container">
            <form id="form" class="flex-1">
                <input placeholder="输入用户名以搜索" name="username" type="text" class="flex-16-24 form-control">
                <button type="button" class="btn-info btn" id="submitButton">搜索</button>
            </form>
            <div id="userDisplayArea" class="flex-1">

            </div>


        </div>
    </div>
    <nav aria-label="Page navigation example">
        <ul class="pagination" id="pagination">

        </ul>
    </nav>


</main>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的收藏</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/myphoto_myfavor.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="js/class/PageWithPagination.js"></script>
    <script src="js/class/MyFavorPage.class.js"></script>
    <script src="js/myfavor.js"></script>
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
            设置
        </div>
        <form class="card-body flex-container" id="form">
            <label>别人是否可以查看你的收藏？</label>

            <c:choose>
                <c:when test="${requestScope.user.canBeSeenFavors==1}">
                    <input type="radio" name="canBeSeenFavor" checked value="1">可
                    <input type="radio" name="canBeSeenFavor" value="0">不可
                </c:when>
                <c:when test="${requestScope.user.canBeSeenFavors!=1}">
                    <input type="radio" name="canBeSeenFavor" value="1">可
                    <input type="radio" name="canBeSeenFavor" checked value="0">不可
                </c:when>
            </c:choose>

            <button class="btn btn-info" type="button" id="configButton">确定</button>
        </form>
    </div>


    <div class="card flex-24-24">
        <div class="card-header">
            我的足迹
        </div>
        <div class="card-body flex-container" >
            <ul class="list-group list-group-horizontal-sm flex-1 flex-container" id="footageArea">

            </ul>
            <button class="btn btn-info" id="clearFootageButton">点我清空足迹</button>
        </div>
    </div>
    <div class="card flex-24-24">
        <div class="card-header">
            我的收藏
        </div>
        <div class="card-body" id="imageArea">

        </div>
    </div>

    <nav aria-label="Page navigation example">
        <ul class="pagination" id="pagination">

        </ul>
    </nav>
</main>


</body>
</html>
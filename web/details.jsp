<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>图片详情</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/detail.css">
    <link rel="stylesheet" href="libraries/thdoan-magnify-cca1561/css/magnify.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
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
            <li class="nav-item active">
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


<main class="flex-container-center">
    <div class="card flex-24-24">
        <div class="card-header">
            图片详情
        </div>
        <div class="card-body flex-container-center">
            <div id="leftColumn" class="flex-14-24">
                <img src="photos/medium/${requestScope.image.path}" class="zoom"
                     data-magnify-src="photos/medium/${requestScope.image.path}">
            </div>
            <div id="rightColumn" class="flex-10-24 flex-container-center">
                <ul class="list-group flex-1">
                    <li class="list-group-item">标题：<c:out value="${requestScope.image.title}"></c:out></li>
                    <li class="list-group-item">作者：<c:out value="${requestScope.image.username}"></c:out></li>
                    <li class="list-group-item">图片主题：<c:out value="${requestScope.image.content}"></c:out></li>
                    <li class="list-group-item">简介：<c:out value="${requestScope.image.description}"></c:out></li>
                    <li class="list-group-item">热度：<c:out value="${requestScope.image.favorCount}"></c:out></li>
                    <li class="list-group-item">国家或地区：<c:out
                            value="${requestScope.image.country_RegionName}"></c:out></li>
                    <li class="list-group-item">城市：<c:out value="${requestScope.image.asciiName}"></c:out></li>
                    <li class="list-group-item">发布时间：<c:out value="${requestScope.image.dateReleased}"></c:out></li>
                </ul>
                <c:choose>
                    <c:when test="${requestScope.user==null}">
                        <button class="flex-1 btn btn-info" onclick="location.assign('login')">想要收藏这张照片？登录！</button>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${requestScope.hasFavoredImage}">
                            <button class="flex-1 btn btn-info"
                                    onclick="location.assign('details?imageID=${requestScope.image.imageID}&action=unfavor')">
                                取消收藏
                            </button>
                        </c:if>
                        <c:if test="${!requestScope.hasFavoredImage}">
                            <button class="flex-1 btn btn-info"
                                    onclick="location.assign('details?imageID=${requestScope.image.imageID}&action=favor')">
                                收藏
                            </button>
                        </c:if>
                    </c:otherwise>
                </c:choose>

                <c:if test="${requestScope.actionResult!=null}">
                    <div class="flex-1 alert alert-primary">
                        <c:out value="${requestScope.actionResult.info}"></c:out>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>


<script src="libraries/thdoan-magnify-cca1561/js/jquery.magnify.js"></script>
<script src="libraries/thdoan-magnify-cca1561/js/jquery.magnify-mobile.js"></script>
<script>
    $(document).ready(function () {
        $('.zoom').magnify();
    });
</script>
</body>
</html>
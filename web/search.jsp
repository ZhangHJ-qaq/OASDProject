<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>搜索</title>
    <link rel="stylesheet" href="libraries/bootstrap-4.5.0-dist/css/bootstrap.css">
    <link rel="stylesheet" href="libraries/FlexHelper/FlexHelper.css">
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/search.css">
    <script src="libraries/jQuery/jquery-3.5.1.js"></script>
    <script src="libraries/bootstrap-4.5.0-dist/js/bootstrap.js"></script>
    <script src="js/class/PageWithPagination.js"></script>
    <script src="js/class/SearchPage.class.js"></script>
    <script src="js/search.js"></script>
</head>
<body>
<!--This is navigation bar-->
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
            <li class="nav-item active">
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
<!--This is main part-->
<main class="flex-container-center">
    <div class="card flex-24-24">
        <div class="card-header">
            搜索
        </div>
        <div class="card-body">
            <form id="form">
                <div>
                    <input type="radio" name="howToSearch" value="title">
                    <label>按标题搜索</label>
                    <input type="radio" name="howToSearch" value="content">
                    <label>按主题搜索</label>
                </div>
                <div>
                    <input type="radio" name="howToOrder" value="popularity">
                    <label>按热度排序</label>
                    <input type="radio" name="howToOrder" value="time">
                    <label>按上传时间排序</label>
                </div>
                <div>
                    <input type="text" name="input" class="form-control">
                    <button class="btn btn-info" id="submitButton" type="button">搜索</button>
                </div>

            </form>
        </div>
    </div>


    <div class="card flex-24-24">
        <div class="card-header">
            搜索结果
        </div>
        <div class="card-body" id="imageArea">

        </div>
    </div>


    <nav aria-label="Page navigation example">
        <ul class="pagination" id="pagination">
            <%--<li class="page-item">
                <a class="page-link" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item"><a class="page-link">1</a></li>
            <li class="page-item"><a class="page-link">2</a></li>
            <li class="page-item"><a class="page-link">3</a></li>
            <li class="page-item">
                <a class="page-link" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>--%>
        </ul>
    </nav>


</main>

</body>
</html>
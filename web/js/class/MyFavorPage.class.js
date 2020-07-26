class MyFavorPageClass extends PageWithPagination {
    constructor() {
        super();
        this.pagesize = 6;
        this.imageArea = $("#imageArea");
        this.footageArea = $("#footageArea");
        this.clearFootageButton = $("#clearFootageButton");
        this.form = $("#form");
        this.configButton = $("#configButton");
    }

    search(requestedPage) {
        let that = this;
        let serializedArray = [
            {"name": "requestedPage", "value": requestedPage},
            {"name": "pageSize", "value": this.pagesize},
        ];
        $.post("ImageServlet?method=myfavor", serializedArray)
            .done(function (data) {
                let searchResult = JSON.parse(data);
                that.setImage(searchResult);
                that.setPagination(searchResult);

            })
            .fail(function () {
                alert("失败了，请稍后再试")
            })

    }

    setImage(searchResult) {
        let that = this;
        let imageList = searchResult['imageList'];
        that.imageArea.empty();
        for (let i = 0; i < imageList.length; i++) {
            let element = $(
                `<div class="media">
                <a href="details?imageID=${imageList[i]['imageID']}">
                 <img class="mr-3 img-thumbnail myThumbnail" src="photos/small/${imageList[i]['path']}" alt="Generic placeholder image">
                </a>
                <div class="media-body">
                    <h5 class="mt-0">${imageList[i]['title']}</h5>
                    <p>${imageList[i]['description']}</p>
                    <div>
                        <button class="btn-danger btn">取消收藏</button>
                    </div>
                </div>
            </div>`
            );
            let unfavorButton = $(element.get(0).querySelector(".btn-danger"));
            unfavorButton.click(function () {
                that.unfavor(imageList[i]['imageID'])
            })
            that.imageArea.append(element);
        }

    }


    unfavor(imageID) {
        let that=this;
        let c = confirm("你确定要取消收藏吗？");
        if (!c) return;
        $.post(`UserServlet?method=unfavor&imageID=${imageID}`)
            .done(function (data) {
                let actionResult = JSON.parse(data);
                alert(actionResult['info']);
                if (actionResult['success']) {
                    that.search(that.currentPage);
                }
            })
            .fail(function () {
                alert("出错了，请稍后再试");
            })
    }

    setMyFootage() {
        let that = this;
        $.post("UserServlet?method=getUid")
            .done(function (data) {
                let uid = JSON.parse(data)['uid'];
                let cookies = document.cookie.split(";");

                for (let i = 0; i < cookies.length; i++) {//遍历cookie

                    //如果找到了uid对应的 存有我的足迹信息的cookie
                    if (cookies[i].split("=")[0] === uid.toString()) {
                        let json = decodeURIComponent(cookies[i].split("=")[1]);

                        let browseRecord = JSON.parse(json);
                        that.footageArea.empty()
                        for (let i = 0; i <= browseRecord['records'].length - 1; i++) {
                            let element = $(
                                `<li class="list-group-item flex-6-24">
                                    <span class="footageTitle">${browseRecord['records'][i]['title']}</span>
                                </li>`
                            )
                            that.footageArea.append(element);


                            //设置点击足迹跳转到对应页面
                            element.click(function () {
                                location.assign(`details?imageID=${browseRecord['records'][i]['imageID']}`)
                            })

                        }
                        //设置点击了清空足迹以后的行为

                        that.clearFootageButton.click(function () {
                            document.cookie = `${uid}=;expires=Thu, 01 Jan 1970 00:00:00 GMT`;
                            alert("清空成功！");
                            that.footageArea.empty();
                        })

                    }
                }


            })

    }


    /**
     * 设置 调整是否允许好友查看我的收藏时 的事件
     */
    setConfigButtonOnClick() {
        let that = this;

        that.configButton.click(function () {
            $.post("UserServlet?method=setCanBeSeenFavor",that.form.serializeArray())
                .done(function (data) {
                    let result=JSON.parse(data);
                    alert(result['info'])

                })
                .fail(function () {
                    alert("失败了，请稍后再试");
                })
        })

    }
}


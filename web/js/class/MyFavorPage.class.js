class MyFavorPageClass extends PageWithPagination {
    constructor() {
        super();
        this.pagesize = 6;
        this.imageArea = $("#imageArea");
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
                let c=confirm("你确定要取消收藏吗？");
                if(!c) return;
                $.post(`UserServlet?method=unfavor&imageID=${imageList[i]['imageID']}`)
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
            })
            that.imageArea.append(element);
        }

    }


    unfavor(imageID) {
        alert("取消收藏" + imageID)
    }


}


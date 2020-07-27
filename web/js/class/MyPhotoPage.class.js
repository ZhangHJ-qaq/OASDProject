class MyPhotoPageClass extends PageWithPagination {
    constructor() {
        super();
        this.pageSize = 6;
        this.imageArea = $("#imageArea");
    }

    search(requestedPage) {
        let that = this;
        let serializedArray = [
            {"name": "pageSize", "value": this.pageSize},
            {"name": "requestedPage", "value": requestedPage}
        ];
        this.imageArea.empty();
        $.post("ImageServlet?method=myphoto", serializedArray)
            .done(function (data) {
                let searchResult = JSON.parse(data);
                that.setImage(searchResult);
                that.setPagination(searchResult);
            })
            .fail(function () {
                alert("出错了，请稍后再试")
            })

    }

    setImage(searchResult) {
        let that = this;
        let imageList = searchResult['imageList'];
        for (let i = 0; i < imageList.length; i++) {
            let element = $(
                `<div class="media">
                <a href="details?imageID=${imageList[i]['imageID']}"> 
                <img class="mr-3 myThumbnail img-thumbnail" src="photos/small/${imageList[i]['path']}" alt="Generic placeholder image">
                </a>
                <div class="media-body">
                    <h5 class="mt-0">${imageList[i]['title']}</h5>
                    <p>${imageList[i]['description']}</p>
                    <div>
                        <button class="btn-info btn modifyButton">修改</button>
                        <button class="btn btn-danger deleteButton">删除</button>
                    </div>
                </div>
            </div>`
            );
            this.imageArea.append(element);
            let modifyButton = $(element.get(0).querySelector(".modifyButton"));
            let deleteButton = $(element.get(0).querySelector(".deleteButton"));
            modifyButton.click(function () {
                that.modifyImage(imageList[i]['imageID']);
            })
            deleteButton.click(function () {
                that.deleteImage(imageList[i]['imageID']);
            })

        }


    }


    //待实现
    modifyImage(imageID) {
        location.assign(`modify?imageID=${imageID}`)
    }

    deleteImage(imageID) {
        let that = this;
        $.post(`UserServlet?method=delete&imageID=${imageID}`)
            .done(function (data) {
                let deleteResult = JSON.parse(data);
                if (deleteResult['success']) {
                    alert("删除成功");
                    that.search(that.currentPage);
                } else {
                    alert("删除失败");
                }

            })
            .fail(function () {
                alert("删除失败");
            })
    }




}
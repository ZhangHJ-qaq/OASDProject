class MyPhotoPageClass {
    constructor() {
        this.pageSize = 6;
        this.imageArea = $("#imageArea");
        this.pagination=$("#pagination");
    }

    search(requestedPage) {
        let that = this;
        let serializedArray = [
            {"name": "pageSize", "value": this.pageSize},
            {"name": "requestedPage", "value": requestedPage}
        ];
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
        this.imageArea.empty();
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

    setPagination(searchResult) {
        this.pagination.empty();
        this.currentPage = searchResult['respondedPage'];
        let that = this;
        let previousPageButton = $(`<li class="page-item">
                <a class="page-link" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>`);
        let nextPageButton = $(`<li class="page-item">
                <a class="page-link" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>`);
        previousPageButton.click(function () {
            that.search(that.currentPage - 1);
        })
        nextPageButton.click(function () {
            that.search(that.currentPage + 1);
        })

        let start = Math.max(searchResult['respondedPage'] - 4, 1);
        let distanceLeft = searchResult['respondedPage'] - start;
        let distanceRight = 9 - distanceLeft;
        let end = Math.min(searchResult['maxPage'], searchResult['respondedPage'] + distanceRight);

        if (searchResult['respondedPage'] !== 1) {
            this.pagination.append(previousPageButton);
        }
        for (let i = start; i <= end; i++) {
            let button;
            if (i === searchResult['respondedPage']) {
                button = $(`<li class="page-item active"><a class="page-link">${i}</a></li>`)
            } else {
                button = $(`<li class="page-item"><a class="page-link">${i}</a></li>`);
            }
            button.click(function () {
                that.search(i);
            })
            that.pagination.append(button);
        }
        if (searchResult['respondedPage'] !== searchResult['maxPage']) {
            this.pagination.append(nextPageButton);
        }

    }



    //待实现
    modifyImage(imageID) {
        alert("修改" + imageID);
    }

    deleteImage(imageID) {
        alert("删除" + imageID);
    }


}
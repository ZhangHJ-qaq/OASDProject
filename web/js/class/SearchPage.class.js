class SearchPageClass {
    constructor(pageSize) {
        this.form = $("#form");
        this.submitButton = $("#submitButton");
        this.pageSize = pageSize;
        this.imageArea = $("#imageArea");
        this.pagination = $("#pagination");
        this.currentPage = 0;

    }

    setSubmitButtonOnClick() {
        let that = this;
        that.submitButton.click(function () {
            that.search(1);

        })

    }

    search(requestedPage) {
        let serializedArray = this.form.serializeArray();
        serializedArray[serializedArray.length] = {"name": "requestedPage", "value": requestedPage};
        serializedArray[serializedArray.length] = {"name": "pageSize", "value": this.pageSize};
        let that = this;
        $.post("ImageServlet?method=pureSearch", serializedArray)
            .done(function (data) {
                let searchResult = JSON.parse(data);
                that.setImages(searchResult);
                that.setPagination(searchResult);

            })
            .fail(function () {
                alert("搜索失败了，请稍后再试")
            })


    }

    setImages(searchResult) {
        this.imageArea.empty();
        let imageList = searchResult['imageList'];
        for (let i = 0; i <= imageList.length - 1; i++) {
            let imageID = imageList[i]["imageID"];
            let path = imageList[i]['path'];
            let desc = imageList[i]['description'];
            let title = imageList[i]['title'];

            let element = $(`
            <div class="media">
                <a href="detail?imageID=${imageID}"> <img class="mr-3 img-thumbnail myThumbnail" src="photos/small/${path}" alt="Generic placeholder image">
                </a>
                <div class="media-body">
                    <h5 class="mt-0">${title}</h5>
                    ${desc}
                </div>
            </div>

            `)
            this.imageArea.append(element)
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
}
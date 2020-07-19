class SearchPageClass extends PageWithPagination{
    constructor(pageSize) {
        super()
        this.form = $("#form");
        this.submitButton = $("#submitButton");
        this.pageSize = pageSize;
        this.imageArea = $("#imageArea");
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
                <a href="details?imageID=${imageID}"> <img class="mr-3 img-thumbnail myThumbnail" src="photos/small/${path}" alt="Generic placeholder image">
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


}
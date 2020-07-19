class PageWithPagination {
    constructor() {
        this.pagination=$("#pagination");
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
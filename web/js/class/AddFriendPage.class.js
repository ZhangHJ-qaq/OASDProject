class AddFriendPageClass extends PageWithPagination {
    constructor() {
        super();
        this.submitButton = $("#submitButton");
        this.form = $("#form");
        this.userDisplayArea = $("#userDisplayArea");
        this.pageSize = 6;
        this.currentPage = 1;
    }

    setSubmitButtonOnClick() {
        let that = this;
        this.submitButton.click(function () {
            that.search(1);
        })
    }


    search(requestedPage) {
        let that = this;
        let serializedArray = this.form.serializeArray();
        this.userDisplayArea.empty();
        serializedArray[serializedArray.length] = {"name": "requestedPage", "value": requestedPage};
        serializedArray[serializedArray.length] = {"name": "pageSize", "value": that.pageSize};
        $.post("UserServlet?method=searchFriendToAdd", serializedArray)
            .done(function (data) {
                let searchResult = JSON.parse(data);
                that.displayUser(searchResult);
                that.setPagination(searchResult);

            })

    }

    displayUser(searchResult) {
        let that = this;
        for (let i = 0; i <= searchResult['userList'].length - 1; i++) {
            let element = $(
                `<div class="media">
                    <div class="media-body" style="padding-bottom: 10px">
                        <h5 class="mt-0">${searchResult['userList'][i]['username']}</h5>
                        <div>注册时间：${new Date(searchResult['userList'][i]['dateJoined'])}</div>
                        <div>邮箱：${searchResult['userList'][i]['email']}</div>
                        <button class="btn btn-info">加好友</button>
                     </div>
                </div>`
            );
            let button = $(element.get(0).querySelector(".btn-info"));
            button.click(function () {
                that.addFriend(searchResult['userList'][i]['username']);
            })
            that.userDisplayArea.append(element);
        }

    }

    addFriend(username) {
        let c = confirm(`你确定要向${username}发出好友申请嘛？`);
        if (c) {
            $.post(`UserServlet?method=addfriend&username=${username}`)
                .done(function (data) {
                    let addFriendResult = JSON.parse(data);
                    alert(addFriendResult['info']);
                })
        }
    }
}

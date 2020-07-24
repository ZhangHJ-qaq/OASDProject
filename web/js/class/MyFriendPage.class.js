class MyFriendPageClass extends PageWithPagination {
    constructor() {
        super();
        this.friendRequestArea = $("#friendRequestArea");
        this.friendListArea = $("#friendListArea");
        this.sysMessageArea = $("#sysMessageArea");
        this.pageSize = 6;
        this.currentPage = 1;

    }

    getFriendRequests() {
        let that = this;
        $.post("UserServlet?method=getFriendRequests")
            .done(function (data) {
                that.friendRequestArea.empty();
                let friendRequestList = JSON.parse(data);
                for (let i = 0; i <= friendRequestList.length - 1; i++) {
                    let element = $(
                        `<div class="alert alert-info" role="alert">
                            用户${friendRequestList[i]['senderUsername']}向你发出了好友申请
                            <span class="badge badge-success">可</span>
                            <span class="badge badge-danger">不可</span>
                        </div>`
                    )
                    let acceptButton = $(element.get(0).querySelector(".badge-success"));
                    let refuseButton = $(element.get(0).querySelector(".badge-danger"));

                    acceptButton.click(function () {
                        that.acceptRequest(friendRequestList[i]['requestID'])
                    })
                    refuseButton.click(function () {
                        that.refuseRequest(friendRequestList[i]['requestID'])
                    });

                    that.friendRequestArea.append(element)

                }


            })


    }


    refuseRequest(requestID) {
        let that = this;
        $.post(`UserServlet?method=refuseRequest&requestID=${requestID}`)
            .done(function (data) {
                let refuseResult = JSON.parse(data);
                alert(refuseResult['info']);
                that.getFriendRequests();
                that.search(that.currentPage);

            })
    }

    acceptRequest(requestID) {
        let that = this;
        $.post(`UserServlet?method=acceptRequest&requestID=${requestID}`)
            .done(function (data) {
                let acceptResult = JSON.parse(data);
                alert(acceptResult['info']);
                that.getFriendRequests();
                that.search(that.currentPage);
            })
    }

    getSysMessage() {
        let that = this;
        that.sysMessageArea.empty();
        $.post("UserServlet?method=getSysMessage")
            .done(function (data) {
                let sysMessageList = JSON.parse(data);

                for (let i = sysMessageList.length - 1; i >= 0; i--) {
                    let element = $(
                        `<div class="alert alert-primary" role="alert">
                            ${sysMessageList[i]['messageContent']}
                            <span class="badge badge-success">阅</span>
                        </div>`
                    )
                    let readButton = $(element.get(0).querySelector(".badge-success"));
                    readButton.click(function () {
                        that.readMessage(sysMessageList[i]['messageID'])
                    })
                    that.sysMessageArea.append(element);

                }

            })


    }

    readMessage(messageID) {
        let that = this;
        $.post(`UserServlet?method=readMessage&messageID=${messageID}`)
            .done(function () {
                that.getSysMessage();
            })
    }


    search(requestedPage) {
        let that = this;
        let serializedArray = [
            {name: "requestedPage", value: requestedPage},
            {name: "pageSize", value: this.pageSize},
        ];
        $.post("UserServlet?method=getMyFriend", serializedArray)
            .done(function (data) {
                that.friendListArea.empty();
                let searchResult = JSON.parse(data)
                that.displayMyFriend(searchResult);
                that.setPagination(searchResult);

            })

    }

    displayMyFriend(searchResult) {
        let that = this;
        this.friendListArea.empty();
        for (let i = 0; i <= searchResult['userList'].length - 1; i++) {
            let element = $(
                `<div class="media">
                    <div class="media-body" style="padding-bottom: 10px">
                        <h5 class="mt-0">${searchResult['userList'][i]['username']}</h5>
                        <div>注册时间：${new Date(searchResult['userList'][i]['dateJoined'])}</div>
                        <div>邮箱：${searchResult['userList'][i]['email']}</div>
                        <button class="btn btn-danger">删好友</button>
                     </div>
                </div>`
            );
            let deleteFriendButton = $(element.get(0).querySelector(".btn-danger"));
            deleteFriendButton.click(function () {
                that.deleteFriend(searchResult['userList'][i]['uid']);
            })


            //设置点击用户名跳转到对方的收藏页面
            let h5 = $(element.get(0).querySelector("h5"));
            h5.click(function () {
                location.assign(`othersfavor?username=${searchResult['userList'][i]['username']}`)
            })

            that.friendListArea.append(element);

        }

    }

    deleteFriend(targetUid) {
        let that = this;
        let c = confirm("确定删除这个好友吗？");
        if (!c) return;
        $.post(`UserServlet?method=deleteFriend&targetUid=${targetUid}`)
            .done(function (data) {
                let deleteResult = JSON.parse(data);
                alert(deleteResult['info']);
                that.search(that.currentPage);

            })
    }

}
$(function () {
    let loginButton = $("#loginButton");
    let form = $("#form")
    loginButton.click(function () {
        let arrayToBeSubmitted = form.serializeArray();
        arrayToBeSubmitted[1]['password'] = md5(arrayToBeSubmitted[1]['password']);
        $.post("UserServlet?method=login", arrayToBeSubmitted)
            .done(function (data) {
                let object = JSON.parse(data);
                alert(object['loginResult']);
                location.assign("index");
            })

    })

})
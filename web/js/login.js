$(function () {
    let loginButton = $("#loginButton");
    let form = $("#form")
    loginButton.click(function () {
        let arrayToBeSubmitted = form.serializeArray();
        arrayToBeSubmitted[1]['value'] = md5(arrayToBeSubmitted[1]['value']);
        $.post("UserServlet?method=login", arrayToBeSubmitted)
            .done(function (data) {
                let object = JSON.parse(data);
                alert(object['info']);
                if (object['success']) {
                    location.assign("index");
                }
            })

    })

})
class RegisterPageClass {
    constructor() {
        this.userNameInput = $("#userNameInput");
        this.emailInput = $("#emailInput");
        this.password1Input = $("#password1Input");
        this.password2Input = $("#password2Input");
        this.captchaInput = $("#captchaInput");
        this.userNameInfoArea = $("#usernameInfoArea");
        this.emailInfoArea = $("#emailInfArea");
        this.passwordProgressArea = $("#password1ProgressArea");
        this.password2InfoArea = $("#password2InfoArea");
        this.submitButton = $("#submitButton");
        this.form = $("#form")

        this.usernameOK = false;
        this.emailOK = false;
        this.passwordOK = false;


        //get all the input elements needed
    }

    setUsernameInputOnChange() {
        let that = this;
        this.userNameInput.on("input", function (target) {
            let username = that.userNameInput.val();
            let usernameOK = username.length >= 4 && username.length <= 15;
            that.usernameOK = usernameOK;
            that.userNameInfoArea.empty();
            if (!usernameOK) {
                that.userNameInfoArea.append($("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                    "  用户名必须是4-15位" +
                    "</div>"))
            } else {
                that.userNameInfoArea.empty();
            }

        })
    }


    setEmailInputOnChange() {
        let that = this;
        this.emailInput.on("input", function () {
            let email = that.emailInput.val();
            let emailOK = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(email);
            that.emailOK = emailOK;
            that.emailInfoArea.empty();
            if (!emailOK) {
                that.emailInfoArea.append($("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                    "  邮箱格式不符合要求，请自查" +
                    "</div>"));
            } else {
                that.emailInfoArea.empty()
            }

        })
    }


    setPasswordOnChange() {
        let that = this;
        that.password1Input.on("input", function () {
                let password1 = that.password1Input.val();
                let getSecurityIndex = function (s) {
                    if (s.length < 6 || s.length > 15) {
                        return 0
                    }
                    let hasNumber = false;
                    let hasUpperCaseLetter = false;
                    let hasLowerCaseLetter = false;
                    let hasSpecCharacter = false;
                    let count = 0;//count is number of kinds of elements;
                    for (let i = 0; i <= s.length - 1; i++) {
                        if (!hasNumber && isNumber(s[i])) {
                            count++;
                            hasNumber = true;
                        } else if (!hasLowerCaseLetter && isLowerCaseLetter(s[i])) {
                            count++;
                            hasLowerCaseLetter = true;
                        } else if (!hasUpperCaseLetter && isUpperCaseLetter(s[i])) {
                            count++;
                            hasUpperCaseLetter = true;
                        } else if (!hasSpecCharacter && isSpecialCharacter(s[i])) {
                            count++;
                            hasSpecCharacter = true;
                        }
                    }
                    return count * 25;


                    function isNumber(s) {
                        return /^[0-9]$/.test(s)
                    }

                    function isUpperCaseLetter(s) {
                        return /^[A-Z]$/.test(s)
                    }

                    function isLowerCaseLetter(s) {
                        return /^[a-z]$/.test(s)
                    }

                    function isSpecialCharacter(s) {
                        return !isNumber(s) && !isUpperCaseLetter(s) && !isLowerCaseLetter(s);
                    }

                }
                let securityIndex = getSecurityIndex(password1);
                that.passwordProgressArea.empty();
                that.passwordProgressArea.append($(
                    `<div class="progress">
                    <div class="progress-bar" role="progressbar" style="width: ${securityIndex}%" aria-valuenow="${securityIndex}" aria-valuemin="0" aria-valuemax="100"></div>
                </div>`
                ));
            }
        );
        let checkPassword1AndPassword2 = function () {
            let password1 = that.password1Input.val();
            let password2 = that.password2Input.val();
            that.password2InfoArea.empty();
            if (password1 !== password2) {
                that.password2InfoArea.append(`<div class="alert alert-danger" role="alert">
                                                    两次密码不一致!
                                               </div>`);
                that.passwordOK = false;
                return


            }
            if (password1.length < 6 || password1.length > 15) {
                that.password2InfoArea.append(`<div class="alert alert-danger" role="alert">
                                                    密码必须是6-15位!
                                               </div>`);
                that.passwordOK = false;
                return;
            }
            that.passwordOK = true;


        }
        that.password1Input.on("input", checkPassword1AndPassword2);
        that.password2Input.on("input", checkPassword1AndPassword2);
    }


    setSubmitButtonOnClick() {
        let that = this;
        this.submitButton.click(function (e) {
            if (!that.usernameOK) {
                syalert.syopen("alertUsername");
                e.preventDefault();
                return;
            }
            if (!that.emailOK) {
                syalert.syopen("alertEmail");
                e.preventDefault();
                return;
            }
            if (!that.passwordOK) {
                syalert.syopen("alertPassword");
                e.preventDefault();
                return;
            }

            let arrayToBeSubmitted = that.form.serializeArray();
            arrayToBeSubmitted[1]['password1'] = md5(arrayToBeSubmitted['password1']);
            arrayToBeSubmitted[2]['password2'] = md5(arrayToBeSubmitted['password2']);


            $.post("UserServlet?method=register", arrayToBeSubmitted)
                .done(function (data) {
                    let object = JSON.parse(data);
                    alert(object['registerResult']);
                    if (object['registerResult'] === "注册成功") {
                        location.assign("index");
                    }
                })



        })

    }


}
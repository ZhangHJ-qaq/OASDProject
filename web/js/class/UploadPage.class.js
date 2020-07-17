class UploadPageClass {
    constructor() {
        this.form = $("#form");
        this.submitButton = $("#submitButton");
        this.photoInput = $("#photoInput");
        this.photoPreviewArea = $("#photoPreviewArea");
    }

    setPhotoPreview() {
        let that = this;
        this.photoInput.change(function () {
            let file = that.photoInput.get(0).files[0];
            let fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function () {
                that.photoPreviewArea.empty();
                let url = fileReader.result;
                let imgElement = document.createElement("img");
                imgElement.src = url;
                imgElement.style.maxWidth = "100%"
                that.photoPreviewArea.append(imgElement);
            }
        })
    }

    setSubmitButtonOnClick() {
        let that = this;
        this.submitButton.click(function (e) {
            let formDataObject = (function (form) {
                let o = {};
                $.each(form.serializeArray(), function (index) {
                    if (o[this['name']]) {
                        o[this['name']] = o[this['name']] + "," + this['value'];
                    } else {
                        o[this['name']] = this['value'];
                    }
                });
                return o;
            })(that.form);

            if (!formDataObject['title']) {
                alert("你必须填写标题！")
                e.preventDefault();
                return;
            }
            if (!formDataObject['theme']) {
                alert("你必须填写主题！")
                e.preventDefault();
                return;
            }
            if (!formDataObject['desc']) {
                alert("你必须填写照片描述！")
                e.preventDefault();
                return;
            }
            if (!formDataObject['country']) {
                alert("你必须填写国家或地区！")
                e.preventDefault();
                return;
            }
            if (!formDataObject['city']) {
                alert("你必须填写城市！");
                e.preventDefault();
                return;
            }
        })
    }


}
class UploadPageClass {
    constructor() {
        this.form = $("#form");
        this.submitButton = $("#submitButton");
        this.photoInput = $("#photoInput");
        this.photoPreviewArea = $("#photoPreviewArea");
        this.countrySelect = $("#countrySelect");
        this.citySelect = $("#citySelect");
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
            if (!formDataObject['content']) {
                alert("你必须填写主题！")
                e.preventDefault();
                return;
            }
            if (!formDataObject['description']) {
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

            //准备上传
            that.upload();


        })
    }

    setCountryOnChange() {
        let that = this;
        this.countrySelect.change(function () {
            let iso = that.countrySelect.val();
            $.post(`CountryCityServlet?method=erjiliandong&iso=${iso}`)
                .done(function (data) {
                    that.citySelect.empty();
                    let cityList = JSON.parse(data);
                    for (let i = 0; i <= cityList.length - 1; i++) {
                        that.citySelect.append(
                            $(`<option value="${cityList[i]['geoNameID']}">${cityList[i]['asciiName']}</option>`)
                        )
                    }
                })


        })
    }

    upload() {
        let formData = new FormData();
        formData.append("photo", this.photoInput.get(0).files[0]);//添加文件

        let serializedArray = this.form.serializeArray();
        for (let i = 0; i <= serializedArray.length - 1; i++) {
            formData.append(serializedArray[i]['name'], serializedArray[i]['value']);
        }

        $.ajax({
            url: 'UserServlet?method=upload',
            type: 'POST',
            data: formData,
            cache: false,
            processData: false,
            contentType: false
        })
            .done(function (data) {
                let uploadResult = JSON.parse(data);
                alert(uploadResult['info']);
                if(uploadResult['success']){
                    location.assign("myphoto")
                }
            })


    }

}
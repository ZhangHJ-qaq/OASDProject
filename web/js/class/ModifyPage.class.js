class ModifyPageClass extends UploadPageClass {
    constructor() {
        super();
        this.imageID = getAllUrlParams()['imageID'];
        this.titleInput = $("input[name=title]");
        this.contentInput = $("input[name=content]");
        this.descriptionInput = $("input[name=description]");
    }

    showImageInfoOnPage() {
        let that = this;
        $.post(`ImageServlet?method=getImageInfo&imageID=${this.imageID}`)
            .done(function (data) {
                if(JSON.parse(data)['info']){
                    alert(JSON.parse(data)['info']);
                }
                let image = JSON.parse(data);
                that.titleInput.val(image['title']);
                that.descriptionInput.val(image['description']);
                that.contentInput.val(image['content']);
                that.setCountryOptions(image['country_RegionCodeISO']);
                that.photoPreviewArea.append(
                    $(`<img src="photos/medium/${image['path']}" alt="${image['title']}">`)
                );
                that.citySelect.append(
                    $(`<option value="${image['cityCode']}" selected>${image['asciiName']}</option>`)
                );

            })
    }

    setSubmitButtonOnClick() {
        let that = this;
        this.submitButton.click(function () {
            if (that.checkInfoComplete() === false) {
                return;
            }
            that.modify();
        })

    }

    modify() {
        let formData = new FormData();
        formData.append("photo", this.photoInput.get(0).files[0]);//添加文件

        let serializedArray = this.form.serializeArray();
        for (let i = 0; i <= serializedArray.length - 1; i++) {
            formData.append(serializedArray[i]['name'], serializedArray[i]['value']);
        }

        $.ajax({
            url: `UserServlet?method=modify&imageID=${this.imageID}`,
            type: 'POST',
            data: formData,
            cache: false,
            processData: false,
            contentType: false
        })
            .done(function (data) {
                let uploadResult = JSON.parse(data);
                alert(uploadResult['info']);
                if (uploadResult['success']) {
                    location.assign("myphoto")
                }
            })


    }


}
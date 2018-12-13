$(
    function () {
        const label = window.parent.inputLabel;
        const addOrEdit = {};

        let success = false;

        addOrEdit.buildUI = function () {
            $('.name-label').text(label.name);
        };

        addOrEdit.bindClick = function () {
            $('.save-btn').on('click', e => {
                let url;
                let data;
                let message;

                switch (label.type) {
                    //保存名字
                    case 0:
                        url = '/schedule/special';
                        data = {
                            name: $('.name').val(),
                        };
                        message = {
                            success: '保存成功',
                            error: '保存失败...'
                        };
                        this.postRequset(url, data, message);
                        break;
                    default:
                        break;
                }
            });

            $('.cancel-btn').on('click', function (e) {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                if (success) {
                    window.parent.querySpecialEmployees();
                }
            });
        };



        addOrEdit.postRequset = function (url, data, msg) {
            $.ajax({
                url: url,
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code == 0) {
                        $('.message').text(msg.success);
                        success = true;
                    } else {
                        $('.message').text(msg.error + result.message);
                        success = false;
                    }
                },
                error: (xhr, e) => {
                    $('.message').text(msg.error);
                    success = false;
                }
            });
        }

        addOrEdit.buildUI();
        addOrEdit.bindClick();
    }
);
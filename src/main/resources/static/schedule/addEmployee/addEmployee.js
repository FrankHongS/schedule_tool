$(
    function () {
        const label = window.parent.inputLabel;
        const addOrEdit = {};

        let success = false;

        addOrEdit.buildUI = function () {
            $('.name-label').text(label.name);
            if (label.value) {
                $('.name').val(label.value);
            }

            if(label.employeeType){
                $('#employee-type').val(label.employeeType);
            }
        };

        addOrEdit.bindClick = function () {
            $('.save-btn').on('click', e => {
                let url;
                let data;
                let message;

                switch (label.type) {
                    //保存名字
                    case 2:
                        url = '/schedule/program_employee';
                        data = {
                            name: $('.name').val(),
                            programId: label.programId,
                            employeeType: $('#employee-type').children('option:selected').val()
                        };
                        message = {
                            success: '保存成功',
                            error: '保存失败...'
                        };
                        this.postRequset(url, data, message);
                        break;
                    //更新名字
                    case 3:
                        url='/schedule/program_employee/update';
                        data={
                            id: label.id,
                            name: $('.name').val(),
                            employeeType: $('#employee-type').children('option:selected').val()
                        };
                        message = {
                            success: '更新成功',
                            error: '更新失败...'
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
                    window.parent.queryEmployees();
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
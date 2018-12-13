$(
    function () {
        const originData = window.parent.originData;

        const editEmployee = {};

        let isRequestSuccess=false;

        editEmployee.bindOrigiData = function () {
            switch (originData.type) {
                case 0:
                    break;
                case 1:
                    $('.name').val(originData.name);
                    $('.alias').val(originData.alias);
                    break;
                default:
                    break;
            }
        };

        editEmployee.bindClick = function () {

            $('.save-btn').click(() => {
                let url;
                let data;
                let msg;

                const name = $('.name').val();
                const alias = $('.alias').val();

                if (!name || !alias) {
                    $('.message-container .message').text('名字或alias不能为空');
                    return;
                }

                switch (originData.type) {
                    case 0://save employee
                        url = '/schedule/station_employee/add';
                        data = {
                            name: name,
                            alias: alias
                        };
                        msg = {
                            success: '添加成功',
                            failure: '添加失败...'
                        };
                        break;
                    case 1://modify employee
                    url = '/schedule/station_employee/update';
                    data = {
                        id:originData.id,
                        name: name,
                        alias: alias
                    };
                    msg = {
                        success: '更新成功',
                        failure: '更新失败...'
                    };
                        break;
                    default:
                        break;
                }

                this.request(url, data, msg);
            });

            $('.cancel-btn').click(function () {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                if(isRequestSuccess){
                    window.parent.queryEmployees();
                }
            });
        };

        editEmployee.request = function (url, data, msg) {
            $.ajax({
                url: url,
                type: 'POST',
                data: data,
                success: result => {
                    if (result.code === 0) {
                        isRequestSuccess=true;
                        $('.message-container .message').text(msg.success);
                    } else {
                        isRequestSuccess=false;
                        $('.message-container .message').text(msg.failure + result.message);
                    }
                },
                error:(xhr,e)=>{
                    $('.message-container .message').text(msg.failure);
                }
            });
        };

        editEmployee.bindOrigiData();
        editEmployee.bindClick();
    }
);
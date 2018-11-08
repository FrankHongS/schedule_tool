$(document).ready(
    function () {
        const modify = {};

        modify.buildUI=function(){
            $('.name').val(window.parent.title.name);
            $('.alias').val(window.parent.title.alias);
        }

        modify.bindAjax = function () {
            $('.modify-btn').bind('click', function (e) {
                $('.message').text('正在修改...');

                $.ajax({
                    url: '/schedule/employee/update',
                    type: 'POST',
                    data: {
                        id: window.parent.title.employeeId,
                        name: $('.name').val(),
                        alias: $('.alias').val()
                    },
                    success: result => {
                        $('.message').text('修改成功');
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        $('.message').text('修改失败...'+e);
                        console.log(result);
                    }
                });
            });

            $('.delete-btn').bind('click', function (e) {
                $('.message').text('正在删除...');

                $.ajax({
                    url: '/schedule/employee/delete',
                    type: 'POST',
                    data: {
                        id: window.parent.title.employeeId
                    },
                    success: result => {
                        $('.message').text('删除成功');
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        $('.message').text('删除失败...'+e);
                        console.log(e);
                    }
                });
            });
        }

        modify.bindClick=function(){
            $('.cancel-btn').bind('click', function (e) {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            });
        };

        modify.buildUI();
        modify.bindAjax();
        modify.bindClick();
    }
);
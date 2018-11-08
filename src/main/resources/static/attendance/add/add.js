$(document).ready(
    function () {
        $('.save-btn').bind('click', function (e) {
            $('.message').text('正在保存...');
            $.ajax({
                url: '/schedule/employee',
                type: 'POST',
                data: {
                    name: $('.name').val(),
                    alias: $('.alias').val()
                },
                success: result => {
                    $('.message').text('保存成功');
                    console.log(result);
                },
                error:(xhr,e)=>{
                    $('.message').text('保存失败...'+e);
                    console.log(e);
                }
            });
        });

        $('.cancel-btn').bind('click', function (e) {
            const index=parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });

    }
);
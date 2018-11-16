$(document).ready(
    function () {
        $('.save-btn').bind('click', function (e) {
            $('.message').text('正在保存...');
            $.ajax({
                url: '/schedule/employee',
                type: 'POST',
                data: {
                    name: $('.name').val(),
                    alias: $('.alias').val(),
                    annual:$('.annual').val()==''?'0':$('.annual').val()
                },
                success: result => {
                    if(result.code==0){
                        $('.message').text('保存成功');
                    }else{
                        $('.message').text('保存失败...'+result.message);
                    }
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
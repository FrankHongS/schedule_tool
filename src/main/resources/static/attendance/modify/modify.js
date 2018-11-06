$(document).ready(
    function () {
        $('.modify-btn').bind('click', function (e) {
            console.log('start ajax...');

            $.ajax({
                url: '/schedule/employee/update',
                type: 'POST',
                data: {
                    id: 2,
                    name: $('.name').val(),
                    alias: $('.alias').val()
                },
                success: result => {
                    console.log(result);
                }
            });
        });

        $('.delete-btn').bind('click',function(e){
            console.log('start ajax...');

            $.ajax({
                url: '/schedule/employee/delete',
                type: 'POST',
                data: {
                    alias: $('.alias').val()
                },
                success: result => {
                    console.log(result);
                }
            });
        });

        $('.cancel-btn').bind('click', function (e) {
            const index=parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });

    }
);
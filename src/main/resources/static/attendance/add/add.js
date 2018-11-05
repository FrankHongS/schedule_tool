$(document).ready(
    function () {
        $('.save-btn').bind('click', function (e) {
            console.log('start ajax...');

            $.ajax({
                url: '/schedule/employee',
                type: 'POST',
                data: {
                    name: $('.name').val(),
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
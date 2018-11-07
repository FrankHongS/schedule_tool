$(document).ready(
    function () {
        const modify = {};

        modify.buildUI=function(){
            $('.name').val(window.parent.title.name);
            $('.alias').val(window.parent.title.alias);
        }

        modify.bindAjax = function () {
            $('.modify-btn').bind('click', function (e) {
                console.log('start ajax...');

                $.ajax({
                    url: '/schedule/employee/update',
                    type: 'POST',
                    data: {
                        id: window.parent.title.employeeId,
                        name: $('.name').val(),
                        alias: $('.alias').val()
                    },
                    success: result => {
                        console.log(result);
                    }
                });
            });

            $('.delete-btn').bind('click', function (e) {
                console.log('start ajax...');

                $.ajax({
                    url: '/schedule/employee/delete',
                    type: 'POST',
                    data: {
                        id: window.parent.title.employeeId
                    },
                    success: result => {
                        console.log(result);
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
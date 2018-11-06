$(
    function () {
        const edit = {};

        edit.buildUI = function () {

            const render = laydate.render({
                elem: '.datepicker',
                theme: '#393D49',
                btns: ['confirm'],
                range: true
            });



            $('#event-type').bind('change', function () {
                const value = $(this).children('option:selected').val();
                switch (value) {
                    case '1':
                        $('#edit-form')[0].reset();

                        $('.time-label').text('请假时间');
                        $('.datepicker').attr('placeholder', '  -')
                        render.config.range = '-';//设置laydate的属性
                        break;
                    case '2':
                        $('#edit-form')[0].reset();

                        $('.time-label').text('时间');
                        $('.datepicker').attr('placeholder', '发生日期')
                        render.config.range = false;
                        url = '/schedule/late';
                        break;
                    case '3':
                        $('#edit-form')[0].reset();

                        $('.time-label').text('时间');
                        $('.datepicker').attr('placeholder', '发生日期')
                        $('.datepicker').val('');
                        render.config.range = false;
                        url = '/schedule/homebase';
                        break;
                    default:
                        break;
                }
            });
        };

        edit.bindAjax = function () {
            $('.save').bind('click', function () {
                console.log('start ajax...');

                let data;
                let url;
                const value = $('#event-type').children('option:selected').val();
                switch (value) {
                    case '1':
                        data = {
                            name: '李杰',
                            alias: 'v-shhong',
                            leaveDateRange: $('.datepicker').val(),
                            isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                            comment: $('.comment-text').val()
                        };
                        console.log(data);
                        
                        url = '/schedule/leave';
                        break;
                    case '2':
                        data = {
                            name: '李杰',
                            alias: 'v-shhong',
                            lateDate: $('.datepicker').val(),
                            isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                            comment: $('.comment-text').val()
                        };
                        url = '/schedule/late';
                        break;
                    case '3':
                        data = {
                            name: '李杰',
                            alias: 'v-shhong',
                            homebaseDate: $('.datepicker').val(),
                            isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                            comment: $('.comment-text').val()
                        };
                        url = '/schedule/homebase';
                        break;
                    default:
                        break;
                }

                $.ajax({
                    url: url,
                    type: 'POST',
                    data: data,
                    success: result => {
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        console.log(e);
                    }
                });
            });
        };

        edit.buildUI();
        edit.bindAjax();
    }
);
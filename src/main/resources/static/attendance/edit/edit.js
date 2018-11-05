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
                console.log(value);
                switch (value) {
                    case '1':
                        $('.time-label').text('请假时间');
                        $('.datepicker').attr('placeholder', '  -')
                        render.config.range = true;//设置laydate的属性
                        break;
                    case '2':
                        $('.time-label').text('时间');
                        $('.datepicker').attr('placeholder', '发生日期')
                        render.config.range = false;

                        break;
                    case '3':
                        $('.time-label').text('时间');
                        $('.datepicker').attr('placeholder', '发生日期')
                        render.config.range = false;
                        break;
                    default:
                        break;
                }
            });
        };

        edit.buildUI();
    }
);
window.exportSchedule=function(){
        const exportSchedule={};

        exportSchedule.bindLayer=function(){
            laydate.render({
                elem: '.export-from',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
            laydate.render({
                elem: '.export-to',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
        }

        exportSchedule.bindClick=function(){
            $('.export-main-container .export-link').on('click', function () {

                const from = $('.export-from').val();
                const to = $('.export-to').val();

                if (!from||!to) {
                    alert('时间范围不能为空！');
                    return;
                }

                $(this).attr('href', '/schedule/schedule_excel/table?from=' + from + '&to=' + to);
            });
        };

        exportSchedule.bindLayer();
        exportSchedule.bindClick();
    };
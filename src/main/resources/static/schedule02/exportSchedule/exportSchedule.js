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

            $('.holiday-container ul').on('click','li',function (e) {
                $(e.target).addClass('active').removeClass('unactive')
                    .siblings().removeClass('active').addClass('unactive');
    
                return false;
            });

            $("input").blur(function(){
                console.log('blur...')
                const from = $('.export-from').val();
                const to = $('.export-to').val();
                // if(from&&to){
                    console.log(from);
                    console.log(to);
                    window.queryHoliday(from,to);
                // }
              });

            $('.add-holiday').click(function(){

                const from = $('.export-from').val();
                const to = $('.export-to').val();

                if (!from||!to) {
                    alert('时间范围不能为空！');
                    return;
                }

                window.originData.from=from;
                window.originData.to=to;

                layer.open({
                    type: 2,
                    title: '添加节假日',
                    area: ['650px', '400px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '/schedule/schedule02/exportSchedule/addHoliday/addHoliday.html'
                });
            });

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

        window.queryHoliday=function(from,to){
            $.ajax({
                url:'/schedule/holiday?from='+from+'&to='+to,
                success:result=>{
                    if(result.code===0){
                        exportSchedule.buildHliday(result.data.holidays);
                    }else{
                        console.log(result);
                    }
                }
            });
        };

        exportSchedule.buildHliday=function(dateList){
            const dateItems = dateList.map(
                item => {
                    return $('<li>')
                        .text(window.parseUTCTimeToYMD(item.date));
                }
            );
    
            $('.holiday-container ul')
                .html('')
                .append(dateItems);
        };

        exportSchedule.bindLayer();
        exportSchedule.bindClick();
    };

// $(
//     function(){
//         exportSchedule();
//     }
// )
$(
    function () {

        let isRequestSuccess=false;

        $('.save-btn').click(
            function () {
                const from = $('.holiday-from').val();
                const to = $('.holiday-to').val();

                if (!from || !to) {
                    $('.message-container .message').text('假期时间范围不能为空...');
                    return false;
                }

                if (!window.isDateInRange(from, to, parent.originData.from, parent.originData.to)) {
                    $('.message-container .message').text('假期时间范围需在导出时间范围中...');
                    return false;
                }

                const dayCount = window.calculateDayCount(from, to);

                if (dayCount <= 0) {
                    $('.message-container .message').text('添加失败...请选择合适时间范围');
                    return false;
                }

                let temp = from;

                const dateList=[];

                for (let i = 0; i < dayCount; i++) {
                    dateList.push({
                        date:temp
                    });
                    if (i != dayCount - 1) {
                        temp = window.getNextDate(temp);
                    }
                }

                saveHoliday(dateList,{ success: '添加成功', failure: '添加失败...' });
            }
        );

        $('.cancel-btn').click(function () {
            const index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);

            if(isRequestSuccess){
                parent.queryHoliday(parent.originData.from, parent.originData.to);
            }
        });

        function saveHoliday(dateList, msg) {
            $.ajax({
                url: '/arrange1/holiday/add',
                type:'POST',
                data: {
                    holidays: JSON.stringify(dateList)
                },
                success: result => {
                    if (result.code === 0) {
                        if (msg) {
                            $('.message-container .message').text(msg.success);
                            isRequestSuccess=true;
                        }
                    } else {
                        if (msg) {
                            $('.message-container .message').text(msg.failure + result.message);
                        }
                    }
                }
            });
        }

        function bindLayer() {
            laydate.render({
                elem: '.holiday-from',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
            laydate.render({
                elem: '.holiday-to',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
        }

        bindLayer();
    }
);
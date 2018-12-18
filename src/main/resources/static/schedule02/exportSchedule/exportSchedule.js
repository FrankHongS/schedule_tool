window.exportSchedule = function () {
    const exportSchedule = {};

    let curSpecialArray;

    exportSchedule.bindLayer = function () {
        laydate.render({
            elem: '.export-from',
            theme: '#393D49',
            btns: ['confirm'],
            range: false,
            done: function (value, date) {
                const to = $('.export-to').val();
                if (to) {
                    window.queryHoliday(value, to);
                }
            }
        });
        laydate.render({
            elem: '.export-to',
            theme: '#393D49',
            btns: ['confirm'],
            range: false,
            done: function (value, date) {
                const from = $('.export-from').val();
                if (from) {
                    window.queryHoliday(from, value);
                }
            }
        });
    }

    exportSchedule.bindClick = function () {

        /** */
        $('.group-wrapper ul').on('click', '.level1>a', function (e) {
            $(e.target).addClass('current')
                .next().show()
                .parent().siblings().children('a').removeClass('current')
                .next().hide();
            return false;
        });

        $('.add-group-btn').click(function () {

            window.originData.type = 0;

            layer.open({
                type: 2,
                title: '添加互斥组',
                area: ['380px', '340px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/exportSchedule/editSpecial/editSpecial.html'
                // content: './editSpecial/editSpecial.html'
            });
        });

        $('.edit-group-btn').click(function(){

            const $current=$('.current');

            if(!$current.get(0)){
                alert('请选中组再编辑');
                return;
            }

            const curSpecial=curSpecialArray[$current.parent().index()];

            window.originData.type = 1;
            window.originData.curSpecial=curSpecial;

            layer.open({
                type: 2,
                title: '修改互斥组',
                area: ['380px', '340px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/schedule/schedule02/exportSchedule/editSpecial/editSpecial.html'
            });
        });

        /** */
        $('.holiday-container ul').on('click', 'li', function (e) {
            $(e.target).addClass('active').removeClass('unactive')
                .siblings().removeClass('active').addClass('unactive');

            return false;
        });

        $('.add-holiday').click(function () {

            const from = $('.export-from').val();
            const to = $('.export-to').val();

            if (!from || !to) {
                alert('时间范围不能为空！');
                return;
            }

            window.originData.from = from;
            window.originData.to = to;

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

        $('.delete-holiday').click(function () {
            const $current = $('.holiday-container .active');

            if (!$current.get(0)) {// DOM object
                alert('请选中节假日再删除');
                return;
            }

            if (confirm('确认删除')) {
                const date = $current.text();
                console.log(date);
                $.ajax({
                    url: '/schedule/holiday/delete?date=' + date,
                    success: result => {
                        if (result.code === 0) {
                            alert('删除' + date + '成功');
                            const from = $('.export-from').val();
                            const to = $('.export-to').val();
                            window.queryHoliday(from, to);
                        } else {
                            alert('删除失败...' + result.message);
                        }
                    },
                    error: (xhr, e) => {
                        alert('删除失败...');
                    }
                });

            }
        });

        $('.export-main-container .export-link').on('click', function () {

            const from = $('.export-from').val();
            const to = $('.export-to').val();

            if (!from || !to) {
                alert('时间范围不能为空！');
                return;
            }

            $(this).attr('href', '/schedule/schedule_excel/table?from=' + from + '&to=' + to);
        });
    };

    window.queryHoliday = function (from, to) {
        $.ajax({
            url: '/schedule/holiday?from=' + from + '&to=' + to,
            success: result => {
                if (result.code === 0) {
                    exportSchedule.buildHoliday(result.data.holidays);
                } else {
                    console.log(result);
                }
            }
        });
    };

    exportSchedule.buildHoliday = function (dateList) {
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

    window.querySpecialGroup = function () {
        $.ajax({
            url: '/schedule/mutex_employee',
            success: result => {
                if (result.code === 0) {
                    curSpecialArray=result.data.data;
                    exportSchedule.buildSpecialGroup(curSpecialArray);
                } else {
                    console.log(result);
                }
            }
        });
    };

    exportSchedule.buildSpecialGroup = function (dataArray) {

        const groupItems = dataArray.map(
            (item, index) => {
                const groupEmployeeItems = item.employees.map(
                    employee => {
                        return $('<li>')
                            .text(employee.name + '(' + employee.alias + ')');
                    }
                );

                const groupEmployeeItemsHtml = $('<ul>')
                    .addClass('level2')
                    .append(groupEmployeeItems);

                return $('<li>')
                    .addClass('level1')
                    .append('<a href="#">组' + (index + 1) + '</a>')
                    .append(groupEmployeeItemsHtml);
            }
        );

        $('.group-wrapper ul')
            .html('')
            .append(groupItems);

    };

    window.querySpecialGroup();
    exportSchedule.bindLayer();
    exportSchedule.bindClick();
};

// $(
//     function(){
//         exportSchedule();
//     }
// )
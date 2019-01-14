// window.clickSchdule=false;
window.exportSchedule = function () {
    const exportSchedule = {};

    let curSpecialArray;
    let curSpecialProgramArray;
    var index;
    var canClickGen=true;

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
                content: '/arrange/schedule02/exportSchedule/editSpecial/editSpecial.html'
                // content: './editSpecial/editSpecial.html'
            });
        });

        $('.edit-group-btn').click(function(){

            const $current=$('.special-employee-wrapper .current');

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
                content: '/arrange/schedule02/exportSchedule/editSpecial/editSpecial.html'
            });
        });

        $('.delete-group-btn').click(function () {
            const $current=$('.special-employee-wrapper .current');

            if(!$current.get(0)){
                alert('请选中组再删除');
                return;
            }

            const curSpecial=curSpecialArray[$current.parent().index()];

            if(confirm('确定删除?')){
                $.ajax({
                    url:'/arrange/mutex_employee/delete?id='+curSpecial.id,
                    success:result=>{
                        if(result.code===0){
                            alert('删除成功');
                            window.querySpecialGroup();
                        }else{
                            alert('删除失败...'+result.message);
                        }
                    }
                });
            }
        });

        $('.add-program-group-btn').click(function(){
            window.originData.type = 0;

            layer.open({
                type: 2,
                title: '添加相同节目组',
                area: ['380px', '340px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/arrange/schedule02/exportSchedule/editSpecialProgram/editSpecialProgram.html'
            });
        });

        $('.edit-program-group-btn').click(function(){

            const $current=$('.special-program-wrapper .current');

            if(!$current.get(0)){
                alert('请选中节目组再编辑');
                return;
            }

            const curSpecialProgram=curSpecialProgramArray[$current.parent().index()];

            window.originData.type = 1;
            window.originData.curSpecialProgram=curSpecialProgram;

            layer.open({
                type: 2,
                title: '修改相同节目组',
                area: ['380px', '340px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/arrange/schedule02/exportSchedule/editSpecialProgram/editSpecialProgram.html'
            });
        });

        $('.delete-program-group-btn').click(function(){

            const $current=$('.special-program-wrapper .current');

            if(!$current.get(0)){
                alert('请选中节目组再删除');
                return;
            }

            const curSpecialProgram=curSpecialProgramArray[$current.parent().index()];

            if(confirm('确定删除?')){
                $.ajax({
                    url:'/arrange/equal_role/delete?id='+curSpecialProgram.id,
                    success:result=>{
                        if(result.code===0){
                            alert('删除成功');
                            window.querySpecialProgramGroup();
                        }else{
                            alert('删除失败...'+result.message);
                        }
                    }
                });
            }
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
                content: '/arrange/schedule02/exportSchedule/addHoliday/addHoliday.html'
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
                    url: '/arrange/holiday/delete?date=' + date,
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

        $('.export-main-container .gen-info').click(function(){
            if(!canClickGen){
                alert('请勿重复点击');
                return;
            }
            // window.clickSchdule=true;
            canClickGen=false;
            const from = $('.export-from').val();
            const to = $('.export-to').val();

            if (!from || !to) {
                alert('时间范围不能为空！');
                return;
            }

            $('.export-btns .message').text('正在生成排班记录,请稍等...');
            $.ajax({
                url:'/arrange/schedule/schedue?from=' + from + '&to=' + to,
                success:result=>{
                    $('.export-btns .message').text('');
                    canClickGen=true;
                    // window.clickSchdule=false;

                    if(result.code===0){
                        alert('生成排班记录成功!');
                        layer.close(index);
                    }else{
                        alert('生成排班记录失败!'+result.message);
                        layer.close(index);
                    }
                }
            });
            index = layer.open({
                type: 2,
                title:"排班进度",
                area: ['380px', '143px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/arrange/schedule02/exportSchedule/exportProgress/exportProgress.html'
                // content: './editSpecial/editSpecial.html'
            });
        });

        $('.export-main-container .export-link').on('click', function () {

            const from = $('.export-from').val();
            const to = $('.export-to').val();

            if (!from || !to) {
                alert('时间范围不能为空！');
                return;
            }

            $(this).attr('href', '/schedule/excel/export_schedule?from=' + from + '&to=' + to+'&isHoliday=false');
        });
        $('.check-progress').click(function(){
            index=layer.open({
                type: 2,
                title:"排班进度",
                area: ['380px', '143px'],
                fix: false,
                maxmin: false,
                scrollbar: false,
                content: '/arrange/schedule02/exportSchedule/exportProgress/exportProgress.html'
                // content: './editSpecial/editSpecial.html'
            });

        });
        $('.cancel-schedule').click(function () {
            $.ajax({
                url:'/arrange/schedule/cancel',
                success:result=>{
                    if(result.code===0){
                        $('.export-btns .message').text('取消成功');
                    }else{
                        $('.export-btns .message').text('取消失败');
                    }
                    canClickGen=true;
                    // window.clickSchdule=false;

                }
            });
        });
    };

    window.queryHoliday = function (from, to) {
        $.ajax({
            url: '/arrange/holiday?from=' + from + '&to=' + to,
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
            url: '/arrange/mutex_employee',
            success: result => {
                if (result.code === 0) {
                    curSpecialArray=result.data.data;
                    exportSchedule.buildSpecialGroup('.special-employee-wrapper ul','人员',curSpecialArray);
                } else {
                    console.log(result);
                }
            }
        });
    };

    window.querySpecialProgramGroup=function(){
        $.ajax({
            url: '/arrange/equal_role',
            success: result => {
                if (result.code === 0) {
                    curSpecialProgramArray=result.data.data;
                    exportSchedule.buildSpecialGroup('.special-program-wrapper ul','节目',curSpecialProgramArray);
                } else {
                    console.log(result);
                }
            }
        });
    };

    exportSchedule.buildSpecialGroup = function (container,desc,dataArray) {

        const groupItems = dataArray.map(
            (item, index) => {
                let groupItems;
                if(item.employees){
                    groupItems = item.employees.map(
                        employee => {
                            return $('<li>')
                                .text(employee.name + '(' + employee.alias + ')');
                        }
                    );
                }else if(item.rolesList){
                    groupItems = item.rolesList.map(
                        role => {
                            return $('<li>')
                                .text(role.programName + '(' + role.roleName + ')');
                        }
                    );
                }

                const groupItemsHtml = $('<ul>')
                    .addClass('level2')
                    .append(groupItems);

                return $('<li>')
                    .addClass('level1')
                    .append('<a href="#">'+desc+'组' + (index + 1) + '</a>')
                    .append(groupItemsHtml);
            }
        );

        $(container)
            .html('')
            .append(groupItems);

    };


    window.querySpecialGroup();
    window.querySpecialProgramGroup();
    exportSchedule.bindLayer();
    exportSchedule.bindClick();
};

// $(
//     function(){
//         exportSchedule();
//     }
// )
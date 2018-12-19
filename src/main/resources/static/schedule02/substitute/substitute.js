window.substitute = function () {
    const substitute = {};

    let curSubstituteArray;

    substitute.bindLayer = function () {
        laydate.render({
            elem: '.from',
            theme: '#393D49',
            btns: ['confirm'],
            range: false
        });
        laydate.render({
            elem: '.to',
            theme: '#393D49',
            btns: ['confirm'],
            range: false
        });
    };

    substitute.bindClick = function () {
        $('.add-btn').on('click', function () {
            layer.open({
                type: 2,
                title: '添加替班信息',
                area: ['480px', '400px'],
                fix: false,
                maxmin: false,
                scrollbar: true,
                content: '/schedule/schedule02/substitute/addSubstitute/addSubstitute.html'
                // './addSubstitute/addSubstitute.html'
            });
        });

        $('.substitute-main-container .export-link').on('click', function () {

            const from = $('.from').val();
            const to = $('.to').val();
            const isHoliday = $('#hol:checked').length == 1 ? true : false;

            // if ((from && !to) || (!from && to)) {
            //     alert('时间范围需不为空... :)');
            //     return;
            // }

            if (from && to) {
                if(isHoliday){
                    $(this).attr('href', '/schedule/excel/export_schedule?isHoliday=' + isHoliday + '&from=' + from + '&to=' + to);
                }else{
                    $(this).attr('href', '/schedule/excel/export_replace?from=' + from + '&to=' + to);
                }
            } else {
                alert('时间范围需不为空... :)');
                return;
            }
        });

        $('.search-btn').on('click', () => {
            const from = $('.from').val();
            const to = $('.to').val();
            const isHoliday = $('#hol:checked').length == 1 ? true : false;

            if ((from && !to) || (!from && to)) {
                alert('时间范围需同为空或同不为空... :)');
                return;
            }

            if (from && to) {
                if (isHoliday) {
                    $.ajax({
                        url: '/schedule/schedule?isHoliday=' + isHoliday + '&from=' + from + '&to=' + to,
                        success: result => {
                            if (result.code == 0) {
                                this.createTable('.tb-container .sub-body', this.parseData(result.data.data, true), true);
                            } else {
                                alert('检索失败 ' + result.message);
                            }
                        }
                    });
                } else {
                    $.ajax({
                        url: '/schedule/replace?from=' + from + '&to=' + to,
                        success: result => {
                            if (result.code == 0) {
                                this.createTable('.tb-container .sub-body', this.parseData(result.data.data, false), false);
                            } else {
                                alert('检索失败 ' + result.message);
                            }
                        }
                    });
                }
            } else {
                if (isHoliday) {
                    $.ajax({
                        url: '/schedule/schedule/holiday',
                        success: result => {
                            if (result.code == 0) {
                                this.createTable('.tb-container .sub-body', this.parseData(result.data.data, true), true);
                            } else {
                                alert('检索失败 ' + result.message);
                            }
                        }
                    });
                } else {
                    $.ajax({
                        url: '/schedule/replace/findAll',
                        success: result => {
                            if (result.code == 0) {
                                this.createTable('.tb-container .sub-body', this.parseData(result.data.data, false), false);
                            } else {
                                alert('检索失败 ' + result.message);
                            }
                        }
                    });
                }
            }

        });
    };

    substitute.parseData = function (dataArray, isHoliday) {

        curSubstituteArray=dataArray;

        const list = [];

        for (let i = 0; i < dataArray.length; i++) {
            let item = dataArray[i];
            let itemList = [];

            itemList[0] = i + 1;
            itemList[1] = item.name + '(' + item.alias + ')';
            itemList[2] = item.programName + '(' + item.roleName + ')';
            itemList[3] = parseUTCTimeToYMD(item.date);
            itemList[4] = isHoliday == true ? '是' : '否';
            itemList[5] = 'delete';

            list[i] = itemList;
        }

        return list;
    };

    substitute.createTable = function (container, dataList, isHoliday) {

        const cellsArray = dataList.map(
            rowValues => {
                return rowValues.map(
                    (cells, index) => {
                        if (index === rowValues.length - 1) {
                            return $('<td>')
                                .addClass('delete')
                                .text(cells);
                        } else {
                            return $('<td>')
                                .text(cells);
                        }
                    }
                );
            }
        );

        const rowsArray = cellsArray.map(
            row => {
                return $('<tr>')
                    .append(row);
            }
        );

        $(container)
            .html('')
            .append(rowsArray);

        $('.delete').click(function () {

            const index=$(this).parent().index();

            const curSubstitute=curSubstituteArray[index];

            if(confirm('确认删除?')){
                if(isHoliday){
                    $.ajax({
                        url:'/schedule/schedule/delete_holiday?id='+curSubstitute.id,
                        success:result=>{
                            if(result.code===0){
                                alert('删除成功');
                            }else{
                                alert('删除失败...'+result.message)
                            }
                        }
                    });
                }else{
                    $.ajax({
                        url:'/schedule/replace/delete?id='+curSubstitute.id,
                        success:result=>{
                            if(result.code===0){
                                alert('删除成功');
                            }else{
                                alert('删除失败...'+result.message)
                            }
                        }
                    });
                }
            }
        });
    };

    substitute.bindLayer();
    substitute.bindClick();
};

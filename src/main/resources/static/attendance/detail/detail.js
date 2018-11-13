$(
    function () {
        const detail = {};

        const leaveClassGroup = ['index', 'leaveType', 'created-time', 'time', 'length', 'comment', 'is-normal', 'edit','delete'];

        const lateClassGroup = ['index', 'lateType', 'created-time', 'time', 'comment', 'is-normal', 'edit','delete'];

        //跨域传递name,alias,annual,employeeId给modify.html
        window.profile = {};

        const originDataLeaveArray = [];
        window.originDataLeave = {};

        const originDataLateArray = [];
        window.originDataLate = {};

        // time range
        let from;
        let to;

        detail.getParameter = function () {
            const profileArray = decodeURIComponent($.Request('profile')).split(' , ');
            if (profileArray.length > 0) {
                profile.name = profileArray[0];
                profile.alias = profileArray[1];
                profile.annual=profileArray[2];
                profile.employeeId = profileArray[3];
                $("#title h1").text(profile.name + '(' + profile.alias + ')')
            }

            from = decodeURIComponent($.Request('from'));
            to = decodeURIComponent($.Request('to'));
        };

        detail.createTable = function (container, dataList, classGroup, originDataArray) {
            const cellsArray = dataList.map(
                rowValues => {
                    return rowValues.map(
                        (cells, index) => {
                            return $('<td>')
                                .addClass(classGroup[index % classGroup.length])
                                .text(cells);
                        }
                    );
                }
            );

            const rowsArray = cellsArray.map(
                (row, index) => {
                    return $('<tr>')
                        .attr('name', originDataArray[index])
                        .append(row);
                }
            );

            $(container).append(rowsArray);
        };

        detail.buildTypeMenu = function (container, dataArray) {
            $('select#item-type').html('');
            const rowsArray = dataArray.map(
                (row, index) => {
                    return $('<option>')
                        .attr('value', index)
                        .append(row);
                }
            )

            $(container).append(rowsArray);
        };

        detail.bindClick = function () {
            $('.modify-profile-btn').bind('click', function (e) {
                layer.open({
                    type: 2,
                    title: '修改成员信息',
                    area: ['450px', '400px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: false,
                    content: '../modify/modify.html'
                });

            });

        };

        detail.bindLeaveClick = function () {
            $('.leave .edit').bind('click', function () {

                const tempArray = $(this).parent('tr').attr('name').split(' , ');
                originDataLeave.leaveId = tempArray[0];
                originDataLeave.leaveType = tempArray[1];
                originDataLeave.name = tempArray[2];
                originDataLeave.alias = tempArray[3];
                originDataLeave.leaveDateRange = tempArray[4];
                originDataLeave.normal = tempArray[5];
                originDataLeave.comment = tempArray[6];

                layer.open({
                    type: 2,
                    title: '修改请假信息',
                    area: ['800px', '560px'],
                    fix: true, //不固定
                    maxmin: true,
                    scrollbar: false,//屏蔽父窗口滚动条
                    content: '../modify_leave/modify_leave.html'
                });
            });

            //delete leave
            $('.leave .delete').bind('click',function(){
                const result=confirm('删除该项');
                if(result==true){
                    const tempArray = $(this).parent('tr').attr('name').split(' , ');
                    const leaveId=tempArray[0];
                    $.ajax({
                        url: '/schedule/leave/delete',
                        type: 'POST',
                        data:{
                            id: leaveId
                        },
                        success: result => {
                            alert('删除成功');
                            console.log(result);
                        },
                        error:(xhr,e)=>{
                            alert('删除失败...'+e);
                        }
                    });
                }
            });
        };

        detail.bindLateClick = function () {
            $('.late .edit').bind('click', function () {

                const tempArray = $(this).parent('tr').attr('name').split(' , ');
                originDataLate.lateId = tempArray[0];
                originDataLate.lateType = tempArray[1];
                originDataLate.name = tempArray[2];
                originDataLate.alias = tempArray[3];
                originDataLate.lateDate = tempArray[4];
                originDataLate.normal = tempArray[5];
                originDataLate.comment = tempArray[6];

                layer.open({
                    type: 2,
                    title: '修改考勤异常信息',
                    area: ['800px', '560px'],
                    fix: true, //不固定
                    maxmin: true,
                    scrollbar: false,//屏蔽父窗口滚动条
                    content: '../modify_late/modify_late.html'
                });
            });

            $('.late .delete').bind('click',function(){
                const result=confirm('删除该项');
                if(result==true){
                    const tempArray = $(this).parent('tr').attr('name').split(' , ');
                    const lateId=tempArray[0];
                    $.ajax({
                        url: '/schedule/late/delete',
                        type: 'POST',
                        data:{
                            id: lateId
                        },
                        success: result => {
                            alert('删除成功');
                            console.log(result);
                        },
                        error:(xhr,e)=>{
                            alert('删除失败...'+e);
                        }
                    });
                }
            });
        };

        detail.bindAjax = function () {
            $('.leave-search').bind('click', () => {
                const leaveType = $('.leave-type').children('option:selected').val();

                let url;
                if (leaveType == 0) {
                    if (from == 0 && to == 0) {
                        url = '/schedule/leave?employeeId=' + profile.employeeId;
                    } else if (from != 0 && to != 0) {
                        url = '/schedule/leave/range?employeeId=' + profile.employeeId + '&from=' + from + '&to=' + to;
                    }
                } else {
                    if (from == 0 && to == 0) {
                        url = '/schedule/leave/type?employeeId=' + profile.employeeId + '&leaveType=' + (leaveType - 1);
                    } else if (from != 0 && to != 0) {
                        url = '/schedule/leave/range_and_type?employeeId=' + profile.employeeId + '&from=' + from + '&to=' + to + '&leaveType=' + (leaveType - 1);
                    }
                }

                this.getLeaveRequest(url);

            });

            $('.late-search').bind('click', () => {
                const lateType = $('.late-type').children('option:selected').val();

                let url;
                if (lateType == 0) {
                    if (from == 0 && to == 0) {
                        url = '/schedule/late?employeeId=' + profile.employeeId;
                    } else if (from != 0 && to != 0) {
                        url = '/schedule/late/range?employeeId=' + profile.employeeId + '&from=' + from + '&to=' + to;
                    }
                } else {
                    if (from == 0 && to == 0) {
                        url = '/schedule/late/type?employeeId=' + profile.employeeId + '&lateType=' + (lateType - 1);
                    } else if (from != 0 && to != 0) {
                        url = '/schedule/late/range_and_type?employeeId=' + profile.employeeId + '&from=' + from + '&to=' + to + '&leaveType=' + (lateType - 1);
                    }
                }

                this.getLateRequest(url);

            });
        };


        detail.getLeaveRequest = function (url) {
            $.ajax({
                url: url,
                success: result => {
                    $('.leave-body').html('');
                    this.createTable('.leave-body', this.parseLeaveData(result.leave), leaveClassGroup, originDataLeaveArray);
                    this.bindLeaveClick();
                }
            });
        };

        detail.getLateRequest = function (url) {
            $.ajax({
                url: url,
                success: result => {
                    $('.late-body').html('');
                    this.createTable('.late-body', this.parseLateData(result.late), lateClassGroup, originDataLateArray);
                    this.bindLateClick();
                }
            });
        };

        detail.parseLeaveData = function (dataArray) {
            const list = [];

            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let itemList = [];

                itemList[0] = i + 1;
                itemList[1] = window.leaveTypeArray[item.leaveType + 1];
                itemList[2] = parseUTCTime(item.createdTime);
                itemList[3] = parseUTCTimeToYMD(item.from) + ' - ' + parseUTCTimeToYMD(item.to);
                itemList[4] = item.dayCount;
                itemList[5] = item.comment;
                itemList[6] = item.normal ? '正常' : '异常';
                itemList[7] = 'edit';
                itemList[8] = 'delete';

                list[i] = itemList;

                originDataLeaveArray[i] = item.id + ' , ' + item.leaveType + ' , ' + item.name + ' , ' + item.alias + ' , ' + itemList[3] + ' , ' + item.normal + ' , ' + item.comment;
            }

            return list;
        };

        detail.parseLateData = function (dataArray) {
            const list = [];

            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let itemList = [];

                itemList[0] = i + 1;
                itemList[1] = window.lateTypeArray[item.lateType + 1];
                itemList[2] = parseUTCTime(item.createdTime);
                itemList[3] = parseUTCTimeToYMD(item.lateDate);
                itemList[4] = item.comment;
                itemList[5] = item.normal ? '正常' : '异常';
                itemList[6] = 'edit';
                itemList[7] = 'delete';

                list[i] = itemList;

                originDataLateArray[i] = item.id + ' , ' + item.lateType + ' , ' + item.name + ' , ' + item.alias + ' , ' + itemList[3] + ' , ' + item.normal + ' , ' + item.comment;
            }

            return list;
        };

        detail.buildUI = function () {

            this.buildTypeMenu('select.leave-type', window.leaveTypeArray);
            this.buildTypeMenu('select.late-type', window.lateTypeArray);

            this.bindClick();
        };

        detail.getParameter();
        detail.buildUI();
        detail.bindAjax();
    }
);
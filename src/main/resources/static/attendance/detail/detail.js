$(
    function () {
        const detail = {};

        const leaveClassGroup = ['index', 'leaveType','created-time', 'time', 'length', 'comment','is-normal','edit'];

        const lateClassGroup = ['index','lateType', 'created-time', 'time', 'comment','is-normal','edit'];

        //跨域传递name,alias,employeeId给modify.html
        window.title={};

        const originDataLeaveArray=[];
        window.originDataLeave={};

        const originDataLateArray=[];
        window.originDataLate={};

        // time range
        let from;
        let to;
        
        detail.getParameter = function () {
            const titleArray = decodeURIComponent($.Request('title')).split(' , ');
            if (titleArray.length>0) {
                title.name=titleArray[0];
                title.alias=titleArray[1];
                title.employeeId=titleArray[2];
                $("#title h1").text(title.name + '('+title.alias +')')
            }

            from=decodeURIComponent($.Request('from'));
            to=decodeURIComponent($.Request('to'));
        };

        detail.createTable=function(container,dataList,classGroup,originDataArray){
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
                (row,index) => {
                    return $('<tr>')
                        .attr('name',originDataArray[index])
                        .append(row);
                }
            );

            $(container).append(rowsArray);
        };

        detail.buildTypeMenu=function(container,dataArray){
            $('select#item-type').html('');
            const rowsArray=dataArray.map(
                (row,index)=>{
                   return $('<option>')
                        .attr('value',index)
                        .append(row);
                }
            )

            $(container).append(rowsArray);
        };

        detail.bindClick=function(){
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

        detail.bindLeaveLayer=function(){
            $('.leave .edit').bind('click',function(){

                const tempArray=$(this).parent('tr').attr('name').split(' , ');
                originDataLeave.leaveId=tempArray[0];
                originDataLeave.leaveType=tempArray[1];
                originDataLeave.name=tempArray[2];
                originDataLeave.alias=tempArray[3];
                originDataLeave.leaveDateRange=tempArray[4];
                originDataLeave.normal=tempArray[5];
                originDataLeave.comment=tempArray[6];

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
        };

        detail.bindLateLayer=function(){
            $('.late .edit').bind('click',function(){

                const tempArray=$(this).parent('tr').attr('name').split(' , ');
                originDataLate.lateId=tempArray[0];
                originDataLate.lateType=tempArray[1];
                originDataLate.name=tempArray[2];
                originDataLate.alias=tempArray[3];
                originDataLate.lateDate=tempArray[4];
                originDataLate.normal=tempArray[5];
                originDataLate.comment=tempArray[6];

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
        };

        detail.bindAjax=function(){
            $('.leave-search').bind('click',()=>{
                console.log('start ajax....');

                const leaveType=$('.leave-type').children('option:selected').val();

                if(leaveType==0){
                    if(from==0&&to==0){
                        $.ajax({
                            url:'/schedule/leave?employeeId='+title.employeeId,
                            success:result=>{
                                $('.leave-body').html('');
                                this.createTable('.leave-body',this.parseLeaveData(result.leave),leaveClassGroup,originDataLeaveArray);
                                this.bindLeaveLayer();
                            }
                        });
                    }else if(from!=0&&to!=0){
                        $.ajax({
                            url:'/schedule/leave/range?employeeId='+title.employeeId+'&from='+from+'&to='+to,
                            success:result=>{
                                $('.leave-body').html('');
                                this.createTable('.leave-body',this.parseLeaveData(result.leave),leaveClassGroup,originDataLeaveArray);
                                this.bindLeaveLayer();
                            }
                        });
                    }
                }else{
                    if(from==0&&to==0){
                        $.ajax({
                            url:'/schedule/leave/type?employeeId='+title.employeeId+'&leaveType='+(leaveType-1),
                            success:result=>{
                                $('.leave-body').html('');
                                this.createTable('.leave-body',this.parseLeaveData(result.leave),leaveClassGroup,originDataLeaveArray);
                                this.bindLeaveLayer();
                            }
                        });
                    }else if(from!=0&&to!=0){
                        $.ajax({
                            url:'/schedule/leave/range_and_type?employeeId='+title.employeeId+'&from='+from+'&to='+to+'&leaveType='+(leaveType-1),
                            success:result=>{
                                $('.leave-body').html('');
                                this.createTable('.leave-body',this.parseLeaveData(result.leave),leaveClassGroup,originDataLeaveArray);
                                this.bindLeaveLayer();
                            }
                        });
                    }
                }

            });

            $('.late-search').bind('click',()=>{
                console.log('start ajax....');
                const lateType=$('.late-type').children('option:selected').val();

                if(lateType==0){
                    if(from==0&&to==0){
                        $.ajax({
                            url:'/schedule/late?employeeId='+title.employeeId,
                            success:result=>{
                                $('.late-body').html('');
                                this.createTable('.late-body',this.parseLateData(result.late),lateClassGroup,originDataLateArray);
                                this.bindLateLayer();
                            }
                        });
                    }else if(from!=0&&to!=0){
                        $.ajax({
                            url:'/schedule/late/range?employeeId='+title.employeeId+'&from='+from+'&to='+to,
                            success:result=>{
                                $('.late-body').html('');
                                this.createTable('.late-body',this.parseLateData(result.late),lateClassGroup,originDataLateArray);
                                this.bindLateLayer();
                            }
                        });
                    }
                }else{
                    if(from==0&&to==0){
                        $.ajax({
                            url:'/schedule/late/type?employeeId='+title.employeeId+'&lateType='+(lateType-1),
                            success:result=>{
                                $('.late-body').html('');
                                this.createTable('.late-body',this.parseLateData(result.late),lateClassGroup,originDataLateArray);
                                this.bindLateLayer();
                            }
                        });
                    }else if(from!=0&&to!=0){
                        $.ajax({
                            url:'/schedule/late/range_and_type?employeeId='+title.employeeId+'&from='+from+'&to='+to+'&leaveType='+(lateType-1),
                            success:result=>{
                                $('.late-body').html('');
                                this.createTable('.late-body',this.parseLateData(result.late),lateClassGroup,originDataLateArray);
                                this.bindLateLayer();
                            }
                        });
                    }
                }

            });
        };

        detail.parseLeaveData=function(dataArray){
            const list=[];

            for(let i=0;i<dataArray.length;i++){
                let item=dataArray[i];
                let itemList=[];

                itemList[0]=i+1;
                itemList[1]=window.leaveTypeArray[item.leaveType+1];
                itemList[2]=parseUTCTime(item.createdTime);
                itemList[3]=parseUTCTimeToYMD(item.from)+' - '+parseUTCTimeToYMD(item.to);
                itemList[4]=item.dayCount;
                itemList[5]=item.comment;
                itemList[6]=item.normal?'正常':'异常';
                itemList[7]='edit';

                list[i]=itemList;

                originDataLeaveArray[i]=item.id+' , '+item.leaveType+' , '+item.name+' , '+item.alias+' , '+itemList[3]+' , '+item.normal+' , '+item.comment;
            }

            return list;
        };

        detail.parseLateData=function(dataArray){
            const list=[];

            for(let i=0;i<dataArray.length;i++){
                let item=dataArray[i];
                let itemList=[];

                itemList[0]=i+1;
                itemList[1]=window.lateTypeArray[item.lateType+1];
                itemList[2]=parseUTCTime(item.createdTime);
                itemList[3]=parseUTCTimeToYMD(item.lateDate);
                itemList[4]=item.comment;
                itemList[5]=item.normal?'正常':'异常';
                itemList[6]='edit';

                list[i]=itemList;

                originDataLateArray[i]=item.id+' , '+item.lateType+' , '+item.name+' , '+item.alias+' , '+itemList[3]+' , '+item.normal+' , '+item.comment;
            }

            return list;
        };

        detail.buildUI = function () {

            this.buildTypeMenu('select.leave-type',window.leaveTypeArray);
            this.buildTypeMenu('select.late-type',window.lateTypeArray);

            this.bindClick();
        };

        detail.getParameter();
        detail.buildUI();
        detail.bindAjax();
    }
);
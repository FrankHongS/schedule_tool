$(
    function () {
        const detail = {};

        const leaveClassGroup = ['index', 'created-time', 'time', 'length', 'comment','is-normal','edit'];

        const lateClassGroup = ['index', 'created-time', 'time', 'comment','is-normal','edit'];

        //跨域传递name,alias,employeeId给modify.html
        window.title={};
        
        detail.getParameter = function () {
            const titleArray = decodeURIComponent($.Request('title')).split(' ');
            if (titleArray.length>0) {
                title.name=titleArray[0];
                title.alias=titleArray[1];
                title.employeeId=titleArray[2];
                $("#title h1").text(title.name + '('+title.alias +')  考勤情况')
            }
        };

        detail.createTable=function(container,dataList,classGroup){
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
                row => {
                    return $('<tr>')
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

        detail.bindAjax=function(){
            $('.leave-search').bind('click',()=>{
                console.log('start ajax....');

                const leaveType=$('.leave-type').children('option:selected').val();

                $.ajax({
                    url:'/schedule/leave/type?employeeId='+title.employeeId+'&leaveType='+leaveType,
                    success:result=>{
                        this.createTable('.leave-body',this.parseLeaveData(result.leave),leaveClassGroup);
                        this.bindLeaveLayer();
                    }
                });
            });

            $('.late-search').bind('click',()=>{
                console.log('start ajax....');
                const lateType=$('.late-type').children('option:selected').val();
                $.ajax({
                    url:'/schedule/late/type?employeeId='+title.employeeId+'&lateType='+lateType,
                    success:result=>{
                        this.createTable('.late-body',this.parseLateData(result.late),lateClassGroup);
                    }
                });
            });
        };

        detail.parseLeaveData=function(dataArray){
            const list=[];

            for(let i=0;i<dataArray.length;i++){
                let item=dataArray[i];
                let itemList=[];

                itemList[0]=i+1;
                itemList[1]=parseUTCTime(item.createdTime);
                itemList[2]=item.leaveDateRange;
                itemList[3]=item.dayCount;
                itemList[4]=item.comment;
                itemList[5]=item.normal?'正常':'异常';
                itemList[6]='edit';

                list[i]=itemList;
            }

            return list;
        };

        detail.parseLateData=function(dataArray){
            const list=[];

            for(let i=0;i<dataArray.length;i++){
                let item=dataArray[i];
                let itemList=[];

                itemList[0]=i+1;
                itemList[1]=parseUTCTime(item.createdTime);
                itemList[2]=item.lateDate;
                itemList[3]=item.comment;
                itemList[4]=item.normal?'正常':'异常';
                itemList[5]='edit';

                list[i]=itemList;
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
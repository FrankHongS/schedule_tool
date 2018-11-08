$(
    function () {
        const edit = {};

        edit.buildUI = function () {

            const that=this;
            const render = laydate.render({
                elem: '.datepicker',
                theme: '#393D49',
                btns: ['confirm'],
                range: '-'
            });
            window.leaveTypeArray.splice(0,1);
            window.lateTypeArray.splice(0,1);

            that.buildTypeMenu(window.leaveTypeArray);
            that.buildTitle();

            $('#event-type').bind('change', function () {
                const value = $(this).children('option:selected').val();
                switch (value) {
                    case '1':
                        $('#edit-form')[0].reset();

                        $('.time-label').text('请假时间');
                        $('.type-label').text('请假类型');
                        $('.datepicker').attr('placeholder', '  -')
                        render.config.range = '-';//设置laydate的属性

                        that.buildTypeMenu(window.leaveTypeArray);
                        break;
                    case '2':
                        $('#edit-form')[0].reset();

                        $('.time-label').text('时间');
                        $('.type-label').text('考勤类型');
                        $('.datepicker').attr('placeholder', '发生日期')
                        render.config.range = false;

                        that.buildTypeMenu(window.lateTypeArray);
                        break;
                    default:
                        break;
                }
            });

        };

        edit.bindClick=function(){
            $('.cancel').bind('click',function(){
                const index=parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            });
        };
        
        edit.buildTypeMenu=function(dataArray){
            $('select#item-type').html('');
            const rowsArray=dataArray.map(
                (row,index)=>{
                   return $('<option>')
                        .attr('value',index)
                        .append(row);
                }
            )

            $('select#item-type').append(rowsArray);
        };

        edit.buildTitle=function(){
            $('.name').val(window.parent.title.name);
            $('.name').attr('readonly','readonly');
            $('.alias').val(window.parent.title.alias);
            $('.alias').attr('readonly','readonly');
        };

        edit.bindAjax = function () {
            $('.save').bind('click', function (e) {
                $('.message').text('正在保存...');

                let data;
                let url;
                const value = $('#event-type').children('option:selected').val();
                const typeValue=$('#item-type').children('option:selected').val();
                switch (value) {
                    case '1':
                        data = {
                            name: $('.name').val(),
                            alias: $('.alias').val(),
                            leaveType: typeValue,
                            leaveDateRange: $('.datepicker').val(),
                            dayCount: calculateDayCount($('.datepicker').val()),
                            employeeId: window.parent.title.employeeId,
                            isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                            comment: $('.comment-text').val()
                        };
                        console.log(data);
                        
                        url = '/schedule/leave';
                        break;
                    case '2':
                        data = {
                            name: $('.name').val(),
                            alias: $('.alias').val(),
                            lateType: typeValue,
                            lateDate: $('.datepicker').val(),
                            employeeId: window.parent.title.employeeId,
                            isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                            comment: $('.comment-text').val()
                        };
                        console.log(data);
                        
                        url = '/schedule/late';
                        break;
                    default:
                        break;
                }

                $.ajax({
                    url: url,
                    type: 'POST',
                    data: data,
                    success: result => {
                        $('.message').text('保存成功');
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        $('.message').text('保存失败...'+e);
                        console.log(e);
                    }
                });
            });
        };

        edit.buildUI();
        edit.bindClick();
        edit.bindAjax();
    }
);
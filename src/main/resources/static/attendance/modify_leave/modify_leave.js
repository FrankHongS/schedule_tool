$(
    function () {
        const edit = {};

        edit.buildUI = function () {

            const that=this;
            const render = laydate.render({
                elem: '.datepicker',
                theme: '#393D49',
                btns: ['confirm'],
                range: true
            });

            that.buildTypeMenu(window.leaveTypeArray);
            that.buildTitle();

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

        edit.buildOriginData=function(){
            $('#item-type').children('option:selected').val(window.parent.origin_data.type)
            $('.name').val(window.parent.origin_data.name);
            $('.name').attr('readonly','readonly');
            $('.alias').val(window.parent.origin_data.alias);
            $('.alias').attr('readonly','readonly');
            $('.datepicker').val(window.parent.origin_data.leaveDateRange);
            $('.comment-text').val(window.parent.origin_data.comment);
        };

        edit.bindAjax = function () {
            $('.save').bind('click', function (e) {
                console.log('start ajax...');

                $.ajax({
                    url: '/schedule/leave/update',
                    type: 'POST',
                    data: {
                        name: $('.name').val(),
                        alias: $('.alias').val(),
                        leaveType: typeValue,
                        leaveDateRange: $('.datepicker').val(),
                        dayCount: calculateDayCount($('.datepicker').val()),
                        employeeId: window.parent.title.employeeId,
                        isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                        comment: $('.comment-text').val()
                    },
                    success: result => {
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        console.log(e);
                    }
                });
            });
        };

        edit.buildUI();
        edit.bindAjax();
    }
);
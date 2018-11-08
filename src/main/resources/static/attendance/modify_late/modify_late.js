$(
    function () {
        const edit = {};

        edit.buildUI = function () {
            
            const that=this;
            laydate.render({
                elem: '.datepicker',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });

            window.lateTypeArray.splice(0,1);
            that.buildTypeMenu(window.lateTypeArray);
            that.buildOriginData();

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
            $('#item-type').val(window.parent.originDataLate.lateType);//设置select选中的值
            $('.name').val(window.parent.originDataLate.name);
            $('.name').attr('readonly','readonly');
            $('.alias').val(window.parent.originDataLate.alias);
            $('.alias').attr('readonly','readonly');
            $('.datepicker').val(window.parent.originDataLate.lateDate);

            if(window.parent.originDataLate.normal=='true'){// normal返回的是一个字符串，不是boolean类型
                $("input[name='isNormal']").get(0).checked=true;
            }else{
                $("input[name='isNormal']").get(1).checked=true;
            }
            $('.comment-text').val(window.parent.originDataLate.comment);
        };

        edit.bindAjax = function () {
            $('.save').bind('click', function (e) {
                $('.message').text('正在更新...');

                const typeValue=$('#item-type').children('option:selected').val();

                $.ajax({
                    url: '/schedule/late/update',
                    type: 'POST',
                    data: {
                        id: window.parent.originDataLate.lateId,
                        lateType: typeValue,
                        lateDate: $('.datepicker').val(),
                        isNormal: $('.choose input[name="isNormal"]:checked').val()==='1' ? true : false,
                        comment: $('.comment-text').val()
                    },
                    success: result => {
                        $('.message').text('更新成功');
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        $('.message').text('更新失败...'+e);
                        console.log(e);
                    }
                });
            });

            $('.delete').bind('click', function (e) {
                $('.message').text('正在删除...');
                $.ajax({
                    url: '/schedule/late/delete',
                    type: 'POST',
                    data:{
                        id: window.parent.originDataLate.lateId
                    },
                    success: result => {
                        $('.message').text('删除成功');
                        console.log(result);
                    },
                    error:(xhr,e)=>{
                        $('.message').text('删除失败...'+e);
                        console.log(e);
                    }
                });
            });
        };

        edit.buildUI();
        edit.bindAjax();
    }
);
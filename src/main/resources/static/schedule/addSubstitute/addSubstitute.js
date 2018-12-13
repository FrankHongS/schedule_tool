$(
    function(){

        const addSubstitute={};

        addSubstitute.buildProgramList=function(){
            $.ajax({
                url:'/schedule/program',
                success:result=>{
                    if(result.code==0){
                        const rowsArray=result.data.program.map(
                            (item,index)=>{
                               return $('<option>')
                                    .val(index)
                                    .append(item.name);
                            }
                        )
            
                        $('select#sub-program').append(rowsArray);
                    }else{
                        console.log(result);
                    }
                }
            });
        };

        addSubstitute.bindClick=function(){
            $('.save-btn').on('click',function(){
                $.ajax({
                    url:'/schedule/substitute',
                    type:'POST',
                    data:{
                        subDate:$('.sub-date').val(),
                        subProgram:$('#sub-program').children('option:selected').text(),
                        subName:$('.sub-name').val(),
                        isHoliday:$('#sub-hol:checked').length==1?true:false
                    },
                    success:result=>{
                        if(result.code==0){
                            $('.message').text('保存成功');
                            console.log(result)
                        }else{
                            $('.message').text('保存失败'+result.message);
                        }
                    }
                });
            });

            $('.cancel-btn').bind('click',function(){
                const index=parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            });
        };

        addSubstitute.bindLayer=function(){
            laydate.render({
                elem: '.sub-date',
                theme: '#393D49',
                btns: ['confirm'],
                range: false
            });
        };

        addSubstitute.buildProgramList();
        addSubstitute.bindClick();
        addSubstitute.bindLayer();

    }
);
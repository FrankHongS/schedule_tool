$(
    function(){
        const substitute={};

        substitute.bindLayer=function(){
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

        substitute.bindClick=function(){
            $('.add-btn').on('click',function(){
                layer.open({
                    type: 2,
                    title: '添加替班信息',
                    area: ['480px', '400px'],
                    fix: false,
                    maxmin: false,
                    scrollbar: true,
                    content: '../addSubstitute/addSubstitute.html'
                });
            });

            $('.export-link').on('click', function () {

                const from = $('.from').val();
                const to = $('.to').val();
                const isHoliday=$('#hol:checked').length==1?true:false;

                if((from&&!to)||(!from&&to)){
                    alert('时间范围需同为空或同不为空... :)');
                    return;
                }

                if(from&&to){
                    $(this).attr('href', '/schedule/schedule_excel/substitute?isHoliday='+isHoliday+'&from=' + from + '&to=' + to);
                }else{
                    $(this).attr('href', '/schedule/schedule_excel/substitute?isHoliday='+isHoliday);
                }
            });

            $('.search-btn').on('click',()=>{
                const from=$('.from').val();
                const to=$('.to').val();
                const isHoliday=$('#hol:checked').length==1?true:false;

                if((from&&!to)||(!from&&to)){
                    alert('时间范围需同为空或同不为空... :)');
                    return;
                }

                if(from&&to){
                    $.ajax({
                        url:'/schedule/substitute/range?isHoliday='+isHoliday+'&from='+from+'&to='+to,
                        success:result=>{
                            if(result.code==0){
                                this.createTable('.tb-container .sub-body',this.parseData(result.data.substitute));
                            }else{
                                alert('检索失败 '+result.message);
                            }
                        }
                    });
                }else{
                    $.ajax({
                        url:'/schedule/substitute?isHoliday='+isHoliday,
                        success:result=>{
                            if(result.code==0){
                                console.log(result);
                                this.createTable('.tb-container .sub-body',this.parseData(result.data.substitute));
                            }else{
                                alert('检索失败 '+result.message);
                            }
                        }
                    });
                }

            });
        };

        substitute.parseData=function(dataArray){
            const list = [];

            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let itemList = [];

                itemList[0] = i + 1;
                itemList[1] = item.employeeName;
                itemList[2] = item.programName;
                itemList[3] = parseUTCTimeToYMD(item.substituteDate);
                itemList[4] = item.holiday==true?'是':'否';

                list[i] = itemList;
            }

            return list;
        };

        substitute.createTable=function (container, dataList) {
            $(container).html('');
            const cellsArray = dataList.map(
                rowValues => {
                    return rowValues.map(
                        (cells, index) => {
                            return $('<td>')
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

        substitute.bindLayer();
        substitute.bindClick();
    }
);
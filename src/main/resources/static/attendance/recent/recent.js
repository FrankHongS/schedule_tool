$(
    function(){
        const recent={};

        recent.bindLaydate=function(){
            laydate.render({
                elem: '.from',
                theme: '#393D49',
                btns: ['confirm'],
                type: 'datetime'
              });
              laydate.render({
                elem: '.to',
                theme: '#393D49',
                btns: ['confirm'],
                type: 'datetime'
              });
        };

        recent.bindClick=function(){
            $('.search-btn').on('click',function(){
                $.ajax({
                    url:'',
                    success:
                });

                $.ajax({
                    url:'',
                    success:
                });
            });
        };

        recent.createTable = function (container, dataList) {
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
                (row, index) => {
                    return $('<tr>')
                        .append(row);
                }
            );

            $(container).append(rowsArray);
        };

        recent.bindLaydate();
    }
);
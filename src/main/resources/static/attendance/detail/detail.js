$(
    function () {
        const detail = {};

        const leaveList=[
            [
                1,
                '2018-08-12 12:35:45',
                '2018-08-12 - 2018-08-12',
                '3.5',
                '有事需要请假天',
                '异常'
            ],
            [
                2,
                '2018-08-12 12:35:45',
                '2018-08-12 - 2018-08-12',
                '3.5',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                3,
                '2018-08-12 12:35:45',
                '2018-08-12 - 2018-08-12',
                '3.5',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                4,
                '2018-08-12 12:35:45',
                '2018-08-12 - 2018-08-12',
                '3.5',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ]
        ];
        const leaveClassGroup = ['index', 'created-time', 'time', 'length', 'comment','is-normal'];

        const lateList=[
            [
                1,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                2,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                3,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                4,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ]
        ];
        const lateClassGroup = ['index', 'created-time', 'time', 'comment','is-normal'];

        const homebaseList=[
            [
                1,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                2,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                3,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ],
            [
                4,
                '2018-08-12 12:35:45',
                '2018-08-12',
                '有事需要请假半天有事需要请假半天有事需要请假半天有事需要请假半天',
                '异常'
            ]
        ];
        const homebaseClassGroup = ['index', 'created-time', 'time', 'comment','is-normal'];
        
        detail.getParameter = function () {
            const a = decodeURIComponent($.Request('a'));
            if (a) {
                $("#title h1").text(a + '(v-shhong)  考勤情况')
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

        detail.bindLayer=function(){
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


        detail.buildUI = function () {
            detail.createTable('.leave-body',leaveList,leaveClassGroup);
            detail.createTable('.late-body',lateList,lateClassGroup);
            detail.createTable('.homebase-body',homebaseList,homebaseClassGroup)

            detail.bindLayer();
        };

        detail.getParameter();
        detail.buildUI();
    }
);
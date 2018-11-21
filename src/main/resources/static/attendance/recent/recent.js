$(
    function () {
        const recent = {};
        const page = 0;
        const size = 2;

        recent.bindClick = function () {
            $('.search-btn').on('click', e => {
                $.ajax({
                    url: '/schedule/leave/recent?page=' + page + '&size=' + size,
                    success: result => {
                        $('.leave-body').html('');
                        this.createTable('.leave-body', this.parseLeaveData(result.leave))
                    }
                });

                $.ajax({
                    url: '/schedule/late/recent?page=' + page + '&size=' + size,
                    success: result => {
                        $('.late-body').html('');
                        this.createTable('.late-body', this.parseLateData(result.late))
                    }
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

        recent.parseLeaveData = function (dataArray) {
            const list = [];

            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let itemList = [];

                let halfType = item.halfType;
                let desc = '';
                if (halfType == 0) {
                    desc = '';
                } else if (halfType == 1) {
                    desc = '  第一天为半天';
                } else if (halfType == 2) {
                    desc = '  最后一天为半天';
                } else if (halfType == 3) {
                    desc = '  第一天和最后一天为半天';
                }

                itemList[0] = i + 1;
                itemList[1] = item.name;
                itemList[2] = item.alias;
                itemList[3] = window.leaveTypeArray[item.leaveType + 1];
                itemList[4] = parseUTCTime(item.createdTime);
                itemList[5] = parseUTCTimeToYMD(item.from) + ' - ' + parseUTCTimeToYMD(item.to) + desc;
                itemList[6] = item.dayCount;
                itemList[7] = item.comment;

                list[i] = itemList;
            }

            return list;
        }

        recent.parseLateData = function (dataArray) {
            const list = [];

            for (let i = 0; i < dataArray.length; i++) {
                let item = dataArray[i];
                let itemList = [];

                itemList[0] = i + 1;
                itemList[1] = item.name;
                itemList[2] = item.alias;
                itemList[3] = window.lateTypeArray[item.lateType + 1];
                itemList[4] = parseUTCTime(item.createdTime);
                itemList[5] = parseUTCTimeToYMD(item.lateDate);
                itemList[6] = item.comment;

                list[i] = itemList;
            }

            return list;

        }

        recent.bindClick();
    }
);
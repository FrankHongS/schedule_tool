$(
    function () {
        const edit = {};

        edit.buildUI = function () {
            const that = this;
            laydate.render({
                elem: '.datepicker',
                theme: '#393D49',
                btns: ['confirm'],
                range: '-'
            });

            window.leaveTypeArray.splice(0, 1);
            that.buildTypeMenu(window.leaveTypeArray);
            that.buildOriginData();

        };

        edit.buildTypeMenu = function (dataArray) {
            $('select#item-type').html('');
            const rowsArray = dataArray.map(
                (row, index) => {
                    return $('<option>')
                        .attr('value', index)
                        .append(row);
                }
            )

            $('select#item-type').append(rowsArray);
        };

        edit.buildOriginData = function () {
            $('#item-type').val(window.parent.originDataLeave.leaveType);//设置select选中的值
            $('.name').val(window.parent.originDataLeave.name);
            $('.name').attr('readonly', 'readonly');
            $('.alias').val(window.parent.originDataLeave.alias);
            $('.alias').attr('readonly', 'readonly');
            $('.datepicker').val(window.parent.originDataLeave.leaveDateRange.split('  ')[0]);

            if (window.parent.originDataLeave.normal == 'true') {// normal返回的是一个字符串，不是boolean类型
                $("input[name='isNormal']").get(0).checked = true;
            } else {
                $("input[name='isNormal']").get(1).checked = true;
            }
            $('.comment-text').val(window.parent.originDataLeave.comment);

            const halfType = window.parent.originDataLeave.halfType;
            if (halfType == 1) {
                $('.previous').get(0).checked = true;
            } else if (halfType == 2) {
                $('.later').get(0).checked = true;
            } else if (halfType == 3) {
                $('.previous').get(0).checked = true;
                $('.later').get(0).checked = true;
            } else if (halfType == 0) {
                $('.previous').get(0).checked = false;
                $('.later').get(0).checked = false;
            }
        };

        edit.bindAjax = function () {
            $('.save').bind('click', function (e) {
                $('.message').text('正在更新...');

                const typeValue = $('#item-type').children('option:selected').val();
                let halfType = 0;
                if ($('.previous:checked').length + $('.later:checked').length == 2) {
                    halfType = 3;
                } else if ($('.previous:checked').length + $('.later:checked').length == 0) {
                    halfType = 0;
                } else if ($('.previous:checked').length == 1 && $('.later:checked').length == 0) {
                    halfType = 1;
                } else if ($('.previous:checked').length == 0 && $('.later:checked').length == 1) {
                    halfType = 2;
                }
                $.ajax({
                    url: '/schedule/leave/update',
                    type: 'POST',
                    data: {
                        id: window.parent.originDataLeave.leaveId,
                        leaveType: typeValue,
                        leaveDateRange: $('.datepicker').val(),
                        halfType: halfType,
                        dayCount: calculateDayCount($('.datepicker').val(),halfType),
                        isNormal: $('.choose input[name="isNormal"]:checked').val() === '1' ? true : false,
                        comment: $('.comment-text').val()
                    },
                    success: result => {
                        $('.message').text('更新成功');
                        console.log(result);
                    },
                    error: (xhr, e) => {
                        $('.message').text('更新失败...' + e);
                        console.log(e);
                    }
                });
            });

            $('.cancel').bind('click', () => {
                const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            });
        };

        edit.buildUI();
        edit.bindAjax();
    }
);
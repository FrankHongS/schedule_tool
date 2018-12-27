$(
    function () {

        let activeItem;

        $('.home-navigation')
            .bind('click', 'li', function (e) {
                const currItem = $(e.target);
                if (!currItem.hasClass('active')) {
                    currItem.addClass('active');
                    if (currItem.hasClass('selected')) {
                        currItem.removeClass('selected');
                    }
                    if (activeItem) {
                        activeItem.removeClass('active');
                    }
                    activeItem = currItem;
                }
                const index = currItem.index();
                if (index == 0) {
                    if (!$('#schedule').hasClass('loaded')) {
                        $('#schedule').load('../schedule02/main/main.html ', function () {
                            window.schedule_main();
                        });
                        $('#schedule').addClass('loaded');
                    }

                    $('#schedule').show()
                        .siblings().hide();

                } else if (index == 1) {
                    if (!$('#attendance').hasClass('loaded')) {
                        $('#attendance').load('../attendance/main/main.html ', function () {
                            window.attendance_main();
                        });
                        $('#attendance').addClass('loaded');
                    }
                    $('#attendance').show()
                        .siblings().hide();
                }
            })
            .on('mouseenter', 'li', function (e) {
                const currItem = $(e.target);
                if (!currItem.hasClass('active') && !currItem.hasClass('selected')) {
                    currItem.addClass('selected');
                }
            });

        $('.img-navigation').on('click', function (e) {
            if (!$(this).hasClass('nav-show')) {
                $('.home-navigation').animate({
                    left: 44 + 'px'
                }, 500);
                $(this).addClass('nav-show');
            } else {
                $('.home-navigation').animate({
                    left: -100 + 'px'
                }, 500);
                $(this).removeClass('nav-show');
            }
        });

        $('.home-navigation li:first-child').trigger('click');
    }
);
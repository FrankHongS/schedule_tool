$(
    function () {

        let activeItem;

        $('.home-navigation')
            .bind('click', 'li', function (e) {
                let currItem = $(e.target);
                if (!currItem.hasClass('selected')) {
                    currItem.addClass('selected');
                    if(currItem.hasClass('unselected')){
                        currItem.removeClass('unselected');
                    }
                    if (activeItem) {
                        activeItem.removeClass('selected');
                    }
                    activeItem = currItem;
                }
                const index=currItem.index();
                if(index==0){
                    if($('#attendance').hasClass('show')){
                        $('#attendance').removeClass('show')
                                        .addClass('hide');
                    }
                }else if(index==1){
                    if(!$('#attendance').hasClass('hide')){
                        $('#attendance').load('../attendance/main/main.html ',function(){
                            window.attendance_main();
                        });
                    }
                    $('#attendance').addClass('show')
                                    .removeClass('hide');
                }
            })
            .on('mouseenter','li',function(e){
                let currItem = $(e.target);
                if(!currItem.hasClass('selected')){
                    currItem.addClass('unselected');
                }
            });

            $('.img-navigation').on('click',function(e){
                if(!$(this).hasClass('nav-show')){
                    $('.home-navigation').animate({
                        left:44+'px'
                    },500);
                    $(this).addClass('nav-show');
                }else{
                    $('.home-navigation').animate({
                        left:-100+'px'
                    },500);
                    $(this).removeClass('nav-show');
                }
            });
    }
);
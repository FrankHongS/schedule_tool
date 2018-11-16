$(
    function(){
        $('#schedule').bind('click',function(){
            $(this).css({
                background:'#009688',
                color: 'aliceblue'
            });

            $('#attendance').css({
                background:'#ffffff',
                color: 'black'
            })
        });

        $('#attendance').bind('click',function(){
            $(this).css({
                background:'#009688',
                color: 'aliceblue'
            });

            $('#schedule').css({
                background:'#ffffff',
                color: 'black'
            })
        });
    }
);
$(
    function(){
        const a=decodeURIComponent($.Request('a'));
        if(a){
            $("#title h1").text(a+'(v-shhong)  考勤情况')
        }
    }
);
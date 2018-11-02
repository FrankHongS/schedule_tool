$(
    function(){
        $('.name').bind('click',function(e){
            console.log(e.target.innerHTML);
            // $(location).attr('href','../detail/detail.html?a='+e.target.innerHTML);//重定向跳转，在当前窗口打开新页面
            window.open('../detail/detail.html?a='+e.target.innerHTML);//跳转时打开新窗口
        });
    }
);
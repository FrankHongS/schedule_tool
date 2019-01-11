// window.exportProgress = function (currentLayer, totalLayer) {
//
//     layui.use('element', function () {
//         var element = layui.element;
//     });
//
//     var tvProcess = $('.progress-tv');
//     tvProcess.text(currentLayer + '/' + totalLayer);
//     var pre = parseInt(parseFloat(currentLayer / totalLayer) * 100) + '%';
//     console.log(currentLayer + " " + totalLayer + " " + pre);
//
//     var element = layui.element;
//     element.progress("progress", pre);
//
// }
$(
    function () {
        var layuiClient={};
        var progress={};
        var timeOutId;

        layuiClient.init=function () {
            layui.use('element', function () {
                var element = layui.element;
            });
        };

        // progress.init=function(){
        //     document.addEventListener("visibilitychange",function (evt) {
        //         console.log(document.hidden);
        //         if(document.hidden===false){
        //             progress.callNet();
        //         }
        //     })
        // };
        progress.callNet=function(){
            $.ajax({
                url:'/arrange/schedule/state',
                success:result=>{
                    if(result.code===0){
                        // exportProgress(result.data.data.currentNumber,result.data.data.totalNumber);
                        progress.success(result.data.data.currentNumber,result.data.data.totalNumber);
                    }
                    timeOutId= setTimeout(progress.callNet,500);
                }
            })
        };

        progress.success=function(current,total){
            if(total!==0&&current===total){
                clearTimeout(timeOutId);
            }
            var tvProcess = $('.progress-tv');
            tvProcess.text(current + '/' + total);
            var pre = parseInt(parseFloat(current / total) * 100) + '%';
            console.log(current + " " + total + " " + pre);

            var element = layui.element;
            element.progress("progress", pre);
        };


        layuiClient.init();
        progress.callNet();
    }
)
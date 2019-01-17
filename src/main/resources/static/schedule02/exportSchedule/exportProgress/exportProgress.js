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
        var element;
        layui.use('element',function () {
           element=layui.element;
        })
        var interval = setInterval(function () {
            console.log("call state!!!!");
            $.ajax({
                url:'/arrange/schedule/state',
                success:result=>{
                    if(result.code===0){
                        // exportProgress(result.data.data.currentNumber,result.data.data.totalNumber);
                        // progress.success(result.data.data.currentNumber,result.data.data.totalNumber);
                        // layui.use('element', function () {
                        //     var element = layui.element;
                            var total = result.data.data.totalNumber;
                            var current = result.data.data.currentNumber;

                            if(total!==0&&current===total){
                                clearInterval(interval);
                            }else if(total===0&&current===-1){
                                clearInterval(interval);
                            }
                            var tvProcess = $('.progress-tv');
                            tvProcess.text(current + '/' + total);
                            var pre = parseInt(parseFloat(current / total) * 100) + '%';
                            console.log(current + " " + total + " " + pre);
                            if(element!=null){
                                console.log("update progress!!!")
                                element.progress("schedule-process", pre);
                            }
                        // });
                    }
                }
            // timeOutId= setTimeout(progress.callNet,500);
        })},500);
    }
)

//         $.ajax({
//             url:'/arrange/schedule/state',
//             success:result=>{
//             if(result.code===0){
//             // exportProgress(result.data.data.currentNumber,result.data.data.totalNumber);
//             // progress.success(result.data.data.currentNumber,result.data.data.totalNumber);
//                 layui.use('element', function () {
//                     var element = layui.element;
//                     if(total!==0&&current===total){
//                         clearTimeout(timeOutId);
//                     }
//                     var tvProcess = $('.progress-tv');
//                     tvProcess.text(current + '/' + total);
//                     var pre = parseInt(parseFloat(current / total) * 100) + '%';
//                     console.log(current + " " + total + " " + pre);
//
//                     element.progress("schedule-process", pre);
//                 });
//             }
//             // timeOutId= setTimeout(progress.callNet,500);
//         }
//     })  }
// )

    //     var layuiClient={};
    //     var progress={};
    //     var timeOutId;
    //
    //     layuiClient.init=function () {
    //         layui.use('element', function () {
    //             var element = layui.element;
    //         });
    //     };
    //
    //     // progress.init=function(){
    //     //     document.addEventListener("visibilitychange",function (evt) {
    //     //         console.log(document.hidden);
    //     //         if(document.hidden===false){
    //     //             progress.callNet();
    //     //         }
    //     //     })
    //     // };
    //     progress.callNet=function(){
    //         $.ajax({
    //             url:'/arrange/schedule/state',
    //             success:result=>{
    //                 if(result.code===0){
    //                     // exportProgress(result.data.data.currentNumber,result.data.data.totalNumber);
    //                     progress.success(result.data.data.currentNumber,result.data.data.totalNumber);
    //                 }
    //                 timeOutId= setTimeout(progress.callNet,500);
    //             }
    //         })
    //     };
    //
    //     progress.success=function(current,total){
    //         layui.use('element', function () {
    //             var element = layui.element;
    //             if(total!==0&&current===total){
    //                 clearTimeout(timeOutId);
    //             }
    //             var tvProcess = $('.progress-tv');
    //             tvProcess.text(current + '/' + total);
    //             var pre = parseInt(parseFloat(current / total) * 100) + '%';
    //             console.log(current + " " + total + " " + pre);
    //
    //             element.progress("schedule-process", pre);
    //         });
    //     };
    //
    //
    //     // layuiClient.init();
    //     progress.callNet();
    // }
// )
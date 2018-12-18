window.parseUTCTime = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
};

window.parseUTCTimeToYMD = function (timeString) {
    const date = new Date(timeString);
    let month=date.getMonth()+1;
    if(month<10){
        month='0'+month;
    }

    let d=date.getDate();
    if(d<10){
        d='0'+d;
    }
    return date.getFullYear() + '-' + month + '-' + d;
};

window.calculateDayCount=function(smallStr,bigStr){
    const small=new Date(smallStr);
    const big=new Date(bigStr);

    const deltaTime=big.getTime()-small.getTime();

    return parseInt(deltaTime/(1000 * 60 * 60 * 24) + 1);
}

window.getNextDate=function(dateStr){
    const date=new Date(dateStr);

    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + (date.getDate()+1);
}

window.isDateInRange=function(from1Str,to1Str,from2Str,to2Str){
    const from1=new Date(from1Str);
    const from2=new Date(from2Str);

    const to1=new Date(to1Str);
    const to2=new Date(to2Str);

    return (from1.getTime()-from2.getTime()>=0)&&(to1.getTime()-to2.getTime()<=0)
}

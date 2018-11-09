window.calculateDayCount = function (datesString) {
    const dateArray = datesString.split(' - ');
    const small = new Date(dateArray[0]);
    const big = new Date(dateArray[1]);
    const days = big.getTime() - small.getTime();
    return parseInt(days / (1000 * 60 * 60 * 24) + 1);
}

window.parseUTCTime = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
}

window.parseUTCTimeToYMD = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
}

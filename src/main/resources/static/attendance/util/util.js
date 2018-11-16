window.calculateDayCount = function (datesString, halfType) {
    let halfCount;
    switch (halfType) {
        case 0:
            halfCount = 0;
            break;
        case 1:
            halfCount = -0.5;
            break;
        case 2:
            halfCount = -0.5;
            break;
        case 3:
            halfCount = -1;
            break;
        default:
            break;
    }
    const dateArray = datesString.split(' - ');
    const small = new Date(dateArray[0]);
    const big = new Date(dateArray[1]);
    const days = big.getTime() - small.getTime();
    return parseFloat(days / (1000 * 60 * 60 * 24) + 1 + halfCount);
}

window.parseUTCTime = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
}

window.parseUTCTimeToYMD = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
}

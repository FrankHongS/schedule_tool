window.parseUTCTime = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
}

window.parseUTCTimeToYMD = function (timeString) {
    const date = new Date(timeString);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
}

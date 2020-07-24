$(function () {
    let othersusername = getAllUrlParams()['username'];


    let othersFavorPageClass = new OthersFavorPageClass(othersusername);
    othersFavorPageClass.setCardTitle();
    othersFavorPageClass.search(1);


})
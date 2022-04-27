import {validerBruker, renderLogOutBtn, loggUt} from './validerbruker.js';

$(function(){
    validerBruker(function(){
        $("#admin-wrapper").css("display", "block");
        renderLogOutBtn();
        $("#loggut").click(loggUt);
    });
})




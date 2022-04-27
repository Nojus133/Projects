import {handleError} from "./index.js";

$(function(){
    $("#brukernavn, #passord").change(checkInputs);

    $("#login-btn").click(loggInn);

})

function checkInputs() {
    const btn = $("#login-btn");
    console.log($("#brukernavn").val());
    if ($("#brukernavn").val() != "" && $("#passord").val() != "") {
        btn.removeClass("disabled");
        $("#err").text("");
    }
    else btn.addClass("disabled");
}

function loggInn() {
    this.blur();
    const bruker = {
        brukernavn : $("#brukernavn").val(),
        passord : $("#passord").val(),
    }

    $.post("Bestilling/LoggInn", bruker, function(ok){
        if (ok) {
            window.location.href = "admin.html";
        }
        else {
            $("#err").text("Feil brukernavn eller passord");
        }
    })
    .fail(function(err){
        $("#err").text(handleError(err, "Det oppsto en feil på server - prøv igjen senere"));
    });
}

export function validerBruker(callback = function(){
    return;
}) {
    $.get("Bestilling/ValiderBruker", function(ok){
        callback();
        
    })
    .fail(()=>window.location.href = "logginn.html");
}

export function renderLogOutBtn() {
    const btn = document.createElement("button");
    btn.classList.add("btn");
    btn.id = "loggut";
    btn.textContent = "Logg ut";
    $("nav").append(btn);
}

export function loggUt() {
    $.get("Bestilling/LoggUt", ()=>window.location.href = "index.html")
    .fail(function(){

    });
}
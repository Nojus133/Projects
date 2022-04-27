$(function(){
    const valuesFromURL = getValuesFromURL();
    const {ruter : [...reiseTider], bestilling} = valuesFromURL;
    
    setInnDato(bestilling);
    settInnSteder(bestilling);

    $(".back").click(function(){
        window.history.back();
        this.blur();
    })

    reiseTider.forEach(e => byggBillettDiv(e, bestilling));

    $(".btn3").click(function(e){
        velgTid(e, bestilling, reiseTider);
    });
})

function getValuesFromURL() {
    const url = new URL(window.location.href);
    const ruter = JSON.parse(url.searchParams.get("ruter"));
    const bestilling = JSON.parse(url.searchParams.get("bestilling"));
    return {ruter, bestilling};
}

function setInnDato(date) {
    const text = "Reiser for "+date.reiseDato;
    $(".billetter-header h1").text(text);
}

function settInnSteder(steder) {
    const text = steder.fra+"  <i class='fas fa-long-arrow-alt-right'></i>  "+steder.til;
    $(".billetter-header h2").html(text);
}

function byggBillettDiv(reiseTider, date) {
    const {rute_Id, tId : {reiseTid, ukedag}} = reiseTider;
    const {reiseDato} = date;

    const div = document.createElement("div");
    div.classList.add("billettDiv");

    const id = document.createElement("input");
    id.setAttribute("type", "hidden");
    id.value = rute_Id;

    const dato = document.createElement("h2");
    dato.textContent = reiseDato;

    const $ukedag = document.createElement("h2");
    $ukedag.textContent = dagToString(ukedag);

    const tid = document.createElement("h2");
    tid.textContent = "kl "+reiseTid;

    const btn = document.createElement("button");
    btn.classList.add("btn3");
    btn.textContent = "Velg";

    div.appendChild(dato);
    div.appendChild($ukedag);
    div.appendChild(tid);
    div.appendChild(btn);
    div.appendChild(id);

    $("#billettValg").append(div);
}

function dagToString(ukedag) {
    const dager = ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    return dager[--ukedag];
}

function velgTid(e, bestilling, reiseTider){
    const id = Number(Array.from(e.currentTarget.parentElement.children).find(e=>e.localName === "input").value);
    const tid = reiseTider.find(obj => obj.rute_Id === id);
    bestilling.tid = tid;
    
    const bestillingJson = encodeURIComponent(JSON.stringify(bestilling));
    let url = "bestilling.html?bestilling="+bestillingJson;
    
    if (bestilling.harRetur) {
        const finnRute = {
            fra : bestilling.til,
            reiseDato : bestilling.returDato,
        }
        $.post("bestilling/FinnBillett", finnRute, function(e){
            console.log(e);
            const funnet = encodeURIComponent(JSON.stringify(e));
            url = "retur.html?bestilling="+bestillingJson+"&ruter="+funnet;
            window.location.href = url;
        })
        return;
    }
    
    window.location.href = url;
}
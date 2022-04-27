import {handleError} from "./index.js";

$(function(){
    const bestilling = getValuesFromURL();

    settInnSteder(bestilling);

    byggReiseDatoDiv(bestilling);
    byggReturDatoDiv(bestilling);
    byggAntallDiv(bestilling);

    $("#epost, #telefon").change(validerInputs);

    $(".back").click(function(){
        window.history.back();
        this.blur();
    })

    $.get("bestilling/GetPris", function(e){
        regnUtPris(e, bestilling)
    })
    .fail(function(err){
        $("#err").text(handleError(err, "Kunne ikke hente billett priser"));
    });

    $(".btn3").click(function(){
        const billettene = leggBilletterIArray(bestilling);
        const obj = {'':billettene};
        $.post("bestilling/LagreBillettene", obj, function(id){
            const url = "minebestillinger.html?id="+id;
            window.location.href = url;
        })
        .fail(function(err){
            $("#err2").text(handleError(err, "Kunne ikke hente bestillingen"));
        });
    })
})

function regnUtPris(allePriser, bestilling) {
    const antallObj = bestilling.antall
    let pris = 0;
    allePriser.forEach(e => {
        const type = e.type;
        const finnesIBestilling = type in antallObj;
        if (finnesIBestilling) {
            const antall = antallObj[type];
            for (let i = 0; i < antall; i++) {
                pris += e.pris;
            }
        }
    });
    if (bestilling.harRetur) {
        pris *= 2;
    }
    settInnPris(pris);
}

function settInnPris(pris) {
    const text = pris+" kr";
    $("#prisen").text(text);
}

function getAntall(bestilling) {
    const AntallBilletter = [];
    const antallObj = bestilling.antall;
    for (const [key, value] of Object.entries(antallObj)) {
        AntallBilletter.push({[key] : value});
    }
    
    return AntallBilletter;
}

function getValuesFromURL() {
    const url = new URL(window.location.href);
    const bestilling = JSON.parse(url.searchParams.get("bestilling"));
    return bestilling;
}

function settInnSteder(steder) {
    const text = steder.fra+"  <i class='fas fa-long-arrow-alt-right'></i>  "+steder.til;
    $(".bestilling-h2").html(text);
}

function byggReiseDatoDiv(bestilling) {
    const div = document.createElement("div");

    const header = document.createElement("p");
    header.textContent ="Reise dato:";

    const dato = document.createElement("p");
    dato.textContent = bestilling.reiseDato;

    const ukedag = document.createElement("p");
    ukedag.textContent = dagToString(bestilling.tid.tId.ukedag);

    const klokka = document.createElement("h2");
    klokka.textContent = "kl "+bestilling.tid.tId.reiseTid;

    div.appendChild(header);
    div.appendChild(dato);
    div.appendChild(ukedag);
    div.appendChild(klokka);

    $(".bestilling-detaljer").append(div);
}

function byggReturDatoDiv(bestilling) {
    if (bestilling.harRetur === false) return;
    const div = document.createElement("div");

    const header = document.createElement("p");
    header.textContent ="Retur dato:";

    const dato = document.createElement("p");
    dato.textContent = bestilling.returDato;

    const ukedag = document.createElement("p");
    ukedag.textContent = dagToString(bestilling.returTid.tId.ukedag);

    const klokka = document.createElement("h2");
    klokka.textContent = "kl "+bestilling.returTid.tId.reiseTid;

    div.appendChild(header);
    div.appendChild(dato);
    div.appendChild(ukedag);
    div.appendChild(klokka);

    $(".bestilling-detaljer").append(div);
}

function byggAntallDiv(bestilling) {
    const div = document.createElement("div");

    const header = document.createElement("p");
    header.textContent ="Antall:";
    div.appendChild(header);

    for (const [key, value] of Object.entries(bestilling.antall)) {
        const type = document.createElement("p");
        type.textContent = `${value} x ${key}`;
        div.appendChild(type);
    }

    $(".bestilling-detaljer").append(div);
}

function dagToString(ukedag) {
    const dager = ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    return dager[--ukedag];
}

function validerEpost(epost) {
    if (epost === "") return;
    const regex = /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    const ok = regex.test(epost);
    if (ok) {
        $("#feilEpost").text("");
        return ok;
    }
    $("#feilEpost").text("Skriv inn riktig epost adrresse");
    return ok;
}

function validerTelefon(tel) {
    if (tel === "") return;
    const regex = /^[0-9]{8}$/;
    const ok = regex.test(tel);
    if (ok) {
        $("#feilTelefon").text("");
        return ok;
    }
    $("#feilTelefon").text("Telefon nummer skal ha 8 siffer");
    return ok;
}

function validerInputs(){
    $(".btn3").addClass("disabled");
    const okEpost = validerEpost($("#epost").val());
    const okTelefon = validerTelefon($("#telefon").val());
    
    if (okTelefon && okEpost) {
        $(".btn3").removeClass("disabled");
    }
}

function leggBilletterIArray(bestilling) {
    const billetter = [];
    const prisString = $("#prisen").text();
    const pris = Number(prisString.slice(0, -3));

    const {bestillingsDato, 
           reiseDato, 
           returDato, 
           fra, 
           til, 
           harRetur, 
           tid : {rute_Id : rute},  
           antall} = bestilling;
    let ruteRetur;
    if (returDato) {
        ruteRetur = bestilling.returTid.rute_Id;
    }

    for (const [key, value] of Object.entries(antall)) {
        for (let i = 0; i < value; i++) {
            billetter.push({
                bestillingsDato : bestillingsDato,
                reiseDato : reiseDato,
                fra : fra,
                til : til,
                harRetur : harRetur,
                rute : rute,
                reisende : key,
                epost : $("#epost").val(),
                telefon : $("#telefon").val(),
                totalPris : pris
            });
            if (harRetur) {
                billetter.push({
                    bestillingsDato : bestillingsDato,
                    reiseDato : returDato,
                    fra : til,
                    til : fra,
                    harRetur : harRetur,
                    rute : ruteRetur,
                    reisende : key,
                    epost : $("#epost").val(),
                    telefon : $("#telefon").val()
                });
            }
        }
    }
    return billetter;
}
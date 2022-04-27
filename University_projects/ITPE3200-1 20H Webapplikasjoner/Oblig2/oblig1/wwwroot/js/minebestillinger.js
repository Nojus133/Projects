$(function(){
    const id = window.location.search.substring(1);
    const url = "bestilling/HentBilletter?"+id;

    $.get(url, function(billetter){
        formatterBilletter(billetter);
    })
})

function formatterBilletter(billetter){
    const table = document.createElement("table");
    table.classList.add("alleBilletterTable");

    const header = document.createElement("tr");
    header.innerHTML = 
    `<th>Billett Nr.</th>
     <th>Fra</th>
     <th>Til</th>
     <th>Dato</th>
     <th>Reise tid</th>
     <th>Reisende</th>
     <th>Pris</th>`;
    table.appendChild(header);

    billetter.forEach(b=> {
        const {
            billettNr, 
            fra:{stedNavn : fra}, 
            til:{stedNavn : til}, 
            reiseDato, 
            ruten:{ tId:{reiseTid : tid, ukedag : dagInt} },
            billettTyper:{type, pris}
        } = b;
        const dato = formatterReiseDato(reiseDato);
        const ukedag = dagToString(dagInt);

        const tr = document.createElement("tr");
        tr.innerHTML = 
        `<td>${billettNr}</td>
         <td>${fra}</td>
         <td>${til}</td>
         <td>${dato}<br>${ukedag}</td>
         <td>${tid}</td>
         <td>${type}</td>
         <td>${pris} kr</td>`;
        table.appendChild(tr);
    });
    $("#alleBilletterContainer").html("");
    $("#alleBilletterContainer").append(table);
}

function dagToString(ukedag) {
    const dager = ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    return dager[--ukedag];
}

function formatterReiseDato(dato) {
    const datoString = dato.slice(0, 10);
    const datoArray = datoString.split('-');
    return `${datoArray[2]}. ${datoArray[1]}, ${datoArray[0]}`;
}
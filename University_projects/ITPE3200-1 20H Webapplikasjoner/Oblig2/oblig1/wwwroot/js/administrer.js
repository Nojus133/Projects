import { handleError } from './index.js';
import {validerBruker, renderLogOutBtn, loggUt} from './validerbruker.js';

$(function(){
    $.ajaxSetup({
        contentType: "application/json"
     });

    validerBruker(byggHtml);

    $(".back").click(function(){
        window.history.back();
        this.blur();
    })
});

function byggHtml() {
    renderLogOutBtn();
    $("#loggut").click(loggUt);
    const urlParam = getUrlParam();
    const header = byggHeader(urlParam);
    $("section").append(header);
    byggTable(urlParam);
}

function getUrlParam() {return window.location.search.substring(1);}

function byggHeader(navn) {
    const header = document.createElement("header");
    header.classList.add("logginn-header");
    header.innerHTML = `<h1>Administrer ${navn}</h1>`;
    return header;
}

function byggTable(navn) {
    const url = byggUrl(navn);
    getData(url, formatterTable);
}

function formatterTable(data) {
    if (data.length === 0) {
        const p = document.createElement("p");
        p.textContent = "Tabellen er tom";
        p.style.color = "#424242";
        p.style.fontSize = "3rem";
        p.style.margin = "100px 0 100px 2vw";
        $("section").append(p);
        return;
    }
    const table = document.createElement("table");
    table.classList.add("admin-table");
    const type = getUrlParam();

    const header = document.createElement("tr");
    let ut = "";
    Object.keys(data[0]).forEach(key => ut+= `<th>${storBokstav(key)}</th>`);
    ut += `<th colspan='2' style='border-bottom: 2px solid #DBDBDB; text-align: right;'>`;
    if (type === "kunder" || type === "billetter" || type === "bestillinger") ut += `</th>`;
    else {
        ut += `<button class='btn4 btn-add' id='nyttRad'>+ Legg til nytt rad</button></th>`;
    }
    header.innerHTML = ut;
    table.appendChild(header);

    data.forEach(obj=>{
        const tr = document.createElement("tr");
        let utTd = "";
        for (const [,value] of Object.entries(obj)) {
            utTd += `<td><span>${value}</span></td>`; 
        }
        if (type !== "bestillinger" && type !== "billetter") {
            utTd += `<td><button class='btn4 btn-endre' id='endre'>Endre</button></td>`;
        }
        utTd += `<td><button class='btn4 btn-slett' id='slett'>Slett</button></td>`;
        tr.innerHTML = utTd;
        table.appendChild(tr);
    })

    const wrapper = document.createElement("div");
    wrapper.id = "admin-table-wrapper";
    wrapper.appendChild(table);
    $("section").append(wrapper);

    table.addEventListener("click", tableEventHandler);
}

function tableEventHandler(event) {
    const parent = event.target.parentElement.parentElement;
    if (event.target.id === "slett") {
        slettRad(parent);
    }
    if (event.target.id === "endre") {
        endreRad(parent);
    }
}

function endreRuterRad(row) {
    const antall = row.childElementCount;
    const antallSpan = antall -2;
    const spanValues = [];
    let forrigeBtnAdd;
    let forrigeBtnCancel;
    for (let i = 0; i < antall; i++) {
        const content = row.children[i];
        if (i < antallSpan) {
            spanValues.push(content.firstChild.textContent);
            if (i === 0) continue;
            const select = document.createElement("select");
            if (i === 1) {
                let ut = `<option value="" disabled selected>Velg strekning</option>`;
                $.get("Bestilling/HentStrekninger", function(data){
                    data.forEach(obj => {
                        if (spanValues[1] === obj.stedNavn) {
                            ut += `<option value="${obj.sId}" selected>${obj.stedNavn}</option>`;
                        } 
                        else {
                            ut += `<option value="${obj.sId}">${obj.stedNavn}</option>`;
                        }
                    })
                    select.innerHTML = ut;
                });
                select.addEventListener("change", function(){
                    const td = row.children[2];
                    td.innerHTML = "";
                    hentNesteSelect(spanValues, row);
                })
                content.firstChild.remove();
                content.appendChild(select);
                continue;
            }
            content.firstChild.remove();
        }
        else if (i === antallSpan) {
            const btnAdd = byggBtnAdd(function(){
                oppdaterRad(this, spanValues)
            });
            forrigeBtnAdd = content.firstChild;
            
            content.appendChild(btnAdd);
            content.removeChild(forrigeBtnAdd);
        }
        else {
            forrigeBtnCancel = content.firstChild;
            const btnCancel = byggBtnCancel(function(){
                cancelEndreRad(row, antall, antallSpan, spanValues, forrigeBtnAdd, forrigeBtnCancel);
            });
            content.appendChild(btnCancel);
            content.removeChild(forrigeBtnCancel);
        }
    }
    hentNesteSelect(spanValues, row);
       
}

function hentNesteSelect(spanValues, row) {
    const url = `Bestilling/HentTidsplaner`;
    $.get(url, function(data){
        const td = row.children[3];
        td.innerHTML = "";
        const select = document.createElement("select");
        let ut = `<option value="" disabled selected>Velg ukedag</option>`;
        const ukedag = data.map(obj => obj.ukedag)
        .filter((value, index, self) => self.indexOf(value) === index)
        .sort((a, b) => a - b)
        .map(dag => formatterUkedag(dag));
        ukedag.forEach(dag => {
            if (spanValues[3] === dag) {
                ut += `<option value="${dag}" selected>${dag}</option>`;
            }
            else {
                ut += `<option value="${dag}">${dag}</option>`;
            }  
        })
        select.innerHTML = ut;
        let val = toUkedagInt(select.options[select.selectedIndex].text);
        select.addEventListener("change", function(){
            val = toUkedagInt(this.options[this.selectedIndex].text);
            hentSisteSelect(val, data, row, spanValues);
        })
        td.appendChild(select);
        hentSisteSelect(val, data, row, spanValues);
    })
}

function hentSisteSelect(val, data, row, spanValues) {
    const td = row.children[2];
    td.innerHTML = "";
    const select = document.createElement("select");
    let ut = `<option value="" disabled selected>Velg tid</option>`;
    const tider = data.filter(obj => obj.ukedag === val)
    .sort((a, b) => a.reiseTid.localeCompare(b.reiseTid));
    tider.forEach(obj => {
        if (spanValues[2] === obj.reiseTid) {
            ut += `<option value="${obj.tId}" selected>${obj.reiseTid}</option>`;
        }
        else {
            ut += `<option value="${obj.tId}">${obj.reiseTid}</option>`;
        }
    })
    select.innerHTML = ut;
    td.appendChild(select);
}

function endreRad(row) {
    const antall = row.childElementCount;
    const antallSpan = antall -2;
    const spanValues = [];
    let forrigeBtnAdd;
    let forrigeBtnCancel;
    
    if (getUrlParam() === "ruter") {
        endreRuterRad(row);
        return;
    }
    for (let i = 0; i < antall; i++) {
        const content = row.children[i];
        if (i < antallSpan) {
            spanValues.push(content.firstChild.textContent);
            if (i === 0) continue;
            const input = `<input class='table-input' type='text' value='${content.textContent}'>`;
            content.firstChild.outerHTML = input;
        }
        else if (i === antallSpan) {
            const btnAdd = byggBtnAdd(function(){
                oppdaterRad(this, spanValues)
            });
            forrigeBtnAdd = content.firstChild;
            
            content.appendChild(btnAdd);
            content.removeChild(forrigeBtnAdd);
        }
        else {
            forrigeBtnCancel = content.firstChild;
            const btnCancel = byggBtnCancel(function(){
                cancelEndreRad(row, antall, antallSpan, spanValues, forrigeBtnAdd, forrigeBtnCancel);
            });
            content.appendChild(btnCancel);
            content.removeChild(forrigeBtnCancel);
        }
    }
}

function oppdaterRad(that, spanValues) {
    let input = 'input';
    if (getUrlParam() === "ruter") input = 'select';
    const inputValues = Array.from(that.parentElement.parentElement.children)
    .filter(child => child.firstChild.localName === input)
    .map(td => {
        if (input === 'select') {
            const select = td.firstChild;
            return select.options[select.selectedIndex].value;
        } 
        else {
            return td.firstChild.value;
        }
    });
    inputValues.splice(0, 0, that.parentElement.parentElement.children[0].firstChild.textContent);
    const [url, obj] = lagUrlForOppdaterRad(inputValues, spanValues, getUrlParam());
    $.get(url, obj, function(){
        window.location.href = "administrer.html?"+getUrlParam();
    })
    .fail(function(err){
        alert(handleError(err, "Verdiene allerede finnes i databasen"));
    });
}

function lagUrlForOppdaterRad(values, spanValues, type) {
    let url = "Bestilling/Endre";
    let obj = {};
    switch (type) {
        case "priser":
            url += "Type?id="+spanValues[0];
            obj.PId = values[0];
            obj.Pris = values[2];
            obj.Type =values[1].toLowerCase();
            break;
        case "strekninger":
            url += "Strekning";
            obj.SId = values[0];
            obj.StedNavn = storBokstav(values[1].toLowerCase());
            break;
        case "kunder":
            url += "Kunde";
            obj.KId = values[0];
            obj.Epost = values[1];
            obj.Telefon = values[2];
            break;
        case "billetter":
            url += "Billett?id="+value[1];
            break;
        case "bestillinger":
            url += "Bestilling?id="+value[0];
            break;
        case "ruter":
            url += "Rute?";
            obj = "id="+values[0]+"&id_sted="+values[1]+"&id_tid="+values[2];
            
            break;   
        case "tidsplaner":
            if (validerKlFormat(values[1]) && toUkedagInt(values[2]) !== 0) {
                obj.TId = values[0];
                obj.ReiseTid = values[1];
                obj.Ukedag = toUkedagInt(values[2]);
                url += "Tidsplan";
            }
            break;      
        default:
            url = "";
    }
    return [url, obj];
}

function cancelEndreRad(row, antall, antallSpan, value, forrigeBtnAdd, forrigeBtnCancel) {
    for (let i = 0; i < antall; i++) {
        console.log(value);
        const content = row.children[i];
        if (i < antallSpan) {
            if (i === 0) continue;
            const j = i - 1;
            const span = `<span>${value[i]}</span>`;
            content.firstChild.outerHTML = span;
        }
        else if (i === antallSpan) {
            const btnAdd = content.firstChild;
            content.removeChild(btnAdd);
            content.appendChild(forrigeBtnAdd);
        }
        else {
            const btnCancel = content.firstChild;
            content.removeChild(btnCancel);
            content.appendChild(forrigeBtnCancel);
        }
    }
}

function slettRad(row) {
    const antall = row.childElementCount -2;
    
    const type = getUrlParam();
    const values = [];
    for (let i = 0; i < antall; i++) {
        const val = row.children[i].firstChild.textContent;
        values.push(val);
    }
    console.log(values);
    const url = createURLForSlettRad(values, type);
    $.get(url, function(){
        window.location.href = "administrer.html?"+getUrlParam();
    })
}

function createURLForSlettRad(value, type) {
    let url = "Bestilling/Slett";
    switch (type) {
        case "priser":
            url += "Type?id="+value[0];
            break;
        case "strekninger":
            url += "Strekning?id="+value[0];
            break;
        case "kunder":
            url += "Kunde?id="+value[0];
            break;
        case "billetter":
            url += "Billett?id="+value[1];
            break;
        case "bestillinger":
            url += "Bestilling?id="+value[0];
            break;
        case "ruter":
                url += "Rute?id="+value[0];
                break;   
        case "tidsplaner":
            url += "Tidsplan?id="+value[0];
            break;      
        default:
            url = "";
    }
    return url;
}

function storBokstav(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function getData(url, callback) {
    $.get(url, function(data){
        const formattertData = formatterData(data);
        callback(formattertData);
        $("#nyttRad").click(() => leggeNyttRad(data));
    }).fail(function(){
        
    });
}

function leggeRuterRad(rad, table, antall) {
    let cell;
    for (let i = 0; i < antall; i++) {
        cell = rad.insertCell(i);
        if (i < antall) {
            if (i === 0){
                cell.innerHTML = `<span></span>`;
                continue;
            } 
            const select = document.createElement("select");
            if (i === 1) {
                let ut = `<option value="" disabled selected>Velg strekning</option>`;
                $.get("Bestilling/HentStrekninger", function(data){
                    data.forEach(obj => {
                        ut += `<option value="${obj.sId}">${obj.stedNavn}</option>`;
                    })
                    select.innerHTML = ut;
                });
                select.addEventListener("change", function(){
                    const td = cell;
                    td.innerHTML = "";
                    leggeNesteSelect(cell, rad);
                })
                
                cell.appendChild(select);
                continue;
            }
        }
    }
    leggeNesteSelect(cell, rad);
    
    const add = rad.insertCell();
    const btnAdd = byggBtnAdd(addRad);
    add.appendChild(btnAdd);

    const cancel =rad.insertCell();
    const btnClose = byggBtnCancel(()=>{
        document.querySelector("table").deleteRow(1);
    });
    cancel.appendChild(btnClose);
}

function leggeNesteSelect(cell, rad) {
    const url = `Bestilling/HentTidsplaner`;
    $.get(url, function(data){
        const td = cell;
        td.innerHTML = "";
        const select = document.createElement("select");
        let ut = `<option value="" disabled selected>Velg ukedag</option>`;
        const ukedag = data.map(obj => obj.ukedag)
        .filter((value, index, self) => self.indexOf(value) === index)
        .sort((a, b) => a - b)
        .map(dag => formatterUkedag(dag));
        ukedag.forEach(dag => {
            ut += `<option value="${dag}">${dag}</option>`; 
        })
        select.innerHTML = ut;
        select.addEventListener("change", function(){
            const val = toUkedagInt(this.options[this.selectedIndex].text);
            leggeSisteSelect(rad, data, val);
        })
        td.appendChild(select);
        
        leggeSisteSelect(rad, data, 1);
    })
}

function leggeSisteSelect(rad, data, val) {

    const td = rad.children[2];
    td.innerHTML = "";
    // td.insertCell(2);
    const select = document.createElement("select");
    let ut = `<option value="" disabled selected>Velg tid</option>`;
    const tider = data.filter(obj => obj.ukedag === val)
    .sort((a, b) => a.reiseTid.localeCompare(b.reiseTid));
    tider.forEach(obj => {
        ut += `<option value="${obj.tId}">${obj.reiseTid}</option>`;
    })
    select.innerHTML = ut;
    td.appendChild(select);
}

function leggeNyttRad(data) {
    const table = document.querySelector("table");
    if (table.children[1].id === "nyttRadInput") return;
    
    const antallRader = table.children[1].childElementCount - 2;
    
    const rad = table.insertRow(1);
    rad.id = "nyttRadInput";
    const type = getUrlParam();
    if (type === "ruter") {
        leggeRuterRad(rad, table, antallRader);
        return;
    }
    for (let i = 0; i < antallRader; i++) {
        const cell = rad.insertCell(i);
        if (type === "tidsplaner" || type === "priser" || type === "strekninger") {
            if (i !== 0) {
                cell.innerHTML = `<input class='table-input' type='text'>`;
            }
            else {
                cell.innerHTML = `<span></span>`;
            }
        } else {
            cell.innerHTML = `<input class='table-input' type='text'>`;
        }
        
    }
    const add = rad.insertCell();
    const btnAdd = byggBtnAdd(addRad);
    add.appendChild(btnAdd);

    const cancel =rad.insertCell();
    const btnClose = byggBtnCancel(()=>{
        document.querySelector("table").deleteRow(1);
    });
    cancel.appendChild(btnClose);

    table.children[1].children[0].firstChild.focus();
}

function byggBtnAdd(eventHandler) {
    const btnAdd = document.createElement("button");
    btnAdd.classList.add("btn-icon");
    btnAdd.id = "add";
    btnAdd.innerHTML = `<i class='fas fa-check'></i>`;
    btnAdd.addEventListener("click", eventHandler);
    return btnAdd;
}

function byggBtnCancel(eventHandler) {
    const btnClose = document.createElement("button");
    btnClose.classList.add("btn-icon");
    btnClose.id = "cancel";
    btnClose.innerHTML = `<i class='fas fa-times'></i>`;
    btnClose.addEventListener("click", eventHandler);
    return btnClose;
}

function addRad() {
    let input = "input";
    const type = getUrlParam();
    if (type === "ruter") {
        input = "select";
    }
    const rowValues = Array.from(document.querySelector("table").children[1].children)
    .filter(e => e.children[0].localName === input)
    .map(e => e.firstChild.value);
    if (rowValues.some(val => val === "")) return;
    
    const [url, obj] = createObjectAndUrl(type, rowValues);
    
    $.get(url, obj, function(){
        window.location.href = "administrer.html?"+getUrlParam();
    })
    .fail(function(err){
        alert(handleError(err, err.responseText));
    });
}



function createObjectAndUrl(type, values) {
    let obj = {};
    let url = "Bestilling/Lagre";
    console.log(values);
    switch (type) {
        case "priser":
            obj.Pris = values[1];
            obj.Type = values[0].toLowerCase();
            url += "Type";
            break;
        case "strekninger":
            obj.StedNavn = storBokstav(values[0].toLowerCase());
            url += "Strekning";
            break;
        case "kunder": break;
        case "billetter": break;
        case "bestillinger": break;
        case "ruter":
            url += "Rute?";
            obj = "id_sted="+values[0]+"&id_tid="+values[1];
            break;   
        case "tidsplaner":
            if (validerKlFormat(values[0]) && toUkedagInt(values[1]) !== 0) {
                obj.ReiseTid = values[0];
                obj.Ukedag = toUkedagInt(values[1]);
                url += "Tidsplan";
            }
            
            break;      
        default:
            url = "";
    }
    return [url, obj];
}

function validerKlFormat(string) {
    const regex = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
    const ok = regex.test(string);
    return ok;
}

function formatterData(data) {
    
    const type = getUrlParam();
    return data.map(obj => reformattereArray(obj, type));
}

function reformattereArray(obj, type) {
    if (type === "priser" || type === "kunder") return obj;
    if (type === "strekninger") {
        let rObj = {};
        rObj.Id = obj.sId;
        rObj.Sted = obj.stedNavn;
        return rObj;
    }
    if (type === "bestillinger") {
        let rObj = {};
        rObj.Id = obj.bId;
        rObj["Bestillings Dato"] = formatterDato(obj.bestillingsDato);
        rObj.Kunde = obj.kunder.epost;
        rObj.Pris = obj.totaltPris+" kr";
        return rObj;
    }
    if (type === "billetter") {
        let rObj = {};
        rObj["Bestillings Id"] = obj.bestillinger.bId;
        rObj["Billett Id"] = obj.billettNr;
        rObj.Fra = obj.fra.stedNavn;
        rObj.Til = obj.til.stedNavn;
        rObj.Type = obj.billettTyper.type;
        rObj["Reise dato"] = formatterDato(obj.reiseDato);
        rObj["Avgangs tid"] = obj.ruten.tId.reiseTid;
        rObj["Rute Id"] = obj.ruten.rute_Id;
        return rObj;
    }
    if (type === "ruter") {
        let rObj = {};
        rObj["Rute Id"] = obj.rute_Id;
        rObj.Sted = obj.stedNavn.stedNavn;
        rObj["Avgangs tid"] = obj.tId.reiseTid;
        rObj.Ukedag = formatterUkedag(obj.tId.ukedag);
        return rObj;
    }
    if (type === "tidsplaner") {
        let rObj = {};
        rObj.Id = obj.tId;
        rObj["Avgangs tid"] = obj.reiseTid;
        rObj.Ukedag = formatterUkedag(obj.ukedag);
        return rObj;
    }
}

function formatterUkedag(dag) {
    const liste = ["Mandag", "Tirsag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    return liste[--dag]; 
}

function toUkedagInt(string) {
    const liste = ["mandag", "tirsag", "onsdag", "torsdag", "fredag", "lørdag", "søndag"];
    return liste.findIndex(dag => dag === string.toLowerCase()) + 1;
}

function formatterDato(dato) { return dato.slice(0, -9); }

function byggUrl(id) {
    let url = "Bestilling/";
    switch (id) {
        case "priser":
            url += "GetPris";
            break;
        case "strekninger":
            url += "HentStrekninger";
            break;
        case "kunder":
            url += "HentKunder";
            break;
        case "billetter":
            url += "HentAlleBilletter";
            break;
        case "bestillinger":
            url += "HentBestillinger";
            break;
        case "ruter":
                url += "HentRuter";
                break;   
        case "tidsplaner":
            url += "HentTidsplaner";
            break;      
        default:
            url = "";
    }
    return url;
}


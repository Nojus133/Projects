function kjøpBillett() {
    const film = $('#film');
    const inputList = $('input');
    const spanArray = $('span');

    const filmFeilmelding = "Må velge film";
    const antallFeilmelding = "Må skrive noe inn i antall";
    const fornavnFeilmelding = "Må skrive noe inn i fornavnet";
    const etternavnFeilmelding = "Må skrive noe inn i etternavnet";
    const telnrFeilmelding = "Må skrive noe inn i telefonnummer";
    const epostFeilmelding = "Må skrive noe inn i epost";
    const feilmeldinger = [filmFeilmelding, antallFeilmelding, fornavnFeilmelding, etternavnFeilmelding, telnrFeilmelding, epostFeilmelding];

    Array.from(spanArray).forEach(function (span) {
        span.innerText = "";
    });

    const inputArray = Array.from(inputList).map(input => input.value);
    inputArray.unshift(film.val());

    let teller = 0;
    inputArray.forEach(function (input, i) {
        const j = i - 1;
        if (i !== 0) {
            inputList[j].classList.remove("is-invalid");
            inputList[j].classList.remove("is-valid");
        }
        if (input === "" || input === null) {
            spanArray[i].innerText = feilmeldinger[i];
            teller++;
            if (input === "") {
                inputList[j].classList.add("is-invalid");
            }
        } else {
            if (i !== 0) {
                inputList[j].classList.add("is-valid");
            }
        }
    });

    if (teller === 0) {
        const billett = {
            film: inputArray[0],
            antall: inputArray[1],
            fornavn: inputArray[2],
            etternavn: inputArray[3],
            telnr: inputArray[4],
            epost: inputArray[5],
        };

        $.post("/lagreData", billett, function () {
            hentData();
        });

        film.val("");
        Array.from(inputList).forEach(function (input) {
            input.value = "";
            input.classList.remove("is-valid");
        })
    }
}

function hentData() {
    $.get("/hentData", function (data) {
        formatterBillettene(data);
    })
}

function slettData() {
    $.post("/slettData", function () {
        ut = "";
        $('#filmer').html(ut);
    })
}

function formatterBillettene(BillettArray) {
    let ut = "";
    ut += `<h2 class="mb-3">Alle Billetter</h2>`;
    ut += `<div class="table-responsive"><table class="table table-striped border rounded"><thead><tr>`;
    ut += `<th>Film</th>
           <th>Antall</th>
           <th>Fornavn</th>
           <th>Etternavn</th>
           <th>Mobilnummer</th>
           <th>Epost</th></tr></thead>`;
    BillettArray.forEach(function (b) {
        ut += `<tr><td>${b.film}</td>
               <td>${b.antall}</td>
               <td>${b.fornavn}</td>
               <td>${b.etternavn}</td>
               <td>${b.telnr}</td>
               <td>${b.epost}</td></tr>`;
    });
    ut += `</table></div>`;
    ut += `<button class="btn btn-outline-primary" onclick='slettData()'>Slett alle billettene</button>`;
    $('#filmer').html(ut);
}

function visAlleBilletter() {
    $.get("/hentData", function (data) {
        if (data.length !== 0) {
            formatterBillettene(data);
        }
    })
}

visAlleBilletter();
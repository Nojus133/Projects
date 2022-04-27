const søkeStrekning = $("#fra, #til");
const byerDiv = $(".valgene");
const retur = $("input[name='retur']");
const returDato = $("#returDato");
const billettTyper =$(".en-type");
let index = 0;
let forrige;


$(function(){
    $("#dato-en").val(formatterDato(new Date()));
    byggKalendar(new Date(), 0, 0);
    const steder = hentSteder();
    $("#envei").prop("checked", true);
    $("#voksen").val(1);
    $("#barn").val(0);
    $("#student").val(0);

    Array.from(søkeStrekning).forEach(e => {
        e.addEventListener("keyup", function(e){
            compareInput(this, steder);
        })
    });
    
    Array.from(søkeStrekning).forEach(e => {
        e.addEventListener("keydown", arrow)
    });
    
    Array.from(søkeStrekning).forEach(e => {
        e.addEventListener("focus", function(){
            const height = $("nav").outerHeight() + $("header").outerHeight(true);
            if (window.scrollY < 160) {
                window.scroll({
                    top: height,
                    left: 0,
                    behavior: 'smooth'
                });
            }
            index = 0;
            compareInput(this, steder);
        })
    });

    $(".switch").click(function(){
        const val1 = søkeStrekning[0].value;
        const val2 = søkeStrekning[1].value;
        søkeStrekning[0].value = val2;
        søkeStrekning[1].value = val1;
        this.blur();
    })

    Array.from(retur).forEach(e => {
        e.addEventListener("click", function(){
            if (this.value === "envei") {
                returDato[0].style.display = "none";
            } 
            else {
                returDato[0].style.display = "block";
                $("#dato-to").val($("#dato-en").val());
                byggKalendar(new Date(), 1, 0);
            }
        });
    })

    let teller = 1;
    Array.from(billettTyper).forEach(e => {
        e.addEventListener("click", function(e){
            let input = this.children[1];
            
            if (e.target.type === "button") {
                if (e.target.textContent === "-") {
                    if (teller === 10 && input.value === "0") {
                        return;
                    }
                    if (teller > 0 && input.value > 0) {
                        const inputVal = --input.value;
                        input.value = inputVal;
                        teller--;
                    } 
                }
                if (e.target.textContent === "+") {
                    if (teller < 10 ) {
                        const inputVal = ++input.value;
                        input.value = inputVal;
                        teller++;
                    }  
                }
            }
            
            if (input.value > 0) input.classList.add("active");
            else input.classList.remove("active");
        })
    })


    const kal = $(".kalendar");
    const kalendarInput = Array.from($("#dato-en, #dato-to"));
    kalendarInput.forEach(e=> {
        e.addEventListener("focusin", function(e){
            if (e.target.id === "dato-en") kal[0].style.display = "block";
            if (e.target.id === "dato-to") kal[1].style.display = "block";
        });
    })
    
    kalendarInput.forEach(e=> {
        e.addEventListener("focusout", function(e){
            if (e.target.id === "dato-en") {  
                if (e.relatedTarget != null && e.relatedTarget.className === "kalendar") kalendarInput[0].focus();
                else if (e.relatedTarget != null && e.relatedTarget.id != "kalendar") kal[0].style.display = "none";
                else if (e.relatedTarget === null) kal[0].style.display = "none";
            }
    
            if (e.target.id === "dato-to") {  
                if (e.relatedTarget != null && e.relatedTarget.className === "kalendar") kalendarInput[1].focus();
                else if (e.relatedTarget != null && e.relatedTarget.id != "kalendar") kal[1].style.display = "none";
                else if (e.relatedTarget === null) kal[1].style.display = "none";
            }
        })
    })

    Array.from(søkeStrekning).forEach(e=>{
        e.addEventListener("focusout", function(){
            validerStrekninger(steder);
        });
    })
})


function hentSteder() {
    const stederArray = [];
    $.get("bestilling/HentSteder", function (steder) {
        // formatterSteder(steder);
        steder.forEach(s => {
            stederArray.push(s.stedNavn);
        });
        stederArray.sort();
    });
    return stederArray;
}


function formatterSteder(input, steder) {
    let ut = "<ul class='byer'>";
    steder.forEach(s => {
        const funnet = s.toUpperCase().includes(input);
        const compare = input.localeCompare(s.toUpperCase());
        if (compare === 0) {
            byerDiv.html("");
            return;
        }
        if (funnet) ut += "<li>"+s+"</li>";
    })
    ut += "</ul>";
    return ut;
}

function compareInput(inn, steder) {
    let n;
    if (inn.id === "fra") n = 0;
    else n = 1;
    
    const input = inn.value.toUpperCase();

    if (inn.id === "fra") {
        byerDiv[1].innerHTML = "";
        byerDiv[0].innerHTML = formatterSteder(input, steder);
    }
    else {
        byerDiv[0].innerHTML = "";
        byerDiv[1].innerHTML = formatterSteder(input, steder);
    }
        
    if ($.contains($(".byer").get(0), $("li").get(0))) {
        const li = document.querySelectorAll(".byer li");
        if (li.length<=index) {
            index = 0;
        } 
            
        li[index].classList.add("active2");

        window.addEventListener("keyup", function(e){
                if (e.keyCode === 40) {
                    for (let i = 0; i < li.length; i++) {
                        const containsClass = li[i].classList.contains("active2");
                        const divHeight = $(".valgene");
                        
                        if (containsClass) {
                            if (i === li.length-1) {
                                li[i].classList.remove("active2");
                                li[0].classList.add("active2");
                                divHeight[n].scrollTop = 0;
                                index = 0;
                                break;
                            }
                            else {
                                li[i].classList.remove("active2");
                                li[++i].classList.add("active2");

                                const activePosision = $(".active2");
                                
                                if (activePosision[0].offsetTop > 280) {
                                    divHeight[n].scrollTop = divHeight[n].scrollTop + activePosision[0].offsetHeight;
                                }

                                index = i;
                                break;
                            }
                        }
                    }
                }
                if (e.keyCode === 38) {
                    for (let i = 0; i < li.length; i++) {
                        const containsClass = li[i].classList.contains("active2");
                        const divHeight = $(".valgene");
                        if (containsClass) {
                            if (i === 0) {
                                li[i].classList.remove("active2");
                                li[li.length-1].classList.add("active2");
                                const activePosision = $(".active2");
                                console.dir(activePosision);
                                divHeight[n].scrollTop = activePosision[0].offsetTop;
                                index = li.length-1;
                                break;
                            }
                            else {
                                li[i].classList.remove("active2");
                                li[--i].classList.add("active2");

                                const activePosision = $(".active2");
                                
                                if (activePosision[0].offsetTop > 20) {
                                    divHeight[n].scrollTop = divHeight[n].scrollTop - activePosision[0].offsetHeight;
                                }

                                index = i;
                                break;
                            }
                            
                        }
                    }
                }

                if (e.keyCode === 13) {
                    for (let i = 0; i < li.length; i++) {
                        const containsClass = li[i].classList.contains("active2");
                        if (containsClass) {
                            inn.value = li[i].textContent;
                            inn.blur();
                        }
                    }
                }
            }, {once:true});
            
            const ul = document.querySelector(".byer");
            ul.addEventListener("mousedown", function(e){
                inn.value= e.target.textContent;
            })
            
    }          
}

function arrow(e) {
    if (e.keyCode === 38) {
        e.preventDefault();
    }
}

function getInputDate(nr) {
    const monthNames = ["Januar", "Februar", "Mars", "April", "May", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
    const input = $("#dato-en, #dato-to").get(nr);
    const inputString = input.value.replace( /[.,]/g, "" );
    const inputArray = inputString.split(' ');
    const n = monthNames.findIndex(e => e === inputArray[1]);
    inputArray[1] = n;
    return inputArray;
}

function byggKalendar(newDate, nr, n) {
    let monthIndex = n;
    const arrayIndex = nr;
    const date = newDate;
    const monthNames = ["Januar", "Februar", "Mars", "April", "May", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
    
    const inputArray = getInputDate(nr);
    const inputDate = new Date(Number(inputArray[2]), inputArray[1], Number(inputArray[0]));
    const currentDate = new Date();
    
    let month = date.getMonth();
    const kalendarDiv = $(".kalendar");
    kalendarDiv[arrayIndex].innerHTML = "";

    //Få måned navn
    const yearMonthDiv = document.createElement("div");
    yearMonthDiv.classList.add("year");
    yearMonthDiv.textContent = monthNames[month]+", "+String(date.getFullYear());
    
    if (monthIndex != 0) {
        const forrigeM = document.createElement("div");
        forrigeM.classList.add("byttMaaned");
        forrigeM.classList.add("forrige");
        const img = document.createElement("img");
        img.src = "/next.svg";
        forrigeM.appendChild(img);
        forrigeM.addEventListener("click", function(){
            const nesteDato = date.getMonth() - 1;
            monthIndex--;
            byggKalendar(new Date(date.getFullYear(), nesteDato, 1), nr, monthIndex);
        });
        yearMonthDiv.appendChild(forrigeM);
    }

    if (monthIndex < 2) {
        const nesteM = document.createElement("div");
        nesteM.classList.add("byttMaaned");
        nesteM.classList.add("neste");
        const img = document.createElement("img");
        img.src = "/next.svg";
        nesteM.appendChild(img);
        
        yearMonthDiv.appendChild(nesteM);
        nesteM.addEventListener("click", function(){
            const nesteDato = 1 + date.getMonth();
            monthIndex++;
            byggKalendar(new Date(date.getFullYear(), nesteDato, 1), nr, monthIndex);
        });
    }
    
    kalendarDiv[arrayIndex].appendChild(yearMonthDiv);
    
    
    
    //Få ukedager
    const ukeDager = document.createElement("div");
    ukeDager.classList.add("dag-navn");
    const dagArray = ["Man", "Tir", "Ons", "Tor", "Fre", "Lør", "Søn"];
    dagArray.forEach(d => {
        const dagDiv = document.createElement("div");
        const span = document.createElement("span");
        span.textContent = d;
        dagDiv.appendChild(span);
        ukeDager.appendChild(dagDiv);
    });
    kalendarDiv[arrayIndex].appendChild(ukeDager);
    
    //Få alle dager
    const dagerDiv = document.createElement("div");
    dagerDiv.classList.add("dager");
    //Få antall dager på en måned
    let nMonth = date.getMonth();
    const daysInMonth = new Date(date.getFullYear(), ++nMonth, 0);
    date.setDate(1);

    let currentDay;
    for (let i = 1; i <= daysInMonth.getDate(); i++) {
        date.setDate(i);
        const enDag = document.createElement("div");
        const span = document.createElement("span");
        span.textContent = date.getDate();
        if (i === 1) {
            const ukeDag = date.getDay();
            if (ukeDag === 0) {
                enDag.style.gridColumn = 7;
            } 
            else {
                enDag.style.gridColumn = ukeDag;
            } 
        }
        if (inputDate.getDate() === date.getDate() && inputDate.getMonth() === date.getMonth()) {
            enDag.classList.add("active-dag");
            currentDay = enDag;
        }
        if (currentDate.getDate() > date.getDate() && currentDate.getMonth() === date.getMonth()) {
            enDag.classList.add("forrige-dag");
        }
        enDag.appendChild(span);
        dagerDiv.appendChild(enDag);
    }
    dagerDiv.addEventListener("click", function(e){
        currentDay = velgDato(e, date, arrayIndex, currentDay);
    });
    kalendarDiv[arrayIndex].appendChild(dagerDiv);
}

function velgDato(e, date, n, currentDay) {
    if (e.target.textContent != "" && e.target.className != "forrige-dag" && e.target.className != "dager") {
        const innDate = new Date(date.getFullYear(), date.getMonth(), e.target.textContent);
        const input = $("#dato-en, #dato-to").get(n);
        input.value = formatterDato(innDate);
        if (currentDay != undefined) {
            currentDay.classList.remove("active-dag");
        }
        e.target.classList.add("active-dag");
        currentDay = e.target;
        input.blur();
        return currentDay;
    }
    return currentDay;
}

function formatterDato(newDate) {
    const date = newDate;
    const monthsArray = ["Januar", "Februar", "Mars", "April", "May", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
    const year = date.getFullYear();
    const month = monthsArray[date.getMonth()];
    const day = date.getDate();
    return `${day}. ${month}, ${year}`;
}

function validerStrekninger(steder) {
    $(".btn3").addClass("disabled");
    const btn = $(".btn3");
    let ok = false;
    btn[0].removeEventListener("click", validerInputs);
 
    for (s of søkeStrekning) {
        const input = s.value.toUpperCase();
        const funnet = steder.find(e => e.toUpperCase() === input);
        if (funnet) {
            ok = true;
        } 
        else {
            ok = false;
            break;
        }
    }
    
    if (ok) {
        $(".btn3").removeClass("disabled");
        btn[0].addEventListener("click", validerInputs);
        
    }

}



function validerInputs() {
    const inputs = $("#fra, #til, #dato-en, #dato-to");
    let spanText = "";
    teller = 0;

    if (inputs[0].value === inputs[1].value) {
        spanText = "Velg forskjellig destinasjon";
        teller++;
    }
    $(".input-div:nth-of-type(2) .alert").text(spanText); 

    
    

    let antall = 0;
    const antallInput = Array.from($("#voksen, #barn, #student, #honnor"));
    antallInput.forEach(e=>{
        const val = Number(e.value);
        antall += val;
    })
    if (antall > 10) {
        spanText = "10 billetter er maks tillatt antall";
        teller++;
    }
    else {
        spanText = "";
    }
    $(".reisende .alert").text(spanText);
    
    if (teller === 0) {
        const kalendar1 = getInputDate(0);
        const kalendar2 = getInputDate(1);

        const retur = $("input[name='retur']:checked").val();
        let harRetur = false;
        if (retur === "retur") {
            harRetur = true;
        }
        
        const typer = {};
        antallInput.forEach(input=>{
            const antall = Number(input.value);
            if (antall === 0) return;
            typer[input.id] = antall;
        })

        const bestilling = {
            fra : inputs[0].value,
            til : inputs[1].value,
            bestillingsDato : formatterDato(new Date()), 
            reiseDato : formatterDato(new Date(Number(kalendar1[2]), kalendar1[1], Number(kalendar1[0]))),
            returDato : null,
            harRetur : harRetur,
            antall : typer,
        }
        if (harRetur) {
            bestilling.returDato = formatterDato(new Date(Number(kalendar2[2]), kalendar2[1], Number(kalendar2[0])))
        }
        console.log(bestilling);

        const finnRute = {
            fra : inputs[0].value,
            reiseDato : formatterDato(new Date(Number(kalendar1[2]), kalendar1[1], Number(kalendar1[0]))),
            klokkaBestilt : new Date().toLocaleTimeString(),
        }
        
        finnBillett(finnRute, bestilling);
    }
    
}

function finnBillett(ruter, bestilling) {
    const rute = ruter;
    
    $.post("bestilling/FinnBillett", rute, function (e) {
        const funnet = encodeURIComponent(JSON.stringify(e));
        const bestillingen = encodeURIComponent(JSON.stringify(bestilling));
        
        const url = "billetter.html?ruter="+funnet+"&bestilling="+bestillingen;
        window.location.href = url;
    });
}
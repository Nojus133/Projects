//Get dom elements
const numbers = Array.from(document.querySelectorAll(".num"));
const oparators = Array.from(document.querySelectorAll(".oparator"));
const equals = document.querySelector(".equals");
const display = document.querySelector(".input span");
const clear = document.querySelector(".clear");
const backspace = document.querySelector(".del");

const currentCalculation = {
    firstValue : "",
    oparator : "",
    oparator2 : "",
    secondValue : ""
};

//Display
function updateDisplay(number) {
    const value = display.textContent;
    if (value === "0"){
        if (number === ".") {
            display.textContent = "0.";
        } else {
            display.textContent = number;
        }
    } else {
        if (number === ".") {
            if (!value.includes(".")) {
                const numString = value + number;
                display.textContent = numString;
            }
        } else {
            const numString = value + number;
            display.textContent = numString;
        }
    }
}

//Keys
numbers.forEach(function(number){
    number.addEventListener('click', function(){
        registerNumber(number.textContent);
        document.activeElement.blur();
    });
})

function registerNumber(number){
    if (currentCalculation.oparator === "") {
        updateDisplay(number);
    } else {
        if (number === ".") {
            display.textContent = "0.";
        } else {
            display.textContent = number;
        }
        currentCalculation.oparator2 = currentCalculation.oparator;
        currentCalculation.oparator = "";
    }
}

//Operators
oparators.forEach(function(oparator){
    oparator.addEventListener('click', function(){
        const op = oparator.dataset.op;
        addOparator(op);
    })
})

function addOparator(op){
    if (currentCalculation.firstValue !== "") {
        currentCalculation.secondValue = currentCalculation.firstValue;
    }
    currentCalculation.firstValue = display.textContent;

    if (currentCalculation.oparator2 !== "" && currentCalculation.oparator === "") {
        operate();
        currentCalculation.oparator = op;
        
    } else {
        currentCalculation.oparator = op;
    }
}

equals.addEventListener('click', calculate)

function calculate(){
    if (currentCalculation.oparator === "" &&
        currentCalculation.oparator2 === "") {
        return;
    } else {
        if (currentCalculation.firstValue !== "") {
            currentCalculation.secondValue = currentCalculation.firstValue;
        }
        currentCalculation.firstValue = display.textContent;
        operate();
        currentCalculation.oparator = "";
        currentCalculation.oparator2 = "";
    }
}

clear.addEventListener('click', function(){
    currentCalculation.oparator = "";
    currentCalculation.oparator2 = "";
    currentCalculation.firstValue = "";
    currentCalculation.secondValue = "";
    display.textContent = "0";
})

backspace.addEventListener('click', deleteLastDigit)

function deleteLastDigit(){
    const value = display.textContent;
    if (value.length > 1) {
        const newValue = value.slice(0, -1);
        display.textContent = newValue;
    } else {
        display.textContent = "0";
    }
}


function operate(){
    let result = 0;
    if (currentCalculation.oparator2 === "divide") {
        if (currentCalculation.firstValue === "0") {
            result = 0;
        } else {
            result = Number(currentCalculation.secondValue) / Number(currentCalculation.firstValue);
        }
    }
    if (currentCalculation.oparator2 === "multiply") {
        result = Number(currentCalculation.secondValue) * Number(currentCalculation.firstValue);
    }
    if (currentCalculation.oparator2 === "subtract") {
        result = Number(currentCalculation.secondValue) - Number(currentCalculation.firstValue);
    }
    if (currentCalculation.oparator2 === "add") {
        result = Number(currentCalculation.secondValue) + Number(currentCalculation.firstValue);
    }
    display.textContent = result; 
    currentCalculation.secondValue = currentCalculation.firstValue;
    currentCalculation.firstValue = result;
}

window.addEventListener('keydown', function(key){
    const shift = key.shiftKey;
    if (key.which >= 48 && key.which <= 57 && !shift || key.which === 190) {
        registerNumber(key.key);
    }
    if (key.which === 187 || 
        key.which === 189 || 
        shift && key.which === 191 ||
        shift && key.which === 55) {
        let op = "";
        if (key.which === 187) {
            op = "add";
        }
        if (key.which === 189) {
            op = "subtract";
        }
        if (shift && key.which === 191) {
            op = "multiply";
        }
        if (shift && key.which === 55) {
            op = "divide";
        }
        addOparator(op);
    }
    if (key.which === 8) {
        deleteLastDigit();
    }
    if (key.keyCode === 13 || shift && key.keyCode === 48) {
        calculate();
    }
    const buttons = Array.from(document.querySelectorAll("button"));
    let activeKey = "";
    if (key.which !== 13 && key.which !==16 && !shift && key.which !== 8) {
        activeKey = buttons.find(button => button.textContent == key.key);
        if (!key.repeat) {
            const active = document.createElement("div");
            active.classList.add("active");
            activeKey.appendChild(active);
            active.classList.add("after");
        }
    }
})
window.addEventListener('keyup',function(key){
    const shift = key.shiftKey;
    if (key.which !== 13 && key.which !== 16 && !shift  && key.which !== 8) {
        const buttons = Array.from(document.querySelectorAll("button"));
    const activeKey = buttons.find(button => button.textContent == key.key);
    const active = activeKey.querySelector(".active");
    activeKey.removeChild(active);
    }
})
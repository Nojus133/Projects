document.querySelector("main").style.display = "none";
let output = document.querySelector(".out p");


function computerPlay() {
  const moves = ["Rock", "Paper", "Scissors"];
  const move = moves[Math.floor(Math.random() * moves.length)];
  return move;
}


let out = "";
let playerScore = document.getElementById("player");
let computerScore = document.getElementById("computer");

function playRound(playerSelection, computerSelection) {
  if ((playerSelection == "Rock") & (computerSelection == "Scissors")) {
    playerScore.innerHTML = Number(playerScore.innerHTML) + 1;
    playerScore.classList.add("playerAnimation");
    out = "Rock beats scissors";
    setTimeout(function() {
      playerScore.classList.remove("playerAnimation");
    }, 1000);

  } else if ((playerSelection == "Scissors") & (computerSelection == "Rock")) {
    computerScore.innerHTML = Number(computerScore.innerHTML) + 1;
    computerScore.classList.add("computerAnimation");
    out = "Rock beats scissors";
    setTimeout(function() {
      computerScore.classList.remove("computerAnimation");
    }, 1000);


  } else if ((playerSelection == "Scissors") & (computerSelection == "Paper")) {
    playerScore.innerHTML = Number(playerScore.innerHTML) + 1;
    playerScore.classList.add("playerAnimation");
    out = "Scissors beat paper";
    setTimeout(function() {
      playerScore.classList.remove("playerAnimation");
    }, 1000);

  } else if ((playerSelection == "Paper") & (computerSelection == "Scissors")) {
    computerScore.innerHTML = Number(computerScore.innerHTML) + 1;
    computerScore.classList.add("computerAnimation");
    out = "Scissors beat paper";
    setTimeout(function() {
      computerScore.classList.remove("computerAnimation");
    }, 1000);

  } else if ((playerSelection == "Paper") & (computerSelection == "Rock")) {
    playerScore.innerHTML = Number(playerScore.innerHTML) + 1;
    playerScore.classList.add("playerAnimation");
    out = "Paper beats rock";
    setTimeout(function() {
      playerScore.classList.remove("playerAnimation");
    }, 1000);

  } else if ((playerSelection == "Rock") & (computerSelection == "Paper")) {
    computerScore.innerHTML = Number(computerScore.innerHTML) + 1;
    computerScore.classList.add("computerAnimation");
    out = "Paper beats rock";
    setTimeout(function() {
      computerScore.classList.remove("computerAnimation");
    }, 1000);

  } else {
    // playerScore.innerHTML = Number(playerScore.innerHTML) + 1;
    // computerScore.innerHTML = Number(computerScore.innerHTML) + 1;
    out = "Draw";
    // return;
  }
  console.log(out);
  output.textContent = out;
}

function resetScore() {
  playerScore.innerHTML = "0";
  computerScore.innerHTML = "0";
  document.getElementById("arena").innerHTML = "";
}

function game(choice) {
  document.getElementById("arena").innerHTML = "";

  const imgRock = document.createElement("img");
  imgRock.src = "icons8-hand-rock-100.png";

  const imgHand = document.createElement("img");
  imgHand.src = "icons8-hand-100.png";

  const imgScissors = document.createElement("img");
  imgScissors.src = "icons8-hand-scissors-100.png";

  const imgRock2 = document.createElement("img");
  imgRock2.src = "icons8-hand-rock-101.png";

  const imgHand2 = document.createElement("img");
  imgHand2.src = "icons8-hand-101.png";

  const imgScissors2 = document.createElement("img");
  imgScissors2.src = "icons8-hand-scissors-101.png";

  

  let picDiv = document.createElement("div");
  picDiv.id = "arena__player";
  document.getElementById("arena").appendChild(picDiv);
  let picDiv2 = document.createElement("div");
  picDiv2.id = "arena__computer";
  document.getElementById("arena").appendChild(picDiv2);

  imgRock.style.transform = "rotate(90deg)";
  document.getElementById("arena__player").appendChild(imgRock);
  document.getElementById("arena__player").classList.add("shakeplayer");
  imgRock2.style.transform = "rotate(-90deg)";
  document.getElementById("arena__computer").appendChild(imgRock2);
  document.getElementById("arena__computer").classList.add("shakecomputer");
  document.querySelector("body").style['pointer-events']= "none";

  setTimeout(function(){
    const playerSelection = choice.children[1].innerText;
    const computerSelection = computerPlay();

    document.getElementById("arena__player").innerHTML = "";
    document.getElementById("arena__computer").innerHTML = "";


    if (playerSelection == "Rock") {
      imgRock.style.transform = "rotate(90deg)";
      document.getElementById("arena__player").appendChild(imgRock);
    }
    if (playerSelection == "Paper") {
      imgHand.style.transform = "rotate(90deg)";
      document.getElementById("arena__player").appendChild(imgHand);
    }
    if (playerSelection == "Scissors") {
      document.getElementById("arena__player").appendChild(imgScissors);
    }
    if (computerSelection == "Rock") {
      imgRock2.style.transform = "rotate(-90deg)";
      document.getElementById("arena__computer").appendChild(imgRock2);
    }
    if (computerSelection == "Paper") {
      imgHand2.style.transform = "rotate(-90deg)";
      document.getElementById("arena__computer").appendChild(imgHand2);
    }
    if (computerSelection == "Scissors") {
      imgScissors2.style.transform = "scaleX(-1)";
      // imgScissors.style.transform = "rotate()";
      document.getElementById("arena__computer").appendChild(imgScissors2);
    }
    document.querySelector("body").style['pointer-events']= "";
    playRound(playerSelection, computerSelection);
  }, 1000)
 
}

let removeEventListener = false;
const choices = document.querySelectorAll(".choices");
function playGame() {
  resetScore();
  document.querySelector("main").style.display = "block";
  document.querySelector(".play").style.display = "none";
  document.querySelector("main").style["pointer-events"] = "unset";
  document.querySelector(".play").style["background-image"] = "";
  document.querySelector(".play p").innerHTML = "";
  output.textContent = "Make a move";

  choices.forEach(function(choice) {
    if (removeEventListener === true) {
      choice.removeEventListener("click", calculateResult);
      console.log("removed event listener");
    }
    choice.addEventListener("click", calculateResult);
  });
}

function calculateResult() {
  let selection = this;

  game(this);
  removeEventListener = true;
  console.log("added event listener");

  setTimeout(function(){
    if (playerScore.innerHTML == "3" || computerScore.innerHTML == "3") {
      document.querySelector("main").style["pointer-events"] = "none";
      setTimeout(function() {
        document.querySelector("main").style.display = "none";
        document.querySelector(".play").style.display = "block";
        document.querySelector(".play button").textContent = "Play again";
  
        if (playerScore.innerHTML > computerScore.innerHTML) {
          console.log("You win");
          document.querySelector(".play").style["background-image"] =
            "url(confetti.gif)";
          document.querySelector(".play h1").textContent = "You won!";
        } else if (playerScore.innerHTML < computerScore.innerHTML) {
          console.log("Computer wins");
          document.querySelector(".play h1").textContent = "You lost!";
        } else {
          console.log("draw");
          document.querySelector(".play h1").textContent = "Draw";
        }
        resetScore();
      }, 1500);
      return;
    }
  }, 1000)
}

let loader = document.getElementById("loader");
let num = 0;

window.addEventListener("scroll", function() {
  let winHeight = window.innerHeight;
  let docHeight = document.body.scrollHeight;
  let max = docHeight - winHeight;
  loader.setAttribute("max", max);
  let value = window.pageYOffset;
  loader.setAttribute("value", value);
});

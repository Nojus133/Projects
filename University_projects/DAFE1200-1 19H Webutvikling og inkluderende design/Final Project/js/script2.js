//Change active
let navLinks = document.querySelectorAll('nav ul li a');
let pages = document.querySelectorAll('section');

function changeActive() {
  let current = pages.length;
  while(--current && window.scrollY + 100 < pages[current].offsetTop) {}
  navLinks.forEach((link) => link.classList.remove('active'));
  navLinks[current].classList.add('active');
}

changeActive();
window.addEventListener('scroll', changeActive);

//Change text size
var fontSizeSmall = true ;
function changeFontSize() {
  text = document.getElementById('text-size');
  if (fontSizeSmall) {
  text.style.fontSize = "12px" ;
  fontSizeSmall = false;
  }
  else {
  text.style.fontSize = "10px" ;
  fontSizeSmall = true ;
  }
}

//Clear text
  function myFunction2() {
    document.getElementById("contact-form").reset();
  }

//Slideshow doesnt work
  var a = document.getElementById("noscript");
  a.style.display = "none";


//Slideshow
var slideNummer = 1;
slides(slideNummer);

function nextSlide(number) {
  slides(slideNummer += number);
}

function activeSlide(number) {
  slides(slideNummer = number);
}

function slides(number) {
    var index;
    var slides = document.getElementsByClassName("slides");
    var prikker = document.getElementsByClassName("prikk");
    if (number > slides.length) {slideNummer = 1}
    if (number < 1) {slideNummer = slides.length}
    for (index = 0; index < slides.length; index++) 
    {slides[index].style.display = "none";}
    for (index = 0; index < prikker.length; index++)
     {prikker[index].className = prikker[index].className.replace(" active2", "")}
    slides[slideNummer-1].style.display = "block";
    prikker[slideNummer-1].className += " active2";
    }

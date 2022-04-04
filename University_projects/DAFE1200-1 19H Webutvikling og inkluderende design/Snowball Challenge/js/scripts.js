//koden ble tatt fra https://codepen.io/ivanmt07/pen/pxONrw

function onScroll(event){
  var sections = document.querySelectorAll('.nav-list li a');
  var scrollPos = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
  
  for( var i = 0; i < sections.length; i++ ){
    var currLink = sections[i]; 
    var val = currLink.getAttribute('href');
    var refElement = document.querySelector(val);
      if( refElement.offsetTop <= scrollPos && ( refElement.offsetTop + refElement.offsetHeight > scrollPos)){
        document.querySelector('.nav-list li a').classList.remove('active');
        currLink.classList.add('active');
      }
    else{
         currLink.classList.remove('active');
       }
  }

};

window.document.addEventListener('scroll', onScroll );

let nav = document.querySelector('.box'),
			navToogler =  document.querySelector('.nav-icon')

		navToogler.addEventListener('click', function (event) {
			nav.classList.toggle('active2')
		})
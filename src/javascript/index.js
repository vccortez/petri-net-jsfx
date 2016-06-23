/**
 * 
 */
window.addEventListener('load', function() {
	var phrase = document.createElement('p');
	
	phrase.innerHTML = JSON.parse(java.loadJSON()).message;
	
	document.body.appendChild(phrase);
}, false);
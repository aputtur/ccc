var ccc_top_home_timer='';
var ccc_top_home_timer2='';

function Pause() {
ccc_top_home_timer2 = setTimeout("delay_MM_swapImgRestore()",500); // 1 sec
return false;
}

function ClearTimeout() {

try {
clearTimeout(ccc_top_home_timer)
return false;
}

catch (something) {
	
}

}


function Pause_swapImage(a) {

if ( a == "business" ) {

ccc_top_home_timer = setTimeout("MM_swapImage('splash_business','','/media/images/home-splash-business-on.gif','splash_photo','','/media/images/home_splash-photo-business.jpg',1)",500); // .5 sec
return false;

}

if ( a == "academic" ) {

ccc_top_home_timer = setTimeout("MM_swapImage('splash_academic','','/media/images/home-splash-academic-on.gif','splash_photo','','/media/images/home_splash-photo-academic.jpg',1)",500); // .5 sec
return false;

}

if ( a == "copyright" ) {

ccc_top_home_timer = setTimeout("MM_swapImage('splash_copyright','','/media/images/home-splash-copyright-on.gif','splash_photo','','/media/images/home_splash-photo-copyright.jpg',1)",500); // .5 sec
return false;

}



}








function delay_MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}


function randoho() {
	
	var randomnumber=Math.floor(Math.random()*5)
	
	switch (randomnumber)
	
	{
	
	case 0:
		if(document.getElementById('one')) document.getElementById('one').style.display='block';
		break
	
	case 1:
		if(document.getElementById('two')) document.getElementById('two').style.display='block';
		break
	
	case 2:
		if(document.getElementById('three')) document.getElementById('three').style.display='block';
		break
	
	case 3:
		if(document.getElementById('four')) document.getElementById('four').style.display='block';
		break
	case 4:
		if(document.getElementById('five')) document.getElementById('five').style.display='block';
		break
}

}

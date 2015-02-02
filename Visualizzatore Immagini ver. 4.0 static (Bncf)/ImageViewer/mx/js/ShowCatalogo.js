function openExternalOpera(idr){
	  var link;
	  var ris;
	  var pos;
	  link = document.location.href;
	  ris = "";
	  pos = link.indexOf('?');
	  if (pos >-1)
	    link = link.substring(0,pos);

	  link += "?idr="+idr;
	  window.open(link, "_blank");
}

function openOpera(idr){
  var link;
  var ris;
  var pos;
  link = document.location.href;
  ris = "";
  pos = link.indexOf('?');
  if (pos >-1)
    link = link.substring(0,pos);

  link += "?idr="+idr;
  document.location.href=link;
}

function init(){
  resize();
  waitBoxStop();
  window.onresize = resize;
}

function resize(){
  var height;
  var width;

  height = document.getElementById("footer").offsetTop-document.getElementById("header").offsetHeight;
  height -= document.getElementById("menu").offsetTop

  width=0;
  if (navigator.appName.indexOf("Microsoft") != -1 &&
      document.getElementById("periodiciShow").style.display=="")
  {
    width = document.getElementById("periodiciShow").offsetLeft+document.getElementById("periodiciShow").offsetWidth;
  }
  if (document.getElementById("paginaShow")!= null)
  {
    if (navigator.appName.indexOf("Microsoft") != -1)
    {
      width += document.getElementById("paginaShow").offsetLeft+document.getElementById("paginaShow").offsetWidth;
    }
    else
    {
      width = document.getElementById("paginaShow").offsetLeft+document.getElementById("paginaShow").offsetWidth;
    }
  }

  if (navigator.appName.indexOf("Microsoft") != -1)
  {
    width = document.getElementById("footer").offsetWidth-width-4
  }
  else
  {
    width = document.getElementById("footer").offsetWidth-width-6
  }
  width -= 2;
//  height -= 10;
  document.getElementById("columns").style.height=height;
  document.getElementById("columns").style.width=width;
  window.scroll(0,0);
}

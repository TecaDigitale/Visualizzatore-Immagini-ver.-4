isApplet = true;

function initFocus()
{
  window.onresize = "";
  if (document.getElementById("logo") != null)
  {
    document.getElementById("logo").innerHTML="<img src=\"http://opac.bncf.firenze.sbn.it/opac/img/logo-bncf.jpg\"/>";
  }
  myResize();
  waitBoxStop();
  window.onresize = myResize;
  return false;
}

function showHelp()
{
  help = window.open("help.html", 
                     "_blank", 
                     "width=800,height=500");
  help.focus();
}

function showImg(risIdr)
{
  var app = document.getElementById('myApplet');
  app.viewImage(risIdr);
}

function changeViewCol(nomeCol, testoMsg)
{
  if (document.getElementById(nomeCol) != null)
  {
    if (document.getElementById(nomeCol).style.display=="none")
    {
      document.getElementById(nomeCol).style.display="";
      document.getElementById(nomeCol+"Img").src="./images/left.gif";
      document.getElementById(nomeCol+"Img").alt="Nascondi "+testoMsg;
      document.getElementById(nomeCol+"Img").title="Nascondi "+testoMsg;
    }
    else
    {
      document.getElementById(nomeCol).style.display="none";
      document.getElementById(nomeCol+"Img").src="./images/right.gif";
      document.getElementById(nomeCol+"Img").alt="Visualizza "+testoMsg;
      document.getElementById(nomeCol+"Img").title="Visualizza "+testoMsg;
    }
  }
  myResize();
  return false;
}

function changePage(num)
{
  dPagine.openTo('IMG.'+num, true, false);
  changeImg(num);
}

this.aPageImg =[];

function addPage(risIdr)
{
  this.aPageImg[this.aPageImg.length] = risIdr;
}

function changeImg(risIdr)
{
  pageAtt=risIdr;
  var app = document.getElementById('myApplet');
  if (app != null)
  {
    if (isApplet)
      app.viewImage(this.aPageImg[risIdr]);
    else
      app.src=this.aPageImg[risIdr];
  }
}

function pageFirst()
{
  pageAtt=0;
  changePage(pageAtt);
  return false;
}

function pagePrevious()
{
  if (pageAtt<=0)
    pageAtt = 0;
  else
  {
    pageAtt--;
    changePage(pageAtt);
  }
  return false;
}

function pageNext()
{
  if (pageTot<=pageAtt)
    pageAtt = pageTot;
  else
  {
    pageAtt++;
    changePage(pageAtt);
  }
  return false;
}

function pageLast()
{
  pageAtt=pageTot;
  changePage(pageAtt);
  return false;
}

function showImg(risIdr, risIdrFiglio)
{
  waitBoxStart();
  var url = "";
  url = location.protocol;
  url += "//";
  url += location.host;
  url += "/TecaViewer/index.jsp?RisIdr="+risIdr+"&RisIdrFiglio="+risIdrFiglio;
  location.href= url;
  return false;
}

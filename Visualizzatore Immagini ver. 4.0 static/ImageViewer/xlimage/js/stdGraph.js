
function initFocus()
{
  window.onresize = "";
  myResize();
  waitBoxStop();
  window.onresize = myResize;
  return false;
}

this.aPageImg =[];

function addPage(risIdr)
{
  this.aPageImg[this.aPageImg.length] = risIdr;
}

function changePage(num)
{
  dPagine.openTo('IMG.'+num, true, false);
  changeImg(num);
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

function waitBoxStop(){
    document.getElementById('wait').style.display='none';
}

function waitBoxStart(){
    document.getElementById('wait').style.display='block';
}

function changeViewCol(nomeCol, testoMsg)
{
  if (document.getElementById(nomeCol) != null)
  {
    if (document.getElementById(nomeCol).style.display=="none")
    {
      document.getElementById(nomeCol).style.display="";
      document.getElementById(nomeCol+"Img").src="../xlimage/images/left.gif";
      document.getElementById(nomeCol+"Img").alt="Visualizza "+testoMsg;
      document.getElementById(nomeCol+"Img").title="Visualizza "+testoMsg;
    }
    else
    {
      document.getElementById(nomeCol).style.display="none";
      document.getElementById(nomeCol+"Img").src="../xlimage/images/right.gif";
      document.getElementById(nomeCol+"Img").alt="Nascondi "+testoMsg;
      document.getElementById(nomeCol+"Img").title="Nascondi "+testoMsg;
    }
  }
  myResize();
  return false;
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


window.onresize=myResize;
function myResize()
{
  var height;
  var width;
  
  height = document.getElementById("footer").offsetTop-document.getElementById("header").offsetHeight;

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
  height -= 2;
  resizePediodici(height);
  resizePagine(height);
  resizeViewer(height,width);
  window.scroll(0,0);
}

function resizePediodici(w_newHeight)
{
  if (document.getElementById("periodici") != null)
  {
    if (document.getElementById("periodici").style.display=="")
    {
      document.getElementById("periodici").style.height=w_newHeight+"px";
      if (navigator.appName.indexOf("Microsoft") != -1)
      {
        document.getElementById("dPeriodici").style.width="224px";
      }
      else
      {
        document.getElementById("dPeriodici").style.width=document.getElementById("periodici").offsetWidth+"px";
      }
      document.getElementById("dPeriodici").style.height=w_newHeight+"px";
      document.getElementById("dPeriodici").style.display="";
    }
  }
  if (document.getElementById("periodiciShow") != null && document.getElementById("periodiciShow").style.display=="" && navigator.appName.indexOf("Microsoft") == -1)
  {
    document.getElementById("periodiciShow").style.height=w_newHeight+"px";
  }
}

function resizePagine(w_newHeight)
{
  if (document.getElementById("pagina") != null && document.getElementById("pagina").style.display=="")
  {
    document.getElementById("pagina").style.height=w_newHeight+"px";
    if (navigator.appName.indexOf("Microsoft") != -1)
    {
      document.getElementById("dPagine").style.width="224px";
    }
    else
    {
      document.getElementById("dPagine").style.width=document.getElementById("pagina").offsetWidth+"px";
    }
    document.getElementById("dPagine").style.height=(w_newHeight-15)+"px";
    document.getElementById("dPagine").style.display="";
  }
  if (document.getElementById("paginaShow") != null && document.getElementById("paginaShow").style.display=="" && navigator.appName.indexOf("Microsoft") == -1)
  {
    document.getElementById("paginaShow").style.height=w_newHeight+"px";
  }
}

function resizeViewer(w_newHeight, w_newWidth)
{
  if (document.getElementById("viewer") != null && document.getElementById("viewer").style.display=="")
  {
    document.getElementById("viewer").style.height=w_newHeight+"px";
    document.getElementById("viewer").style.width=w_newWidth+"px";
    document.getElementById("myApplet").style.height=w_newHeight+"px";
    document.getElementById("myApplet").style.width=w_newWidth+"px";
  }
}

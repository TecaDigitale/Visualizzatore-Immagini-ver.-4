var curTable;
var isIE;
var startAjax;
var isHtml;

/**
  * Questo metodo viene utilizzato per eseguire la chiamata Ajax
  */
function AJAXCall(url, tipoRis, Html)
{
  startAjax = true;
  document.getElementById('wait').style.display='block';
  isHtml=Html;
  var ajax = new AJAXInteraction(url, tipoRis, Html);
  ajax.send(null);
}

/**
  * Questo metodo viene utilizzato per eseguire la chiamata Ajax Sincrona
  */
function AJAXCallSync(url, tipoRis, Html)
{
  startAjax = true;
  document.getElementById('wait').style.display='block';
  isHtml=Html;
  var ajax = new AJAXInteraction(url, tipoRis, Html);
  ajax.sendSync(null);
}

/**
  * Questo metodo viene utilizzato per verificare la chiamata Ajax
  */
function AJAXInteraction(url, caller, Html) 
{
  var tipoRis = caller;
  var url = url;
  var isHtml = Html;
  
  var req = init();
  
  req.onreadystatechange = processRequest;
  
  /**
    * Questo metodo inizializza i parametri principali controllando se stiamo utilizzato 
    * IE o FireFox
    */
  function init() 
  {
    if (window.ActiveXObject) 
    {
      isIE = true;
      return new ActiveXObject("Microsoft.XMLHTTP");
    }
    else if (window.XMLHttpRequest) 
    {
      return new XMLHttpRequest();
    } 
  }

  /**
    * Questo metodo verifica lo stato della chiamata aspettando la conclusione della
    * attivita'
    */
  function processRequest () 
  {
    if (req.readyState == 4) 
    {
      if (req.status == 200) 
      {
        if (isIE)
        {
          if (isHtml)
          {
            postProcess(req.responseText, tipoRis);
          }
          else
          {
	        var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
    			xmlDoc.async="false";
    			xmlDoc.loadXML(req.responseText);
	        postProcess(xmlDoc, tipoRis);
          }
        }
        else
        {
	      if (isHtml)
          {
        	postProcess(req.responseText, tipoRis);
          }
          else
          {
            if (req.responseXML)
            {
          	  postProcess(req.responseXML, tipoRis);
            }
            else
            {
              alert("Problemi nella risposta "+req.status+"\n"+req.responseText);
              document.getElementById('wait').style.display='none';
            }
          }
        }
      }
      else
      {
        alert("Problemi nella risposta "+req.status+"\n"+req.responseText);
        document.getElementById('wait').style.display='none';
      }
    }
  }

  /**
    * Questo metodo viene utilizzato per eseguire la chiamata Ajax
    */
  this.send = function() 
  {
    req.open("GET", url, true);
    req.send(null);
  }

  /**
    * Questo metodo viene utilizzato per eseguire la chiamata Ajax
    */
  this.sendSync = function() 
  {
    req.open("GET", url, false);
    req.send(null);
  }
}

/**
  * Questo metodo viene utilizzato per calcolare l'URL chiamato togliento il riferimato alla 
  * pagina JSP
  */
function readUrlParent()
{
  var link;
  var ris;
  var pos;
  link = document.location.href;
  ris = "";
  pos = link.indexOf('?');
  if (pos >-1)
    link = link.substring(0,pos);
  pos = link.indexOf('/');
  while(pos>-1)
  {
    ris = ris+link.substring(0,pos+1);
    link = link.substring(pos+1);
    pos = link.indexOf('/');
  }
  return ris;
}
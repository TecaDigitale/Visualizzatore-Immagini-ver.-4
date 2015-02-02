
var tracciatoXml;

/**
 * Questo metodo viene utilizzato per inizializzare le informazioni sul Libro
 */
function initBook(idr)
{
    jQuery.ajax({
        type: "GET",
        url: "/ImageViewer/servlet/ImageViewer?idr="+idr+"&azione=readBook",
        dataType:"xml",
        success: function(response)
        {
    		showBook(response, "readBook");
        },
        error: function (request, status, thrownError){
            alert(request.status);
            alert(status);
        }    
    });

}

/**
  * Questa funzione viene invocata dal processo Ajax per elaborare la Risposta
  */
function showBook(responseXML, tipoRis)
{
  var titolo = '';
  var urlTitolo='';
  if (tipoRis == 'readBook')
  {
    tracciatoXml = responseXML;
//    br = new ImageViewer(1500, 2116, '');
    br = new ImageViewer(tracciatoXml);

    try{
      readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
      if (readBook == null){
        if (prefixXml==""){
          prefixXml="mx-libro:";
        } else {
          prefixXml="";
        }
        readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
      }
    } catch (e){
      if (prefixXml==""){
        prefixXml="mx-libro:";
      } else {
        prefixXml="";
      }
      readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
    }
    if (readBook != null)
    {
      try{
        datiBibliografici = readBook.getElementsByTagName(prefixXml+"datiBibliografici")[0];
        if (readBook == null){
          if (prefixXml==""){
            prefixXml="mx-libro:";
          } else {
            prefixXml="";
          }
          datiBibliografici = readBook.getElementsByTagName(prefixXml+"datiBibliografici")[0];
        }
      } catch (e){
        if (prefixXml==""){
          prefixXml="mx-libro:";
        } else {
          prefixXml="";
        }
        datiBibliografici = readBook.getElementsByTagName(prefixXml+"datiBibliografici")[0];
      }
      if (datiBibliografici != null)
      {
        if (datiBibliografici.getElementsByTagName("autore")[0] != null)
        {
          titolo = datiBibliografici.getElementsByTagName("autore")[0].childNodes[0].nodeValue;
        }
        if (datiBibliografici.getElementsByTagName("titolo")[0] != null)
        {
          if (titolo != '')
            titolo = titolo+" ";
          titolo = titolo+datiBibliografici.getElementsByTagName("titolo")[0].childNodes[0].nodeValue;
        }

        if (datiBibliografici.getElementsByTagName("urlDatiBibliografici")[0] != null)
        {
          if (titolo != '')
            titolo = titolo+" ";
          urlTitolo = datiBibliografici.getElementsByTagName("urlDatiBibliografici")[0].childNodes[0].nodeValue;
        }
      }
      br.setBook(titolo, urlTitolo);
    
      // Viene utilizzato per indicare la pagina da visualizzare
      //			br.setTitleLeaf(11);
   

br.cleanupMetadata();
if (br.olAuth) {
    var olAuth = new OLAuth();
    olAuth.init();
} else {
      // Visualizzazione del visualizzatore
      br.initImg(2);
//    alert("10");
} 
    }
    else
    {
      alert("Non \u00E8 possibile reperire le informazioni del libro");
    }
  }
  document.getElementById('wait').style.display='none';
  startAjax = false;
//  alert("showBook END");
}


function toggle_sidebar() {
	
	if($('.sidecol').css('left') == "0px") {
	
		$('.sidecol').animate({"left": "-304px"}, {
			duration: "slow",
			complete: function() {
		
		      $(this).css('box-shadow', 'none');
			}
	    });	
		
	} else {
		
		$('.sidecol').css('box-shadow', '-1px 0 0px #888');
		$('.sidecol').animate({"left": "0px"}, "slow");
	}
	
	return false;
}

function toggle_sidebarStru() {
	
//	if($('.sidecolStru').css('left') == "0px") {
//	
//		$('.sidecolStru').animate({"left": "-304px"}, {
//			duration: "slow",
//			complete: function() {
//		
//		      $(this).css('box-shadow', 'none');
//			}
//	    });	
//		
//	} else {
//		
//		$('.sidecolStru').css('box-shadow', '-1px 0 0px #888');
//		$('.sidecolStru').animate({"left": "0px"}, "slow");
//	}
	
	return false;
}

function changePage(page){
	var url = '';
	var newUrl = '';

	url =location.href;

	pos = url.indexOf('#');
    if (pos >-1){
        newUrl = url.substring(0,pos+1);
        url = url.substring(pos+1);
    }

    pos = url.indexOf('/');
    if (pos >-1){
        newUrl += url.substring(0,pos+1);
        url = url.substring(pos+1);
    }

    if (url.indexOf('/1up')> -1){
      newUrl += (page+1);
    } else {
      newUrl += page;
    }
    pos = url.indexOf('/');
    if (pos >-1){
        url = url.substring(pos);
    }
    newUrl += url;
    toggle_sidebarStru();
	location.href=newUrl;
	document.getElementById('wait').style.display='none';
}

function BRnavTop(scrollTop, clientHeight, offsetHeight){

  ris = scrollTop + clientHeight-(offsetHeight*4);
//  alert("scrollTop: "+scrollTop + " clientHeight: "+clientHeight + " offsetHeight: "+offsetHeight+
//        "scrollTop+clientHeight-offsetHeight: "+ris);
  return ris;
}

function BRContainerHeight(scrollTop, clientHeight, offsetHeight){
  ris = scrollTop + clientHeight-200;
  return ris;
}

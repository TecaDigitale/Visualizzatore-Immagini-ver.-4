
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

    readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
    if (readBook != null)
    {
      datiBibliografici = readBook.getElementsByTagName(prefixXml+"datiBibliografici")[0];
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
    
      // Visualizzazione del visualizzatore
      br.initImg(1);
//    alert("10");
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

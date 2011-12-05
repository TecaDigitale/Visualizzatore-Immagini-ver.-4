	
/**
 * Costruttore della classe che viene utilizzaato per la gestione
 * del visualizzatore
 *
 * @param tracciatoXml
 */
function ImageViewer(tracciatoXml)
{
  this.tracciatoXml = tracciatoXml;
  this.width = 1500;
  this.height = 2116;
  this.idr = '';
}

/**
 * Costruttore della classe che viene utilizzata per la gestione
 * del visualizzatore
 * 
 * @param width Indica la larghezza della Pagina
 * @param height Indica l'altezza della Pagina
function ImageViewer(width, height, idr)
{
//  console.log('Costruttore ImageViewer');
  this.width = width;
  this.height = height;
  this.idr = idr;
}
 */

/**
 * Viene ereditata le funzioni della classe "BookReader"
 */
ImageViewer.inherits(BookReader);

/**
 * Questo metodo viene utilizzato per indicare la larghezza dell'immagine
 */
ImageViewer.method('getPageWidth', function(index)
{
//	ImageViewer.log('getPageWidth: index: '+index+' width:'+this.width);
  immagine = this.getImmagine(index);
  if (this.isCostola())
  {
    if (this.mode==2 || this.mode ==3)
    {
      immagine = this.getImmagine(index+1);
    }
  }
  return parseInt(immagine.getElementsByTagName("larghezza")[0].childNodes[0].nodeValue);
});

/**
 * Questo metodo viene utilizzato per indicare l'altezza dell'immagine
 */
ImageViewer.method('getPageHeight', function(index)
{
//	ImageViewer.log('getPageHeight: index: '+index+' height:'+this.height);
  immagine = this.getImmagine(index);
  if (this.isCostola())
  {
    if (this.mode==2 || this.mode ==3)
    {
      immagine = this.getImmagine(index+1);
    }
  }

  return parseInt(immagine.getElementsByTagName("altezza")[0].childNodes[0].nodeValue);
});

/**
 * Questo metodo viene utilizzato per indicare l'URL della pagina da 
 * visualizzare
 */
ImageViewer.method('getPageURI', function(index, reduce, rotate)
{
  //	ImageViewer.log('getPageURI: index: '+index+' reduce:'+reduce+' rotate:'+rotate);

  // reduce and rotate are ignored in this simple implementation, but we
  // could e.g. look at reduce and load images from a different directory
  // or pass the information to an image server
  var leafStr = '000';
  var imgStr = (index+1).toString();
  var re = new RegExp("0{"+imgStr.length+"}$");

  immagine = this.getImmagine(index);
  if (this.isCostola())
  {
    if (this.mode==2 || this.mode ==3)
    {
      immagine = this.getImmagine(index+1);
    }
  }

    
    var url = 'http://'+location.hostname;
    url += '/ImageViewer/servlet/ImageViewer';
    url += '?idr='+immagine.attributes.getNamedItem("ID").value;
    url += '&azione=showImg';
    if (this.mode==2 || this.mode==3)
    {
      url += '&sequence='+(index+2);
    }
    else
    {
      url += '&sequence='+(index+1);
    }
    if ('undefined' != typeof(reduce))
    {
        url += '&reduce='+reduce;
    }
    if ('undefined' != typeof(rotate))
    {
        url += '&rotate='+rotate;
    }
    url += '&mode='+this.mode;
    if (this.mode==1)
    {
        url += '&height='+parseInt(this._getPageHeight(index)/this.reduce)+'\n';
    }
    else if (this.mode==2)
    {
        url += '&height='+this.twoPage.height;
    }
    return url;
});

/**
 * Questo metodo viene utilizzato per indicare l'URL della pagina da 
 * visualizzare
 */
ImageViewer.method('_getPageFile', function(index)
{
//	ImageViewer.log('getPageURI: index: '+index+' reduce:'+reduce+' rotate:'+rotate);

    return this.getPageURI(index);
});

/**
 * Questo metodo viene utilizzato per indicare se l'immagine è da 
 * visualizzare sul lato destro o sinistro quando sono affinacate
 */
ImageViewer.method('getPageSide', function(index)
{
//	ImageViewer.log('getPageSide: index: '+index);

    if (0 == (index & 0x1)) 
    {
        return 'R';
    } 
    else 
    {
        return 'L';
    }
});

/**
 * Questo metodo viene utilizzato per indicare se l'immagine è da 
 * visualizzare sul lato destro o sinistro quando sono affinacate
 */
ImageViewer.method('canRotatePage', function(index)
{
//	ImageViewer.log('canRotatePage: index: '+index);

	return true;
});

/**
 * Questo metodo viene utilizzato per indicare il numero della 
 * pagina che viene visualizzata
 */
ImageViewer.method('getPageNum', function(index)
{
//	ImageViewer.log('getPageNum: index: '+index);

	return index+1;
});

/**
 * Questa funzione restituisce gli indici a sinistra ea destra per la 
 * diffusione visibile all'utente che contiene il dato indice. I valori 
 * di ritorno può essere null se non esiste una pagina di fronte o 
 * l'indice non è valido.
 */
ImageViewer.method('getSpreadIndices',function(pindex) 
{
	// $$$ we could make a separate function for the RTL case and
	//      only bind it if necessary instead of always checking
	// $$$ we currently assume there are no gaps

	var spreadIndices = [null, null]; 
	if ('rl' == this.pageProgression) 
	{
		// Right to Left
		if (this.getPageSide(pindex) == 'R')
		{
			spreadIndices[1] = pindex;
			spreadIndices[0] = pindex + 1;
		}
		else
		{
			// Given index was LHS
			spreadIndices[0] = pindex;
			spreadIndices[1] = pindex - 1;
		}
	}
	else
	{
		// Left to right
		if (this.getPageSide(pindex) == 'L')
		{
			spreadIndices[0] = pindex;
			spreadIndices[1] = pindex + 1;
		}
		else
		{
			// Given index was RHS
			spreadIndices[1] = pindex;
			spreadIndices[0] = pindex - 1;
		}
	}
 
	//console.log("   index %d mapped to spread %d,%d", pindex, spreadIndices[0], spreadIndices[1]);
 
	return spreadIndices;
});

/**
 * Questo metodo viene utilizzato per indicare la descrizione della
 * pagina visualizzata
 */
ImageViewer.method('getPageName', function(index)
{
//	ImageViewer.log('getPageName: index: '+index);
  immagine = this.getImmagine(index);
  if (this.isCostola())
  {
    if (this.mode==2 || this.mode ==3)
    {
      immagine = this.getImmagine(index+1);
    }
  }

  return immagine.getElementsByTagName("mx-libro:nomenclatura")[0].childNodes[0].nodeValue;
});

/**
 * Questo metodo viene utilizzato per indicare la descrizione della
 * pagina visualizzata
 */
ImageViewer.method('leafNumToIndex', function(index)
{
//	this.log('leafNumToIndex: index: '+index);

	return index;
});

/**
 * Questo metodo viene utilizzato per indicare le informazioni
 * relative al libro
 * 
 * @param bookTitle Titolo del libro
 * @param bookUrl Url relativo alla scheda del libro
 */
ImageViewer.method('setBook', function(bookTitle, bookUrl)
{
//	console.log('setBook: bookTitle: '+bookTitle+' bookUrl: '+bookUrl);

	this.bookTitle = bookTitle;
	this.bookUrl = bookUrl;
	return this;
});

/**
 * Questo metodo viene utilizzato per indicare la pagina 
 * da visualizzare
 */
ImageViewer.method('setTitleLeaf', function(page)
{
//	console.log('setTitleLeaf: page: '+page);

	this.titleLeaf = page;
	return this;
});

/**
 * Questo metodo viene utilizzato per iniziare il disegno della pagina
 * 
 * @param mode Indica la modalita di visualizzazione delle pagine 
 *             (1= Pagina Signola, 2= Doppia Pagina, 3= Icone)
 */
ImageViewer.method('initImg', function(mode)
{
	// Numero totale della pagine
	this.numLeafs = this.imgTot();
	// Indica il percorso dove trovare le immagini di base per gestire
	// il visualizzatore
	this.imagesBaseURL = '/ImageViewer/BookReader/images/';
	
	this.mode = mode;

    this.server=location.hostname;
    this.subPrefix='unzip';
    this.zip='/usr/bin/unzip';
    this.imageFormat='jpeg';

    this.init();
//	this.uber('init');
alert("5");
});

/**
 * Metodo utilizzato per reperire il numero delle pagine del libro
 *
 */
ImageViewer.method('switchToolbarModeMy', function(mode) 
{

  this.numLeafs = this.imgTot();
  if (this.isCostola())
  {
    if (mode==2 || mode ==3)
    {
      this.numLeafs--;
    }
  }
  this.switchToolbarMode(mode);

//  this.uber('switchToolbarMode', mode);
});

/**
 * Metodo utilizzato per indicare se è presente la costola del libro 
 *
 */
ImageViewer.method('isCostola', function() 
{
  var isCostola = false;

  readBook = tracciatoXml.getElementsByTagName("mx-libro:readBook")[0];
  if (readBook != null)
  {
    immagini = readBook.getElementsByTagName("mx-libro:immagini")[0];
    if (immagini != null)
    {
      if (immagini.attributes != null)
      {
        if (immagini.attributes.getNamedItem("isCostola") != null)
        {
  try
  {
          if (immagini.attributes.getNamedItem("isCostola").value == "true")
          {
            isCostola = true;
          }
          }
          catch(e)
          {
          }
        }
      }
    }
  }
  return isCostola;
});

/**
 * Metodo utilizzato per indicare se è presente la costola del libro 
 *
 */
ImageViewer.method('imgTot', function() 
{
  var numImg = 0;

  readBook = tracciatoXml.getElementsByTagName("mx-libro:readBook")[0];
  if (readBook != null)
  {
    immagini = readBook.getElementsByTagName("mx-libro:immagini")[0];
    if (immagini != null)
    {
      if (immagini.attributes != null)
      {
        if (immagini.attributes.getNamedItem("numImg") != null)
        {
          numImg = immagini.attributes.getNamedItem("numImg").value;
        }
      }
    }
  }
  return parseInt(numImg);
});

/**
 * Metodo utilizzato per leggere le informazioni relative alla singola pagina
 *
 */
ImageViewer.method('getImmagine', function(posizione) 
{
  var immagine;

  readBook=tracciatoXml.getElementsByTagName("mx-libro:readBook")[0];
  if (readBook != null)
  {
    immagini=readBook.getElementsByTagName("mx-libro:immagini")[0];
    if (immagini != null)
    {
      if (immagini.getElementsByTagName("immagine")[0] != null)
      {
        if (immagini.getElementsByTagName("immagine")[posizione] != null)
        {
          immagine = immagini.getElementsByTagName("immagine")[posizione];
        }
      }
    }
  }
  return immagine;
});

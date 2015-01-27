// Error reporting - this helps us fix errors quickly
function logError(description,page,line) {
    if (typeof(archive_analytics) != 'undefined') {
        var values = {
            'bookreader': 'error',
            'description': description,
            'page': page,
            'line': line,
            'itemid': 'birdbookillustra00reedrich',
            'subPrefix': 'birdbookillustra00reedrich',
            'server': 'ia600200.us.archive.org',
            'bookPath': '/4/items/birdbookillustra00reedrich/birdbookillustra00reedrich'
        };

        // if no referrer set '-' as referrer
        if (document.referrer == '') {
            values['referrer'] = '-';
        } else {
            values['referrer'] = document.referrer;
        }
        
        if (typeof(br) != 'undefined') {
            values['itemid'] = br.bookId;
            values['subPrefix'] = br.subPrefix;
            values['server'] = br.server;
            values['bookPath'] = br.bookPath;
        }
        
        var qs = archive_analytics.format_bug(values);

        var error_img = new Image(100,25);
        error_img.src = archive_analytics.img_src + "?" + qs;
    }

    return false; // allow browser error handling so user sees there was a problem
}
window.onerror=logError;
	
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
  this.titleLeaf = 5;
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

ImageViewer.method('onePageGetAutofitHeight', function() {
  if (BrowserDetect.browser=='Explorer' && BrowserDetect.version==8){
    if ($('#BRcontainer').attr('clientHeight')==0){
      var top = 0;
      var botton = 0;
      var height = 0;
      top += $('#header').attr('clientHeight')+20;
      botton += $('#footer').attr('clientHeight')+20;
      height += $('body').attr('clientHeight');
      height -= top;
      height -= botton;
      document.getElementById("BRcontainer").style.height=height+"px";
    }
  }
    return (this.getMedianPageSize().height + 0.0) / ($('#BRcontainer').attr('clientHeight') - this.padding * 2); // make sure a little of adjacent pages show
});

/**
 * Questo metodo viene utilizzato per indicare la larghezza dell'immagine
 */
ImageViewer.method('getPageWidth', function(index)
{
  //  ImageViewer.log('getPageWidth: index: '+index+' width:'+this.width);
  immagine = this.getImmagine(index);
  if (this.isCostola())
  {
    if (this.mode==2 || this.mode ==3)
    {
      immagine = this.getImmagine(index+1);
    }
  }
  if (immagine == undefined){
    return 0;
  } else {
    return parseInt(immagine.getElementsByTagName("larghezza")[0].childNodes[0].nodeValue);
  }
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

  if (immagine == undefined){
    return 0;
  } else {
    return parseInt(immagine.getElementsByTagName("altezza")[0].childNodes[0].nodeValue);
  }
});

/**
 * Questo metodo viene utilizzato per indicare se l'immagine � da 
 * visualizzare sul lato destro o sinistro quando sono affinacate
 */
ImageViewer.method('canRotatePage', function(index)
{
//	ImageViewer.log('canRotatePage: index: '+index);

    return 'jp2' == this.imageFormat; // Assume single format for now
//	return true;
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

// Get a rectangular region out of a page
ImageViewer.method('getRegionURI', function(index, reduce, rotate, sourceX, sourceY, sourceWidth, sourceHeight) {

    // Map function arguments to the url keys
    var urlKeys = ['n', 'r', 'rot', 'x', 'y', 'w', 'h'];
    var page = '';
    for (var i = 0; i < arguments.length; i++) {
        if ('undefined' != typeof(arguments[i])) {
            if (i > 0 ) {
                page += '_';
            }
            page += urlKeys[i] + arguments[i];
        }
    }
    
    var itemPath = this.bookPath.replace(new RegExp('/'+this.subPrefix+'$'), ''); // remove trailing subPrefix
    
    return 'http://'+this.server+'/BookReader/BookReaderImages.php?id=' + this.bookId + '&itemPath=' + itemPath + '&server=' + this.server + '&subPrefix=' + this.subPrefix + '&page=' +page + '.jpg';
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
 * Questo metodo viene utilizzato per indicare se l'immagine � da 
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
 * Questo metodo viene utilizzato per indicare il numero della 
 * pagina che viene visualizzata
 */
ImageViewer.method('getPageNum', function(index)
{
//	ImageViewer.log('getPageNum: index: '+index);

	return index+1;
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
 * Questa funzione restituisce gli indici a sinistra ea destra per la 
 * diffusione visibile all'utente che contiene il dato indice. I valori 
 * di ritorno pu� essere null se non esiste una pagina di fronte o 
 * l'indice non � valido.
 */
ImageViewer.method('getSpreadIndices', function(pindex) 
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

// Remove the page number assertions for all but the highest index page with
// a given assertion.  Ensures there is only a single page "{pagenum}"
// e.g. the last page asserted as page 5 retains that assertion.
ImageViewer.method('uniquifyPageNums', function() {
    var seen = {};
    
    for (var i = br.pageNums.length - 1; i--; i >= 0) {
        var pageNum = br.pageNums[i];
        if ( !seen[pageNum] ) {
            seen[pageNum] = true;
        } else {
            br.pageNums[i] = null;
        }
    }

});

ImageViewer.method('cleanupMetadata', function() {
    br.uniquifyPageNums();
});

// getEmbedURL
//________
// Returns a URL for an embedded version of the current book
ImageViewer.method('getEmbedURL', function(viewParams) {
    // We could generate a URL hash fragment here but for now we just leave at defaults
    var url = 'http://' + window.location.host + '/stream/'+this.bookId;
    if (this.subPrefix != this.bookId) { // Only include if needed
        url += '/' + this.subPrefix;
    }
    url += '?ui=embed';
    if (typeof(viewParams) != 'undefined') {
        url += '#' + this.fragmentFromParams(viewParams);
    }
    return url;
});

// getEmbedCode
//________
// Returns the embed code HTML fragment suitable for copy and paste
ImageViewer.method('getEmbedCode', function(frameWidth, frameHeight, viewParams) {
    return "<iframe src='" + this.getEmbedURL(viewParams) + "' width='" + frameWidth + "' height='" + frameHeight + "' frameborder='0' ></iframe>";
});

// getOpenLibraryRecord
ImageViewer.method('getOpenLibraryRecord', function(callback) {
    // Try looking up by ocaid first, then by source_record
    
    var self = this; // closure
    
    var jsonURL = self.olHost + '/query.json?type=/type/edition&*=&ocaid=' + self.bookId;
    $.ajax({
        url: jsonURL,
        success: function(data) {
            if (data && data.length > 0) {
                callback(self, data[0]);
            } else {
                // try sourceid
                jsonURL = self.olHost + '/query.json?type=/type/edition&*=&source_records=ia:' + self.bookId;
                $.ajax({
                    url: jsonURL,
                    success: function(data) {
                        if (data && data.length > 0) {
                            callback(self, data[0]);
                        }
                    },
                    dataType: 'jsonp'
                });
            }
        },
        dataType: 'jsonp'
    });
});

ImageViewer.method('buildInfoDiv', function(jInfoDiv) {
    // $$$ it might make more sense to have a URL on openlibrary.org that returns this info

    var escapedTitle = BookReader.util.escapeHTML(this.bookTitle);
    var domainRe = /(\w+\.(com|org))/;
    var domainMatch = domainRe.exec(this.bookUrl);
    var domain = this.bookUrl;
    if (domainMatch) {
        domain = domainMatch[1];
    }
       
    // $$$ cover looks weird before it loads
    jInfoDiv.find('.BRfloatCover').append([
                    '<div style="height: 140px; min-width: 80px; padding: 0; margin: 0;"><a href="', this.bookUrl, '"><img src="http://archive.org/download/', this.bookId, '/page/cover_t.jpg" alt="' + escapedTitle + '" height="140px" /></a></div>'].join('')
    );

    jInfoDiv.find('.BRfloatMeta').append([
                    // $$$ description
                    //'<p>Published ', this.bookPublished,
                    //, <a href="Open Library Publisher Page">Publisher name</a>',
                    //'</p>',
                    //'<p>Written in <a href="Open Library Language page">Language</a></p>',
                    '<h3>Other Formats</h3>',
                    '<ul class="links">',
                        '<li><a href="http://archive.org/download/', this.bookId, '/', this.subPrefix, '.pdf">PDF</a><span>|</span></li>',
                        '<li><a href="http://archive.org/download/', this.bookId, '/', this.subPrefix, '_djvu.txt">Plain Text</a><span>|</span></li>',
                        '<li><a href="http://archive.org/download/', this.bookId, '/', this.subPrefix, '_daisy.zip">DAISY</a><span>|</span></li>',
                        '<li><a href="http://archive.org/download/', this.bookId, '/', this.subPrefix, '.epub">ePub</a><span>|</span></li>',
                        '<li><a href="https://www.amazon.com/gp/digital/fiona/web-to-kindle?clientid=IA&itemid=', this.bookId, '&docid=', this.subPrefix, '">Send to Kindle</a></li>',
                    '</ul>',
                    '<p class="moreInfo"><span></span>More information on <a href="'+ this.bookUrl + '">' + domain + '</a>  </p>'].join('\n'));
                    
    jInfoDiv.find('.BRfloatFoot').append([
                '<span>|</span>',                
                '<a href="http://openlibrary.org/contact" class="problem">Report a problem</a>',
    ].join('\n'));
                
    if (domain == 'archive.org') {
        jInfoDiv.find('.BRfloatMeta p.moreInfo span').css(
            {'background': 'url(http://archive.org/favicon.ico) no-repeat', 'width': 22, 'height': 18 }
        );
    }
    
    jInfoDiv.find('.BRfloatTitle a').attr({'href': this.bookUrl, 'alt': this.bookTitle}).text(this.bookTitle);
    var bookPath = (window.location + '').replace('#','%23');
    jInfoDiv.find('a.problem').attr('href','http://openlibrary.org/contact?path=' + bookPath);

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

  return immagine.getElementsByTagName(prefixXml+"nomenclatura")[0].childNodes[0].nodeValue;
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
//alert("5");
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
 * Metodo utilizzato per indicare se � presente la costola del libro 
 *
 */
ImageViewer.method('isCostola', function() 
{
  var isCostola = false;

  readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
  if (readBook != null)
  {
    immagini = readBook.getElementsByTagName(prefixXml+"immagini")[0];
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
 * Metodo utilizzato per indicare se � presente la costola del libro 
 *
 */
ImageViewer.method('imgTot', function() 
{
  var numImg = 0;

  readBook = tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
  if (readBook != null)
  {
    immagini = readBook.getElementsByTagName(prefixXml+"immagini")[0];
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

  readBook=tracciatoXml.getElementsByTagName(prefixXml+"readBook")[0];
  if (readBook != null)
  {
    immagini=readBook.getElementsByTagName(prefixXml+"immagini")[0];
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

function OLAuth() {
    this.olConnect = false;
    this.loanUUID = false;
    this.permsToken = false;
    
    var cookieRe = /;\s*/;
    var cookies = document.cookie.split(cookieRe);
    var length = cookies.length;
    var i;
    for (i=0; i<length; i++) {
        if (0 == cookies[i].indexOf('br-loan-' + br.bookId)) {
            this.loanUUID = cookies[i].split('=')[1];
        }
        if (0 == cookies[i].indexOf('loan-' + br.bookId)) {
            this.permsToken = cookies[i].split('=')[1];
        }
        
        // Set olHost to use if passed in
        if (0 == cookies[i].indexOf('ol-host')) {
            br.olHost = 'http://' + unescape(cookies[i].split('=')[1]);
        }
    }

    this.authUrl = br.olHost + '/ia_auth/' + br.bookId;

    return this;
}

OLAuth.prototype.init = function() {
    var htmlStr =  'Checking loan status with Open Library';

    this.showPopup("#F0EEE2", "#000", htmlStr, 'Please wait as we check the status of this book...');
    var authUrl = this.authUrl+'?rand='+Math.random();
    if (false !== this.loanUUID) {
        authUrl += '&loan='+this.loanUUID
    }
    if (false !== this.permsToken) {
        authUrl += '&token='+this.permsToken
    }
    $.ajax({url:authUrl, dataType:'jsonp', jsonpCallback:'olAuth.initCallback'});
}

OLAuth.prototype.showPopup = function(bgColor, textColor, msg, resolution) {
    this.popup = document.createElement("div");
    $(this.popup).css({
        position: 'absolute',
        top:      '50px',
        left:     ($('#BookReader').attr('clientWidth')-400)/2 + 'px',
        width:    '400px',
        padding:  "15px",
        border:   "3px double #999999",
        zIndex:   3,
        textAlign: 'center',
        backgroundColor: bgColor,
        color: textColor
    }).appendTo('#BookReader');

    this.setPopupMsg(msg, resolution);

}

OLAuth.prototype.setPopupMsg = function(msg, resolution) {
    this.popup.innerHTML = ['<p><strong>', msg, '</strong></p><p>', resolution, '</p>'].join('\n');
}

OLAuth.prototype.showError = function(msg, resolution) {
   $(this.popup).css({
        backgroundColor: "#fff",
        color: "#000"
    });

    this.setPopupMsg(msg, resolution);
}

OLAuth.prototype.initCallback = function(obj) {
    if (false == obj.success) {
        if (br.isAdmin) {
            ret = confirm("We couldn't authenticate your loan with Open Library, but since you are an administrator or uploader of this book, you can access this book for QA purposes. Would you like to QA this book?");
            if (!ret) {
                this.showError(obj.msg, obj.resolution)
            } else {
                br.init();
            }
        } else {
            this.showError(obj.msg, obj.resolution)
        }       
    } else {    
        //user is authenticated
        this.setCookie(obj.token);
        this.olConnect = true;
        this.startPolling();    
        br.init();
    }
}

OLAuth.prototype.callback = function(obj) {
    if (false == obj.success) {
        this.showPopup("#F0EEE2", "#000", obj.msg, obj.resolution);
        clearInterval(this.poller);
        this.ttsPoller = null;
    } else {
        this.olConnect = true;
        this.setCookie(obj.token);
    }
}

OLAuth.prototype.setCookie = function(value) {
    var date = new Date();
    date.setTime(date.getTime()+(10*60*1000));  //10 min expiry
    var expiry = date.toGMTString();
    var cookie = 'loan-'+br.bookId+'='+value;
    cookie    += '; expires='+expiry;
    cookie    += '; path=/; domain=.archive.org;';
    document.cookie = cookie;
    this.permsToken = value;
    
    //refresh the br-loan uuid cookie with current expiry, if needed
    if (false !== this.loanUUID) {
        cookie = 'br-loan-'+br.bookId+'='+this.loanUUID;
        cookie    += '; expires='+expiry;
        cookie    += '; path=/; domain=.archive.org;';
        document.cookie = cookie;
    }
}

OLAuth.prototype.deleteCookies = function() {
    var date = new Date();
    date.setTime(date.getTime()-(24*60*60*1000));  //one day ago
    var expiry = date.toGMTString();
    var cookie = 'loan-'+br.bookId+'=""';
    cookie    += '; expires='+expiry;
    cookie    += '; path=/; domain=.archive.org;';
    document.cookie = cookie;
    
    cookie = 'br-loan-'+br.bookId+'=""';
    cookie    += '; expires='+expiry;
    cookie    += '; path=/; domain=.archive.org;';
    document.cookie = cookie;
}

OLAuth.prototype.startPolling = function () {    
    var self = this;
    this.poller=setInterval(function(){
        if (!self.olConnect) {
          self.showPopup("#F0EEE2", "#000", 'Connection error', 'The BookReader cannot reach Open Library. This might mean that you are offline or that Open Library is down. Please check your Internet connection and refresh this page or try again later.');
          clearInterval(self.poller);
          self.ttsPoller = null;        
        } else {
          self.olConnect = false;
          //be sure to add random param to authUrl to avoid stale cache
          var authUrl = self.authUrl+'?rand='+Math.random();
          if (false !== self.loanUUID) {
              authUrl += '&loan='+self.loanUUID
          }
          if (false !== self.permsToken) {
              authUrl += '&token='+self.permsToken
          }

          $.ajax({url:authUrl, dataType:'jsonp', jsonpCallback:'olAuth.callback'});
        }
    },300000);   //five minute interval
}

br.cleanupMetadata();
if (br.olAuth) {
    var olAuth = new OLAuth();
    olAuth.init();
} else {
    br.init();
}


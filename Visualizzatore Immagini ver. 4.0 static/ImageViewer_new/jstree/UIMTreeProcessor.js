/*
 * UIMTreeProcessor Class
 * version: 1.0 (11-16-2010)
 * 
 * Copyright (c) 2010 Vlad Shamgin (uimonster.com)
 * 
 * @requires jQuery v1.3.2 or later
 * @requires jsTree 1.0-rc1 or later
 *
 * Examples and documentation at: http://uimonster.com
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */

function UIMTreeProcessor(data, treeEl) {
	this.data = data;
	this.treeEl = treeEl;
}

UIMTreeProcessor.prototype.initTree = function(data){
	this.treeEl
	.jstree({
		/*"themes": {
            "theme": "apple",
            "dots": false,
            "icons": true
            },*/
		"json_data" : {
			"data":data,
			"progressive_render":"true"
		},
		"plugins" : [ "themes", "ui", "json_data" ],
		"core":{"animation":0}

	})
	.bind("select_node.jstree", function(event, data){
		if(data.rslt.obj.attr("href")!=undefined){
			var _href = data.rslt.obj.attr("href");
            if ((_href.match("^javascript")=="javascript")){
				eval("javascript:parent.document.getElementById('wait').style.display='block';");
				eval(data.rslt.obj.attr("href"));
				eval("javascript:parent.toggle_sidebar();");
            }else{
				window.open(data.rslt.obj.attr("href"));
            }
		}
	});
/*
	.delegate("a", "click",function(event, data){
		alert("prova");
	});
*/
}

UIMTreeProcessor.prototype.doProcess = function(){
	//Find root:
	var _root = $(this.data).children(':first-child');
	var _a_feed = new Array();

	this.vsTraverse($(_root), _a_feed);

	var _treedata = [{"data":_root[0].nodeName,"children":_a_feed, "state":"open"}];
	this.initTree(_treedata);
}

UIMTreeProcessor.prototype.vsTraverse = function(node, arr){
	var libri = $(node).children();

	for(var i=0; i<libri.length; i++){
		var titolo = undefined;
		var href = undefined;
		var vsArr = undefined;

		for(var j=0; j<libri[i].attributes.length; j++){
			if(libri[i].attributes[j].nodeName=="titolo"){
				titolo=libri[i].attributes[j].nodeValue;
			}else if(libri[i].attributes[j].nodeName=="ns1:titolo"){
				titolo=libri[i].attributes[j].nodeValue;
			}
			if(libri[i].attributes[j].nodeName=="href"){
				href=libri[i].attributes[j].nodeValue;
			}else if(libri[i].attributes[j].nodeName=="ns1:href"){
				href=libri[i].attributes[j].nodeValue;
			}
		}

		if ($(libri[i]).children().length>0){
			vsArr = scanChildren($(libri[i]).children(), arr);
		}

		if (vsArr != undefined && href != undefined){
			arr.push({"data":{"title":titolo,"children":vsArr, "state":"open", "icon": "./jstree/book-icon.png"}, "attr" : { "href" : href}});
		} else if (vsArr != undefined && href == undefined){
			arr.push([{"data":titolo ,"children":vsArr, "state":"open"}]);
		} else {
			arr.push([{"data":titolo ,"state":"open"}]);
		} 
	}
}

function scanChildren(node, arr){
	var vsArr = new Array();

	for(var j=0; j<node.length; j++){
		var titolo = undefined;
		var href = undefined;
		var children = undefined;
		for(var v=0; v<node[j].attributes.length; v++){
			if(node[j].attributes[v].nodeName=="titolo"){
				titolo=node[j].attributes[v].nodeValue;
			}else if(node[j].attributes[v].nodeName=="ns1:titolo"){
				titolo=node[j].attributes[v].nodeValue;
			}
			if(node[j].attributes[v].nodeName=="href"){
				href = node[j].attributes[v].nodeValue;
			}else if(node[j].attributes[v].nodeName=="ns1:href"){
				href = node[j].attributes[v].nodeValue;
			}
		}
		if (titolo == undefined){
			titolo = node[j].textContent;
		}
		if ($(node[j]).children().length>0){
			children = scanChildren($(node[j]).children(), arr);
		}

		if (children != undefined && href != undefined){
			vsArr.push({"data":{"title":titolo,"children":children, "state":"open", "icon": "./jstree/book-icon.png"}, "attr" : { "href" : href}});
		} else if (children != undefined && href == undefined){
			vsArr.push([{"data":titolo ,"children":children, "state":"open"}]);
		} else if (children == undefined && href != undefined){
			vsArr.push({"data":{"title": titolo, "icon": "./jstree/book-icon.png"}, "attr" : { "href" : href}});
		} else {
			vsArr.push([{"data":titolo ,"state":"open"}]);
		} 
		
	}
	return vsArr;
}

//UIMTreeProcessor.prototype.vsTraverse = function(node, arr){
//	var libri = $(node).children();
//	
//	for(var i=0; i<libri.length; i++){
//		var volumi = $(libri[i]).children();
//		var vsArr = new Array();
//		for(var j=0; j<volumi.length; j++){
//			for(var v=0; v<volumi[j].attributes.length; v++){
//				if(volumi[j].attributes[v].nodeName=="href"){
//					//vsArr.push(volumi[j].textContent);
//					vsArr.push({"data":{"title": volumi[j].textContent, "icon": "./jstree/book-icon.png"}, "attr" : { "href" : volumi[j].attributes[v].nodeValue}});
//				}
//			}
//			//vsArr.push(libri[i].firstChild.textContent);
//			//vsArr.push([{"data":"Attributes "+"["+volumi[j].nodeValue+"]", attr : { "class" : "uim_attr"}}]);
//		}
//		
//		for(var j=0; j<libri[i].attributes.length; j++){
//			if(libri[i].attributes[j].nodeName=="titolo"){
//				var titolo=libri[i].attributes[j].nodeValue;
//			}
//			if(libri[i].attributes[j].nodeName=="href"){
//				var href=libri[i].attributes[j].nodeValue;
//			}
//		}
//		if(volumi.length>0){
//			arr.push([{"data":titolo ,"children":vsArr, "state":"open"}]);
//		}else{
//			arr.push({"data":{"title":titolo, "icon": "./jstree/book-icon.png"}, "attr" : { "href" : href}});
//		}
//	}
//}


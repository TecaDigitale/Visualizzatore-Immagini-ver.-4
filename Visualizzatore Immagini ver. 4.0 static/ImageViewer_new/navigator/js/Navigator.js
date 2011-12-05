function buildTree()
{
   	//create a new tree:
   	tree = new YAHOO.widget.TreeView("NavigatorContent");

   	//turn dynamic loading on for entire tree: currentIconMode = 1
   	tree.setDynamicLoad(loadNodeData, 1);
   
   	//get root node for tree:
   	var root = tree.getRoot();
	
   	//add child nodes for tree; our top level nodes are
   	sUrl = "/ImageViewer/NavigatorLoader";
   	
    //prepare our callback object
    var callback = {
        //if our XHR call is successful, we want to make use
        //of the returned data and create first level nodes.
        success: function(oResponse) 
        {
    		responseText = oResponse.responseText;
    		
    		if (responseText.indexOf("error") < 0) 
    		{
	            oResults = responseText.split("[]");
	
	            // loading the branches' labels
	            labels = oResults[0].split("|");
	            tooltips = [];
	            if (isNotNull(oResults[1]))
	                // loading the branches' tooltips
	            	tooltips = oResults[1].split("|");
	            links = [];
	            if (isNotNull(oResults[2])) 
	            {
	                // loading the branches' links            	
	            	links = oResults[2].split("|");
	            }
	            
			   	// This disables dynamic loading for the node.
			   	// Use the isLeaf property to force the leaf node presentation for a given node.
				for (var i=0, j=labels.length; i<j; i++) 
				{
		        	var tempNode = new YAHOO.widget.TextNode(labels[i], root, false);
		        	tempNode.id="CIAO";
		        	if (isNotNull(tooltips[i]))
		        		tempNode.title = tooltips[i];
		        	if (isNotNull(links[i])) 
		        	{
		        		tempNode.href = 'javascript:loadBookReader(\'' + links[i] + '\')';
		        	}
			   	}
	
			   	//render tree with these toplevel nodes; all descendants of these nodes
			   	//will be generated as needed by the dynamic loader.
			   	tree.draw();
    		}
    		else 
    		{
    			showErrorMessage("(Errore creazione albero navigazione) " + responseText.substring(responseText.indexOf("|")+1));
    		}
        },
        
        //if our XHR call is not successful, we want to
        //fire the TreeView callback and let the Tree
        //proceed with its business.
        failure: function(oResponse) {
            alert(oResponse.responseText);
        },
        
        //timeout -- if more than 7 seconds go by, we'll abort
        //the transaction and assume there are no children:
        timeout: 7000
    };
    
    //With our callback object ready, it's now time to 
    //make our XHR call using Connection Manager's
    //asyncRequest method:
    YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
}


function loadNodeData(node, fnLoadComplete)  {
    
    //We'll create child nodes based on what we get back when we
    //use Connection Manager to pass the text label of the 
    //expanding node to the Yahoo!
    //Search "related suggestions" API.  Here, we're at the 
    //first part of the request -- we'll make the request to the
    //server.  In our Connection Manager success handler, we'll build our new children
    //and then return fnLoadComplete back to the tree.
    
    //Get the node's label and urlencode it; this is the word/s
    //on which we'll search for related words:
    var nodeLabel = encodeURI(node.label);
    xmlPath = nodeLabel + "|";
    // the xml path for the search into the source xml is being built
    /* 
     * 
     * 
     * 
     */
    nodeDepth = node.depth;
    parentNode = node.parent;
    while (nodeDepth>=0) {
    	if (parentNode != null && (typeof(parentNode.label) != "undefined")) {
    		xmlPath += ((parentNode.label) + "|");
    		parentNode = parentNode.parent;
    	}
    	nodeDepth--;
    }
    
    //prepare URL for XHR request:
    var sUrl = "/ImageViewer/NodeLoader?xmlPath="+xmlPath;
    alert(sUrl);
    
    //prepare our callback object
    var callback = {
    
        //if our XHR call is successful, we want to make use
        //of the returned data and create child nodes.
        success: function(oResponse) {
    		responseText = oResponse.responseText;
    		
    		if (responseText.indexOf("error") < 0) {
	            var oResults = responseText.split("[]");
	            
	            // loading the branches' labels            
	            labels = oResults[0].split("|");
	            tooltips = [];
	            if (isNotNull(oResults[1]))
	                // loading the branches' tooltips            	
	            	tooltips = oResults[1].split("|");
	            links = [];
	            if (isNotNull(oResults[2]))
	                // loading the branches' links            	
	            	links = oResults[2].split("|");
	        
	            //Result is an array if more than one result
	        	for (var i=0, j=labels.length; i<j; i++) {
	                var tempNode = new YAHOO.widget.TextNode(labels[i], node, false);
		        	if (isNotNull(tooltips[i]))
		        		tempNode.title = tooltips[i];
		        	if (isNotNull(links[i]))
		        		var encodedLink = escape(links[i]);
		        		tempNode.href = 'javascript:loadBookReader(\'' + encodedLink + '\')';
	            }
	                                
	            //When we're done creating child nodes, we execute the node's
	            //loadComplete callback method which comes in via the argument
	            //in the response object (we could also access it at node.loadComplete,
	            //if necessary):
	            oResponse.argument.fnLoadComplete();
    		} else {
    			showErrorMessage("(Errore creazione nodo del'albero) " + responseText.substring(responseText.indexOf("|")+1));
    		}
        },
        
        //if our XHR call is not successful, we want to
        //fire the TreeView callback and let the Tree
        //proceed with its business.
        failure: function(oResponse) {
            YAHOO.log("Failed to process XHR transaction.", "info", "example");
            oResponse.argument.fnLoadComplete();
        },
        
        //our handlers for the XHR response will need the same
        //argument information we got to loadNodeData, so
        //we'll pass those along:
        argument: {
            "node": node,
            "fnLoadComplete": fnLoadComplete
        },
        
        //timeout -- if more than 7 seconds go by, we'll abort
        //the transaction and assume there are no children:
        timeout: 7000
    };
    
    //With our callback object ready, it's now time to 
    //make our XHR call using Connection Manager's
    //asyncRequest method:
    YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
}

/*--------------------------------------------------|
function Node(id, pid, name, url, title, target, icon, iconOpen, open) 
// Tree object
function dTree(objName) 
  }
  this.icon = {
  this.obj = objName;
// Adds a new node to the node array
dTree.prototype.add = function(id, pid, name, url, title, target, icon, iconOpen, open) 
// Open/close all nodes
dTree.prototype.openAll = function() 
dTree.prototype.closeAll = function() 
// Outputs the tree to the page
dTree.prototype.toString = function() 
// Creates the tree structure
dTree.prototype.addNode = function(pNode) 
  for (n; n<this.aNodes.length; n++) 
      if (cn._hc && !cn._io && this.config.useCookies) 
      if (!this.config.folderLinks && cn._hc) 
      if (this.config.useSelection && cn.id == this.selectedNode && !this.selectedFound) 
      str += this.node(cn, n);
    }
// Creates the node icon, url and text
dTree.prototype.node = function(node, nodeId) 
  if (this.config.useIcons) 
    if (!node.iconOpen) 
    if (this.root.id == node.pid) 
    str += '<img id="i' + this.obj + nodeId + '" src="' + this.config.urlWeb + ((node._io) ? node.iconOpen : node.icon) + '" alt="" title="" />';
  if (node.url) 
  str += node.name;
  if (node.url || ((!this.config.folderLinks || !node.url) && node._hc)) 
  str += '</div>';
  if (node._hc) 
  this.aIndent.pop();
  return str;
// Adds the empty and line icons
dTree.prototype.indent = function(node, nodeId) 
  var str = '';
    if (node._hc) 
// Checks if a node has any children and if it is the last sibling
dTree.prototype.setCS = function(node) 
// Returns the selected node
dTree.prototype.getSelected = function() 
// Highlights the selected node
dTree.prototype.s = function(id) 
// Toggle Open or close
dTree.prototype.o = function(id) 
// Open or close all nodes
dTree.prototype.oAll = function(status) 
// Opens the tree to a specific node
dTree.prototype.openTo = function(nId, bSelect, bFirst) 
  var cn=this.aNodes[nId];
// Closes all nodes on the same level as certain node
dTree.prototype.closeLevel = function(node) 
// Closes all children of a node
dTree.prototype.closeAllChildren = function(node) 
// Change the status of a node(open or closed)
dTree.prototype.nodeStatus = function(status, id, bottom) 
// [Cookie] Clears a cookie
dTree.prototype.clearCookie = function() 
// [Cookie] Sets value in a cookie
dTree.prototype.setCookie = function(cookieName, cookieValue, expires, path, domain, secure) 
// [Cookie] Gets a value from a cookie
dTree.prototype.getCookie = function(cookieName) 
// [Cookie] Returns ids of open nodes as a string
dTree.prototype.updateCookie = function() 
// [Cookie] Checks if a node id is in a cookie
dTree.prototype.isOpen = function(id) 
// If Push and pop is not implemented by the browser
if (!Array.prototype.push) 
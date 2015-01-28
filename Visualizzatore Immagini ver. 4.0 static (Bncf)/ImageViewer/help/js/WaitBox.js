function waitBoxStop()
{
  var div = document.getElementById("waitDiv");
  if (div != null)
  {
    div.style.display="none";
  }
}

function waitBoxStart()
{
  if (document.getElementById("waitDiv") != null)
  {
    document.getElementById("waitDiv").style.display="";
  }
}
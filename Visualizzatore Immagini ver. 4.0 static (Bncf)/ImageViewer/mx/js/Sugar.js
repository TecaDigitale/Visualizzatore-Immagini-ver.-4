/**
 * Questo metodo viene utilizzato per aggiungere i metodi alla classe
 */
Function.prototype.method = function (name, func)
{
    this.prototype[name] = func;
    return this;
};

/**
 * Questo metodo viene utilizzato per loggare la classe
Function.prototype.log = function(msg)
{
	if (typeof console == 'undefined')
		$(this).text(msg);
	else
		console.log(msg);
	return this;
};
 */

/**
 *Questo metodo viene utilizzato per derivare dalla classe di origine
 */
Function.method('inherits', function (parent)
{
    var d = {};
    var p = (this.prototype = new parent());

    this.method('uber', function uber(name)
    {
        if (!(name in d))
        {
            d[name] = 0;
        }
        var f, r, t = d[name];
        var v = parent.prototype;

        if (t)
        {
            while (t)
            {
                v = v.constructor.prototype;
                t -= 1;
            }
            f = v[name];
        }
        else
        {
            f = p[name];
//            alert("f: "+f);
            if (f==this[name])
            {
                f = v[name];
            }
//            alert("f: "+f);
        }
        d[name] += 1;
        try
        {
          r = f.apply(this, Array.prototype.slice.apply(arguments, [1]));
        }
        catch(e)
        {
         var a = [];
	    for(var i = 1; i < arguments.length; i++) {
	      a.push(arguments[i]);
	    }
//	    alert(a);
    	f.apply(this, arguments);
           alert(e.description);
        }
//	    alert("4");
        d[name] -= 1;
        return r;
    });
    return this;
});

/**
 *
 */
Function.method('swiss', function(parent)
{
    for (var i=1; i<arguments.length; i+=1)
    {
        var name=arguments[i];
        this.prototype[name]=parent.prototype[name];
    }
    return this;
});

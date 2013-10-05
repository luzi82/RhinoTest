( function (x) {
	var ret = {
		get a() { return x.b(); },
		get b() { return ret.a+1; },
		get aa() { return x.bb; },
	};
	return ret;
} )

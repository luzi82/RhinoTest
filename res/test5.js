( function (x) {
	var ret = {
		get a() { return x.b(); },
		get b() { return x.b()+"asdf"; },
		get c() { return "asdf"; },
	};
	return ret;
} )

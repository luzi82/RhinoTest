package com.luzi82.rhinotest;

import java.io.FileReader;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RequireJsTest {

	/**
	 * still fail...
	 * leave this alone for future solve
	 * @throws Exception
	 */
	@Ignore("not solved")
	@Test
	public void rjstest0() throws Exception {
		Context cx = Context.enter();
		cx.setOptimizationLevel(-1); // avoid the 64k limit... not too good...

		Scriptable scope = cx.initStandardObjects();
		
		cx.evaluateReader(scope,
				new FileReader("res/r.js"), "res/r.js", 0, null);

		Object result = cx.evaluateReader(scope,
				new FileReader("res/rjs_b.js"), "res/rjs_b.js", 0, null);

		Scriptable sa = (Scriptable) result;
		Callable hello_b = (Callable) sa.get("hello_b", sa);
		Assert.assertEquals("HelloWorld", hello_b.call(cx, scope, null, null));

		Context.exit();
	}

}

package com.luzi82.rhinotest;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class SimpleTest {

	@Test
	public void test() {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		cx.evaluateString(scope, "x=1", "<cmd>", 0, null);
		Object result = cx.evaluateString(scope, "x", "<cmd>", 0, null);
		Assert.assertEquals(1d, result);
		Context.exit();
	}

	static public class X {
		public String v() {
			return "Hello";
		}
	}

	@Test
	public void javaBind() {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		Object x = Context.javaToJS(new X(), scope);
		ScriptableObject.putProperty(scope, "x", x);
		Object result = cx.evaluateString(scope, "x.v()", "<cmd>", 0, null);
		result = Context.jsToJava(result, String.class);
		Assert.assertEquals("Hello", result);
		Context.exit();
	}

	@Test
	public void get() {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		cx.evaluateString(scope, "x=1", "<cmd>", 0, null);
		Object result = scope.get("x", scope);
		Assert.assertEquals(1d, result);
		Context.exit();
	}

}

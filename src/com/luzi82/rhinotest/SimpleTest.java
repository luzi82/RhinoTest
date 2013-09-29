package com.luzi82.rhinotest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Callable;
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

	@Test
	public void scope() {
		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();
		ScriptableObject.putProperty(scope, "x", 10);

		Scriptable innerScope = cx.newObject(scope);
		Assert.assertSame(scope, innerScope.getParentScope());

		Assert.assertEquals(10, scope.get("x", scope));
		Assert.assertEquals(10, scope.get("x", innerScope));
		Assert.assertEquals(10, scope.get("x", null));
		Assert.assertEquals(Scriptable.NOT_FOUND, innerScope.get("x", scope));
		Assert.assertEquals(Scriptable.NOT_FOUND,
				innerScope.get("x", innerScope));
		Assert.assertEquals(Scriptable.NOT_FOUND, innerScope.get("x", null));

		ScriptableObject.putProperty(innerScope, "y", 20);
		Assert.assertEquals(Scriptable.NOT_FOUND, scope.get("y", scope));
		Assert.assertEquals(Scriptable.NOT_FOUND, scope.get("y", innerScope));
		Assert.assertEquals(Scriptable.NOT_FOUND, scope.get("y", null));
		Assert.assertEquals(20, innerScope.get("y", scope));
		Assert.assertEquals(20, innerScope.get("y", innerScope));
		Assert.assertEquals(20, innerScope.get("y", null));

		Object result = cx.evaluateString(innerScope, "x", "<cmd>", 0, null);
		Assert.assertEquals(10, result);
		result = cx.evaluateString(innerScope, "y", "<cmd>", 0, null);
		Assert.assertEquals(20, result);

		Context.exit();
	}

	/**
	 * Test access obj property
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void test0() throws FileNotFoundException, IOException {
		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		Object result = cx.evaluateReader(scope,
				new FileReader("res/test0.js"), "res/test0.js", 0, null);
//		System.err.println(result.getClass().getName());

		Scriptable sa = (Scriptable) result;
		Assert.assertEquals(800, sa.get("width", sa));
		Assert.assertEquals(600, sa.get("height", sa));
		Assert.assertEquals("#ff7f7f7f", sa.get("backgroundColor", sa));

		Context.exit();
	}

	/**
	 * Test access property as function and exec
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void test1() throws FileNotFoundException, IOException {
		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		Object result = cx.evaluateReader(scope,
				new FileReader("res/test1.js"), "res/test1.js", 0, null);
//		System.err.println(result.getClass().getName());

		Scriptable sa = (Scriptable) result;
		Assert.assertEquals(800,((Callable) sa.get("width", sa)).call(cx, scope, null, null));
		Assert.assertEquals(600,((Callable) sa.get("height", sa)).call(cx, scope, null, null));
		Assert.assertEquals("#ff7f7f7f",((Callable) sa.get("backgroundColor", sa)).call(cx, scope, null, null));
		Assert.assertEquals(800,((Callable) sa.get("extra", sa)).call(cx, scope, null, null));

		Context.exit();
	}

	/**
	 * Test access property by getter
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void test2() throws FileNotFoundException, IOException {
		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		Object result = cx.evaluateReader(scope,
				new FileReader("res/test2.js"), "res/test2.js", 0, null);
//		System.err.println(result.getClass().getName());

		Scriptable sa = (Scriptable) result;
		Assert.assertEquals(800, sa.get("width", sa));
		Assert.assertEquals(600, sa.get("height", sa));
		Assert.assertEquals(800, sa.get("extra", sa));

		Context.exit();
	}

}

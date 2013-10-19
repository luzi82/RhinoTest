package com.luzi82.rhinotest;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;

public class CommonJsTest {

	@Test
	public void cjstest0() throws Exception {
		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		ModuleScriptProvider msp = new SoftCachingModuleScriptProvider(
				new MSP());

		RequireBuilder requireBuilder = new RequireBuilder();
		requireBuilder.setModuleScriptProvider(msp);
		// requireBuilder.setSandboxed(false);
		Require require = requireBuilder.createRequire(cx, scope);
		require.install(scope);

		Object result = cx.evaluateReader(scope,
				new FileReader("res/cjs_b.js"), "res/rjs_b.js", 0, null);

		Scriptable sa = (Scriptable) result;
		Callable hello_b = (Callable) sa.get("hello_b", sa);
		Assert.assertEquals("Hello A", hello_b.call(cx, scope, null, null));

		Context.exit();
	}

	public static class MSP implements ModuleSourceProvider {

		@Override
		public ModuleSource loadSource(String moduleId, Scriptable paths,
				Object validator) throws IOException, URISyntaxException {
			// return null;
			Reader reader = new FileReader("res/" + moduleId);
			return new ModuleSource(reader, null, URI.create(moduleId),
					URI.create(""), validator);
		}

		@Override
		public ModuleSource loadSource(URI uri, URI baseUri, Object validator)
				throws IOException, URISyntaxException {
			// TODO Auto-generated method stub
			return null;
		}

	}

}

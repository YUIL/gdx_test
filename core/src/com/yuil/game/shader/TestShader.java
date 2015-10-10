package com.yuil.game.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TestShader implements Shader {
	public static class TestColorAttribute extends ColorAttribute {
		public final static String DiffuseUAlias = "diffuseUColor";
		public final static long DiffuseU = register(DiffuseUAlias);

		public final static String DiffuseVAlias = "diffuseVColor";
		public final static long DiffuseV = register(DiffuseVAlias);

		static {
			Mask = Mask | DiffuseU | DiffuseV;
		}

		public TestColorAttribute(long type, float r, float g, float b, float a) {
			super(type, r, g, b, a);
			// TODO Auto-generated constructor stub
		}

	}

	ShaderProgram program;
	Camera camera;
	RenderContext context;
	int u_projViewTrans;
	int u_worldTrans;
	int u_colorU;
	int u_colorV;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		program.dispose();

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		String vert = Gdx.files.internal("test.vertex.glsl").readString();
		String frag = Gdx.files.internal("test.fragment.glsl").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled()) {
			throw new GdxRuntimeException(program.getLog());
		}
		u_projViewTrans = program.getUniformLocation("u_projViewTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
		u_colorU = program.getUniformLocation("u_colorU");
		u_colorV = program.getUniformLocation("u_colorV");
	}

	@Override
	public int compareTo(Shader other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		// TODO Auto-generated method stub

		return instance.material.has(TestColorAttribute.DiffuseU|TestColorAttribute.DiffuseV);
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		// TODO Auto-generated method stub
		this.camera = camera;
		this.context = context;
		program.begin();
		program.setUniformMatrix(u_projViewTrans, camera.combined);
		context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		// TODO Auto-generated method stub
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		Color colorU = ((ColorAttribute) renderable.material
				.get(TestColorAttribute.DiffuseU)).color;
		Color colorV = ((ColorAttribute) renderable.material
				.get(TestColorAttribute.DiffuseV)).color;
		program.setUniformf(u_colorU, colorU.r, colorU.g, colorU.b);
		program.setUniformf(u_colorV, colorV.r, colorV.g, colorV.b);
		renderable.mesh.render(program, renderable.primitiveType,
				renderable.meshPartOffset, renderable.meshPartSize);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		program.end();
	}

}

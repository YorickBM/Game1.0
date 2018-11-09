package engineTester;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import fontRendering.TextMaster;
import postProcessing.PostProcessing;

public class CommetentedStuff {
	
	/**
	 * 
	 * //systemFire.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 20, player.getPosition().z));
			//entity.increaseRotation(0, 1, 0);
			
			renderer.renderShadoMap(entities, sun);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
 * 			//render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			camera.getPosition().y += distance;
			
			//render Refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			
			// render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();
			
			multisampleFbo.bindFrameBuffer();
			
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			waterRenderer.render(waters, camera, sun);
			
			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());
			
			guiRenderer.render(guis);
			guiRenderer.render(InvGui);
			TextMaster.render();
	 */

}

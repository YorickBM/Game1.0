package rendering;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import Entitys.Entity;
import Models.TexturedModel;
import Shader.CombineShaderWithRenderer;
import Shader.StaticShaderLowPoly;
import display.Window;
import fbos.Attachment;
import fbos.Fbo;
import fbos.RenderBufferAttachment;
import fbos.TextureAttachment;
import lwjgl2.Matrix4f;
import lwjgl2.Vector4f;
import rendering.CameraLowPoly;
import rendering.ICamera;
import rendering.LightLowPoly;
import rendering.Renderer;
import terrains.TerrainLowPoly;
import utils.MyFile;
import utils.OpenGlUtils;
import water.WaterTileLowPoly;
import waterRendering.WaterRenderer;

public class RenderEngineLowPoly {

	private Matrix4f projectionMatrix;
	
	private static final float REFRACT_OFFSET = 1f;
	private static final float REFLECT_OFFSET = 0.1f;
	
	private static MyFile iconPath = new MyFile("resources/logo");
	
	public final Window window;
	private WaterRenderer waterRenderer;
	private Fbo reflectionFbo;
	private Fbo refractionFbo;
	
	private StaticShaderLowPoly shader;
	private Renderer renderer = new Renderer();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	/**
	 * Sets up the render engine and initializes the display.
	 * 
	 * @param fps
	 *            - The FPS cap.
	 * @param displayWidth
	 *            - The width of the display in pixels.
	 * @param displayHeight
	 *            - The height of the display in pixels.
	 */
	public RenderEngineLowPoly(int fps, int displayWidth, int displayHeight, boolean icon) {
		
		if(icon)
			this.window = Window.newWindow(displayWidth, displayHeight, fps).antialias(true).withIcon(iconPath).create();
		else
			this.window = Window.newWindow(displayWidth, displayHeight, fps).antialias(true).create();
	}
	
	/**
	 * Creates the FBOs for the reflection and refraction passes. These FBOs allow 
	 * the scene to be rendered to textures which can then be sampled in the shader.
	 * The dimensions of the refraction FBO have been scaled down for
	 * performance reasons, although this results in a slight decrease in
	 * quality.
	 * 
	 * The refraction FBO is set up with a texture depth attachment, so that the
	 * image of the depth buffer can be sampled in the shaders. This is not
	 * necessary for the reflection pass.
	 */
	
	public void ContinueRendering(int displayWidth, int displayHeight) 
	{
		this.waterRenderer = new WaterRenderer();
		this.refractionFbo = createWaterFbo(displayWidth / 2, displayHeight / 2, true);
		this.reflectionFbo = createWaterFbo(displayWidth, displayHeight, false);
	}
	
	/**
	 * 
	 * Initialize the Projection Matrix only once
	 * because we will be using the same Matrix every
	 * time.
	 * 
	 * @param shader
	 * 			- Loads the shader thats is being used to the Combiner class
	 */
	
	public void initProjectionMatrix(StaticShaderLowPoly shader) {
		this.shader = shader;
		CombineShaderWithRenderer.setShader(shader);
		CombineShaderWithRenderer.loadProjectionMatrix(projectionMatrix);
	}
	
	public void createProjectionMatrix(CameraLowPoly camera) {
		projectionMatrix = camera.getProjectionMatrix();
	}

	/**
	 * Carries out all the rendering for a frame. First the scene is rendered to
	 * the reflection texture and the refraction texture using the FBOs. This
	 * creates two images of the scene which can then be used to texture the
	 * water. The main render pass then takes place, rendering the scene
	 * (including the water) to the screen.
	 * 
	 * @param terrain
	 *            - The terrain in the scene.
	 * @param water
	 *            - The water in the scene.
	 * @param camera
	 *            - The scene's camera.
	 * @param light
	 *            - The light being used to illuminate the scene.
	 */
	public void render(WaterTileLowPoly water, ICamera camera, LightLowPoly light) {
		doMainRenderPass(water, camera, light);
	}
	
	public void render(TerrainLowPoly terrain, WaterTileLowPoly water, ICamera camera, LightLowPoly light) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		doReflectionPass(terrain, camera, light, water.getHeight());
		doRefractionPass(terrain, camera, light, water.getHeight());
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		doMainRenderPass(terrain, water, camera, light);
	}
	
	public void render(LightLowPoly sun, CameraLowPoly camera) {
		
		shader.start();
		CombineShaderWithRenderer.loadViewMatrix(camera);
		CombineShaderWithRenderer.loadLight(sun);
	 	
		renderer.render(entities);
	 	shader.stop();
	 	
	 	entities.clear();
	}
	
	/**
	 * Puts the entities at the correct places in
	 * the entity hashMap
	 * 
	 * @param entity
	 * 			- Get the model of the entity to check in wich List it needs to be!
	 */
	
	public void processEntity(Entity entity) {
		prepare();
		
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}


	/**
	 * @return The current display.
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * Deletes the FBOs and closes the display when the game closes.
	 */
	public void close() {
		shader.cleanUp();
		
		reflectionFbo.delete();
		refractionFbo.delete();
		waterRenderer.cleanUp();
		window.destroy();
	}
	
	/**
	 * Prepares for a rendering pass. The depth and colour buffers of the
	 * current framebuffer are cleared and a few other default settings are set.
	 */
	
	public void prepare() {
		/*
		 *  This line is critical for LWJGL's interoperation with GLFW's
		 *  OpenGL context, or any context that is managed externally.
		 *  LWJGL detects the context that is current in the current thread,
		 *  creates the GLCapabilities instance and makes the OpenGL
		 *  bindings available for use.
		 */
		GL.createCapabilities(); //(New in LWJGL3)
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1f);
		
		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting(true);
		OpenGlUtils.antialias(true);
	}
	
	/**
	 * Carries out the reflection pass, by rendering the scene to the reflection
	 * FBO. The camera is inverted before rendering to render the reflection of
	 * the scene. After rendering the camera is reverted to its original
	 * position. The clip plane used when rendering the scene ensures that only
	 * things above the water get rendered to the reflection texture, as things
	 * under the water shouldn't be getting reflected.
	 * 
	 * @param terrain
	 *            - The terrain in the scene.
	 * @param camera
	 *            - The scene's camera
	 * @param light
	 *            - The light in the scene.
	 * @param waterHeight
	 *            - The height of the water.
	 */
	private void doReflectionPass(TerrainLowPoly terrain, ICamera camera, LightLowPoly light, float waterHeight) {
		reflectionFbo.bindForRender(0);
		camera.reflect();
		prepare();
		camera.reflect();
		reflectionFbo.unbindAfterRender();
	}

	/**
	 * Renders the scene to the refraction FBO. The scene is rendered from the
	 * normal camera position, and the result is stored in the refraction
	 * texture. A clipping plane is used to ensure that only parts of the scene
	 * that are under the water are rendered to the refraction FBO.
	 * 
	 * @param terrain
	 *            - The terrain.
	 * @param camera
	 *            - The camera being used in the scene.
	 * @param light
	 *            - The scene's light.
	 * @param waterHeight
	 *            - The height of the water in the world.
	 */
	private void doRefractionPass(TerrainLowPoly terrain, ICamera camera, LightLowPoly light, float waterHeight) {
		refractionFbo.bindForRender(0);
		prepare();
		refractionFbo.unbindAfterRender();
	}

	/**
	 * Renders the entire scene (terrain and water) to the screen. No clip plane
	 * is used here, so that the entire scene is rendered. Both the terrain and
	 * water are rendered during this pass.
	 * 
	 * @param terrain
	 *            - The terrain in the scene.
	 * @param water
	 *            - The water in the scene.
	 * @param camera
	 *            - The camera.
	 * @param light
	 *            - The light.
	 */
	private void doMainRenderPass(WaterTileLowPoly water, ICamera camera, LightLowPoly light) {
		waterRenderer.render(water, camera, light, reflectionFbo.getColourBuffer(0), refractionFbo.getColourBuffer(0),
				refractionFbo.getDepthBuffer());
		
		//window.update();
	}
	
	private void doMainRenderPass(TerrainLowPoly terrain, WaterTileLowPoly water, ICamera camera, LightLowPoly light) {
		terrain.render(camera, light, new Vector4f(0, 0, 0, 0));
		waterRenderer.render(water, camera, light, reflectionFbo.getColourBuffer(0), refractionFbo.getColourBuffer(0),
				refractionFbo.getDepthBuffer());
		//window.update();
	}
	
	/**
	 * Clear the buffer (May not needed because is in rendering function)
	 */
	public void clearBuffers() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Swaps every other frame from buffer (May not needed because is in rendering function)
	 */
	public void swapBuffers() {
		window.swapBuffers();
	}
	
	/**
	 * 
	 * Updates the engine window so every
	 * frame the objects get rendered from
	 * camera perspective so it looks like
	 * the camera moves.
	 * 
	 */
	public void update() {
		window.update();
	}
	
	/**
	 * Sets up an FBO for one of the extra render passes. The FBO is initialised
	 * with a texture colour attachment, and can be initialised with either a
	 * render buffer or texture attachment for the depth buffer.
	 * 
	 * @param width
	 *            - The width of the FBO in pixels.
	 * @param height
	 *            - The height of the FBO in pixels.
	 * @param useTextureForDepth
	 *            - Whether the depth buffer attachment should be a texture or a
	 *            render buffer.
	 * @return The completed FBO.
	 */
	private static Fbo createWaterFbo(int width, int height, boolean useTextureForDepth) {
		Attachment colourAttach = new TextureAttachment(GL11.GL_RGBA8);
		Attachment depthAttach;
		if (useTextureForDepth) {
			depthAttach = new TextureAttachment(GL14.GL_DEPTH_COMPONENT24);
		} else {
			depthAttach = new RenderBufferAttachment(GL14.GL_DEPTH_COMPONENT24);
		}
		return Fbo.newFbo(width, height).addColourAttachment(0, colourAttach).addDepthAttachment(depthAttach).init();
	}

	public static float getRefractOffset() {
		return REFRACT_OFFSET;
	}

	public static float getReflectOffset() {
		return REFLECT_OFFSET;
	}

}

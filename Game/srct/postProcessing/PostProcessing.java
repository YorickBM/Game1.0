package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	
	private static ContrastChanger contrastChanger;
	private static BrightFilter brightFilter;
	
	private static HorizontalBlur hBloomBlur;
	private static VerticalBlur vBloomBlur;
	private static HorizontalBlur hBloomBlur2;
	private static VerticalBlur vBloomBlur2;
	
	private static CombineFilter combineFilter;
	
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	
	public static boolean usesBlur = false;
	public static boolean usesBloom = false;
	
	public static void init(Loader loader, int BlurEffectivnes, boolean useBlur, boolean useBloom){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		combineFilter = new CombineFilter();
		
		if(useBloom) {
			System.out.println("Activating Bloom...");
			usesBloom = true;
			brightFilter = new BrightFilter(Display.getWidth()/2, Display.getHeight()/2);
			hBloomBlur = new HorizontalBlur(Display.getWidth()/BlurEffectivnes, Display.getHeight()/BlurEffectivnes);
			vBloomBlur = new VerticalBlur(Display.getWidth()/BlurEffectivnes, Display.getHeight()/BlurEffectivnes);
			hBloomBlur2 = new HorizontalBlur(Display.getWidth()/2, Display.getHeight()/2);
			vBloomBlur2 = new VerticalBlur(Display.getWidth()/2, Display.getHeight()/2);
		}
		
		if(useBlur) {
			System.out.println("Activating Blur...");
			usesBlur = true;
			hBlur = new HorizontalBlur(Display.getWidth()/BlurEffectivnes, Display.getHeight()/BlurEffectivnes);
			vBlur = new VerticalBlur(Display.getWidth()/BlurEffectivnes, Display.getHeight()/BlurEffectivnes);
			hBlur2 = new HorizontalBlur(Display.getWidth()/2, Display.getHeight()/2);
			vBlur2 = new VerticalBlur(Display.getWidth()/2, Display.getHeight()/2);
		}
	}
	
	public static void doPostProcessing(int colorTexture, int brightTextures){
		start();
		//Render Blur
		if(usesBlur) {
			hBlur2.render(colorTexture);
			vBlur2.render(hBlur2.getOutputTexture());
			hBlur.render(vBlur2.getOutputTexture());
			vBlur.render(hBlur.getOutputTexture());
			contrastChanger.render(vBlur.getOutputTexture());
		}
		
		//Render Bloom
		if(usesBloom){
			hBloomBlur2.render(brightTextures);
			vBloomBlur2.render(hBloomBlur2.getOutputTexture());
			hBloomBlur.render(vBloomBlur2.getOutputTexture());
			vBloomBlur.render(hBloomBlur.getOutputTexture());
			combineFilter.render(colorTexture, vBloomBlur.getOutputTexture());
		}
			
		//Render normal scene if no Blur or Bloom is Applied
		if(!usesBloom && !usesBlur){
			contrastChanger.render(colorTexture);
		}
		end();
	}
	
	public static void cleanUp() {
		contrastChanger.cleanUp();
		combineFilter.cleanUp();
		
		if(usesBlur) {
			hBlur.cleanUp();
			vBlur.cleanUp();
			hBlur2.cleanUp();
			vBlur2.cleanUp();
		}
		
		if(usesBloom) {
			hBloomBlur.cleanUp();
			vBloomBlur.cleanUp();
			brightFilter.cleanUp();
		}
	}

	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}

package Textfied;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import TextfieldRenderer.Loader;
import TextfiedModels.RawModel;
import TextfiedModels.TexturedModel;
import TextfiedTextures.ModelTexture;
import TextfieldRenderer.Renderer;
import TextfieldShaders.StaticShader;

/*
 * You always need to render image (Variable isTextured does not function in FragmentShader)
 * This will be fixed as soon as possible (It shouldn't work wired :/)
 */

public class Textfield {
	
	private Loader loader = null;
	private Renderer renderer = null;
	private StaticShader shader = null;
	
	private RawModel TextfieldModel = null;
	private RawModel BorderModel = null;
	
	private TexturedModel TextfieldTexturedModel = null;
	private TexturedModel BorderTexturedModel = null;
	
	private ModelTexture TextfieldTextureImage = null;
	private ModelTexture BorderTextureImage = null;
	
	private String Text = ""; 
	private String BorderTexture = "";
	private String TextfieldTexture = "";
	
	private int id;
	
	private float Width = 0.5f;
	private float Height = 0.5f;
	private float WidthBorder = 0.6f;
	private float HeightBorder = 0.6f;
	
	private boolean Border = false;
	private boolean Texture = false;
	private boolean Rendered = false;
	
	private Vector3f ColorBorder = new Vector3f(0,0,0);
	private Vector3f Color = new Vector3f(1,1,1);
	
	private Vector2f Position = new Vector2f(0, 0);
	
	private float[] vertices = {            
            -this.Width,this.Height,0,   
            -this.Width,-this.Height,0,  
            this.Width,-this.Height,0,   
            this.Width,this.Height,0     
    };
	private float[] verticesBorder = {            
            -this.WidthBorder,this.HeightBorder,0,   
            -this.WidthBorder,-this.HeightBorder,0,  
            this.WidthBorder,-this.HeightBorder,0,   
            this.WidthBorder,this.HeightBorder,0   
    };
    private int[] indices = {
            0,1,3, 
            3,1,2   
    };
	private float[] textureCoords = {
			0,0,
			0,1,
			1,1,
			1,0
	};
    
    //Text Field constructors without Textures
	public Textfield(float Width, float Height, int id, Vector2f Position, Vector3f Color, String Text) {
		float[] newVertices = {-Width, Height, 0, -Width, -Height, 0, Width, -Height,0, Width, Height,0};
		
		this.Border = false;
		this.Width = Width;
		this.Height = Height;
		this.Text = Text;
		this.id = id;
		this.Position = Position;
		this.Color = Color;
		this.vertices = newVertices;
	}
	public Textfield(float Width, float Height, int id, Vector2f Position, Vector3f Color, String Text, float WidthBorder, float HeightBorder, Vector3f ColorBorder) {
		float y = Position.y;
		float x = (Position.x - 0.5f);
		float[] newVertices = {(-Width / 10) + x, (Height / 10) + y, 0, (-Width / 10) + x, (-Height / 10) + y, 0, (Width / 10) + x, (-Height / 10) + y,0, (Width / 10) + x, (Height / 10) + y,0};
		float[] newVerticesBorder = {((-Width - (WidthBorder / 4)) / 10) + x, ((Height + (HeightBorder / 2)) / 10) + y, 0, ((-Width - (WidthBorder / 4)) / 10) + x, ((-Height - (HeightBorder / 2)) / 10) + y, 0,
				((Width + (WidthBorder / 4)) / 10) + x, ((-Height - (HeightBorder / 2)) / 10) + y,0, ((Width + (WidthBorder / 4)) / 10) + x, ((Height + (HeightBorder / 2))  / 10) + y,0};
		
		this.Border = true;
		this.Width = Width;
		this.Height = Height;
		this.Text = Text;
		this.id = id;
		this.Position = Position;
		this.Color = new Vector3f(Color.x / 255, Color.y / 255, Color.z / 255);
		this.vertices = newVertices;
		
		this.ColorBorder = new Vector3f(ColorBorder.x / 255, ColorBorder.y / 255, ColorBorder.z / 255);
		this.WidthBorder = WidthBorder;
		this.HeightBorder = HeightBorder;
		this.verticesBorder = newVerticesBorder;
	}
	
	//Text Field constructors with Textures
	public Textfield(float Width, float Height, int id, Vector2f Position, Vector3f Color, String Text, String TextfieldTexture) {
		float[] newVertices = {-Width, Height, 0, -Width, -Height, 0, Width, -Height,0, Width, Height,0};
		
		this.Border = false;
		this.Width = Width;
		this.Height = Height;
		this.Text = Text;
		this.id = id;
		this.Position = Position;
		this.Color = Color;
		this.vertices = newVertices;
		this.TextfieldTexture = TextfieldTexture;
		
		this.Texture = true;
	}
	public Textfield(float Width, float Height, int id, Vector2f Position, Vector3f Color, String Text, float WidthBorder, float HeightBorder, Vector3f ColorBorder, String BorderTexture, String TextfieldTexture) {
		float y = Position.y;
		float x = (Position.x - 0.5f);
		float[] newVertices = {(-Width / 10) + x, (Height / 10) + y, 0, (-Width / 10) + x, (-Height / 10) + y, 0, (Width / 10) + x, (-Height / 10) + y,0, (Width / 10) + x, (Height / 10) + y,0};
		float[] newVerticesBorder = {((-Width - (WidthBorder / 4)) / 10) + x, ((Height + (HeightBorder / 2)) / 10) + y, 0, ((-Width - (WidthBorder / 4)) / 10) + x, ((-Height - (HeightBorder / 2)) / 10) + y, 0,
				((Width + (WidthBorder / 4)) / 10) + x, ((-Height - (HeightBorder / 2)) / 10) + y,0, ((Width + (WidthBorder / 4)) / 10) + x, ((Height + (HeightBorder / 2))  / 10) + y,0};
		
		this.Width = Width;
		this.Height = Height;
		this.Text = Text;
		this.id = id;
		this.Position = Position;
		this.Color = new Vector3f(Color.x / 255, Color.y / 255, Color.z / 255);
		this.vertices = newVertices;
		this.TextfieldTexture = TextfieldTexture;
		
		this.ColorBorder = new Vector3f(ColorBorder.x / 255, ColorBorder.y / 255, ColorBorder.z / 255);
		this.WidthBorder = WidthBorder;
		this.HeightBorder = HeightBorder;
		this.verticesBorder = newVerticesBorder;
		this.BorderTexture = BorderTexture;
		
		this.Texture = true;
		this.Border = true;
	}
	
	//Getters and Setters :D
	public void setPosition(Vector2f Position) { this.Position = Position; }
	public void setColor(Vector3f Color) { this.Color = new Vector3f(Color.x / 255, Color.y / 255, Color.z / 255); }
	public void setColorBoder(Vector3f ColorBorder) { this.ColorBorder = new Vector3f(ColorBorder.x / 255, ColorBorder.y / 255, ColorBorder.z / 255); }
		
	public void setWidth(int Width) { this.Width = Width; }
	public void setHeight(int Height) { this.Height = Height; }
		
	public void setText(String Text) { this.Text = Text; }
	public void addText(String Text) { this.Text += Text; }
	
	public void addText(char Text) { this.Text += Text; }
		
	public int getId() { return this.id; }
	public int[] getIndices() {  return this.indices; }
	
	public float getHeight() { return this.Height; }
	public float getWidth() { return this.Width; }
	public float[] getVertices() { return this.vertices; }
	
	public boolean getBorder() { return this.Border; }
	
	public String getText() { return this.Text; }
	
	public Vector2f getScale() {int x = (int) (this.getWidth() / 2); int y = (int) this.getHeight(); Vector2f FixedScale = new Vector2f(x,y); return FixedScale;}
	public Vector2f getPosition() { int x = Integer.valueOf((int) (this.Position.x - 0.5)); int y = Integer.valueOf((int) (this.Position.y)); Vector2f FixedPosition = new Vector2f(x,y); return FixedPosition; }	
	public Vector3f getColor() { return this.Color; }
	
	public RawModel getModel() { this.TextfieldModel = getLoader().loadToVAO(this.vertices,this.indices); return this.TextfieldModel; }
	public RawModel getModelBorder() { this.BorderModel = getLoader().loadToVAO(this.verticesBorder,this.indices); return this.BorderModel; }
	public Loader getLoader() { this.loader = new Loader(); return this.loader;}
	public Renderer getRenderer() { this.renderer = new Renderer(); return this.renderer;}
	public StaticShader getShader() { this.shader = new StaticShader(); return this.shader;}
	
	public TexturedModel getTexturedModel() { this.TextfieldModel = getLoader().loadToVAO(this.vertices,this.textureCoords,this.indices);  this.TextfieldTextureImage = new ModelTexture(getLoader().loadTexture(this.TextfieldTexture)); this.TextfieldTexturedModel = new TexturedModel(this.TextfieldModel,this.TextfieldTextureImage); return this.TextfieldTexturedModel; }
	public TexturedModel getTexturedBorderModel() { this.BorderModel = getLoader().loadToVAO(this.verticesBorder,this.textureCoords,this.indices);  this.BorderTextureImage = new ModelTexture(getLoader().loadTexture(this.BorderTexture)); this.BorderTexturedModel = new TexturedModel(this.BorderModel,this.BorderTextureImage); return this.BorderTexturedModel; }
	
	//Updating Keyboard input and rendering Text Field
	public void update(KeyboardControll Keyboard, List<Textfield> TextFields) {
		for(Textfield TextField:TextFields) {
			if(!TextField.Rendered) {
				//Set rendered to true so shader/renderer/loader
				//and models only get created once
				TextField.Rendered = true;
				
				System.out.println("We have looped for: " + TextField + "\n");
				System.out.println("TextFields"+ TextFields + "\n");
				
				//Initialize Loader/Renderer/Shader
				TextField.getLoader();
				TextField.getRenderer();
				TextField.getShader();
				
				//Initialize Models without texture
				if(!TextField.Texture) TextField.getModel();
				if(TextField.Border && !TextField.Texture) TextField.getModelBorder();
				
				//Initialize Models with Texture
				if(TextField.Texture) TextField.getTexturedModel();
				if(TextField.Border && TextField.Texture) TextField.getTexturedBorderModel();
			}
			
			//Render the Text Field with the correct models, loader, renderer, shader and/or texture.
			if(TextField.Border && TextField.Texture) TextField.renderTextfield(TextField.loader, TextField.renderer, TextField.shader, TextField.TextfieldTexturedModel, TextField.BorderTexturedModel, true);	
			else if(TextField.Texture)TextField.renderTextfield(TextField.loader, TextField.renderer, TextField.shader, TextField.TextfieldTexturedModel, true);
			
			//Render the Text Field without the correct models, loader, renderer, shader and/or texture.
			if(TextField.Border && !TextField.Texture) TextField.renderTextfield(TextField.loader, TextField.renderer, TextField.shader, TextField.TextfieldModel, TextField.BorderModel, false);	
			else if(!TextField.Texture)TextField.renderTextfield(TextField.loader, TextField.renderer, TextField.shader, TextField.TextfieldModel, false);
			
	    	Keyboard.inputManager(TextField);
		}
	}
	
	//Text Field renderer's without texture
	public void renderTextfield(Loader loader, Renderer renderer, StaticShader shader, RawModel model, RawModel modelBorder, boolean Textured) {
		 renderer.prepare();
         shader.start();
         
         shader.setTextured(new Vector2f(0,0));
         
		 shader.setColor(this.ColorBorder); 
		 renderer.render(modelBorder);
    	
         shader.setColor(this.Color);
         renderer.render(model);
         
         shader.stop();
	}
	public void renderTextfield(Loader loader, Renderer renderer, StaticShader shader, RawModel model, boolean Textured) {
		renderer.prepare();
        shader.start();
        
        shader.setTextured(new Vector2f(0,0));
        
        shader.setColor(this.Color);
        renderer.render(model);
        
        shader.stop();
	}
	
	//Text Field renderer's with texture
	public void renderTextfield(Loader loader, Renderer renderer, StaticShader shader, TexturedModel model, TexturedModel modelBorder, boolean Textured) {
		 renderer.prepare();
         shader.start();
         
         shader.setTextured(new Vector2f(1,1));
         
		 shader.setColor(this.ColorBorder); 
		 renderer.renderTexturedModel(modelBorder);
    	
         shader.setColor(this.Color);
         renderer.renderTexturedModel(model);
         
         shader.stop();
	}
	public void renderTextfield(Loader loader, Renderer renderer, StaticShader shader, TexturedModel model, boolean Textured) {
		renderer.prepare();
        shader.start();
        
        shader.setTextured(new Vector2f(1,1));
        
        shader.setColor(this.Color);
        renderer.renderTexturedModel(model);
        
        shader.stop();
	}
	
	
	public void cleanUp(Loader loader, StaticShader shader) {
		shader.cleanUp();
		loader.cleanUp();
	}
}

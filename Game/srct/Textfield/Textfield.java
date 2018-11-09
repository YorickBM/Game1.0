package Textfield;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class Textfield {	
	private Loader loader;
	private KeyboardControll Keyboard;
	private FontType FontType;
	private GUIText DisplayText;
	
    private String texture, borderTexture;
    private Vector2f position, scale, borderPosition, borderScale;
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private GuiTexture guiTexture;
    
    private boolean isHidden = false;
    private boolean isSelected = false;
    
    private String Text = ""; 
    private String Name = "default";

    public Textfield(Loader loader, KeyboardControll keyboard, String texture, Vector2f position, Vector2f scale, String Name) {
        this.setLoader(loader);
        this.setKeyboard(keyboard);
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.guiTexture = new GuiTexture(loader.loadTexture(texture), position, scale);
        this.Name = Name;
    }
    
    public Textfield(Loader loader, KeyboardControll keyboard, String texture, Vector2f position, Vector2f scale, Vector4f color, String Name) {
        this(loader, keyboard, texture, position, scale, Name);
        this.color = color;
        this.guiTexture.setColor(color);
    }
    
    public Textfield(Loader loader, KeyboardControll keyboard, FontType FontType, String texture, Vector2f position, Vector2f scale, String Name) {
    	this(loader, keyboard, texture, position, scale, Name);
    	this.FontType = FontType;
    }
    
    public Textfield(String texture, Vector2f scale, Textfield textfield) {
    	Vector2f BorderScale = new Vector2f(textfield.getScale().x + (scale.x / 10), textfield.getScale().y + (scale.y / 10));
    	
    	 this.setLoader(textfield.getLoader());
         this.setKeyboard(textfield.getKeyboard());
         this.texture = texture;
         this.position = textfield.getPosition();
         this.scale = BorderScale;
         this.guiTexture = new GuiTexture(loader.loadTexture(texture), position, BorderScale);
         this.Name = textfield.getName() + "Border";
    }
    
    public void update(List<Textfield> Textfields) {
    	 if (!isHidden) {
         	GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
             Vector2f location = guiTexture.getPosition();
             Vector2f scale = guiTexture.getScale();
             Vector2f mouseCoordinates = DisplayManager.getNormalizedMouseCoordinates();
             if (location.y + scale.y > -mouseCoordinates.y && location.y - scale.y < -mouseCoordinates.y && location.x + scale.x > mouseCoordinates.x && location.x - scale.x < mouseCoordinates.x) {
            	 
            while (Mouse.next())
                 if (Mouse.isButtonDown(0)) {
                	 for(Textfield Textfield:Textfields) {
                		 Textfield.setIsSelected(false);
                	 }
                	 isSelected = true;
                 }
             }
         }
    	 
    	 if(isSelected) {
    		 Keyboard.inputManager(this);
    		 System.out.println(this.Text);
    	 }
    }
    
    private void startRender(List<GuiTexture> guiTextureList) {
        guiTextureList.add(guiTexture);
    }

    private void stopRender(List<GuiTexture> guiTextureList) {
        guiTextureList.remove(guiTexture);
    }
    
    public void hide(List<GuiTexture> guiTextures) {
        stopRender(guiTextures);
        isHidden = true;
    }

    public void show(List<GuiTexture> guiTextures) {
        startRender(guiTextures);
        isHidden = false;
    }

    public void reopen(List<GuiTexture> guiTextures) {
        hide(guiTextures);
        show(guiTextures);
    }
    
    public KeyboardControll getKeyboard() {
    	return Keyboard;
    }
    
    public void setKeyboard(KeyboardControll Keyboard) {
    	this.Keyboard = Keyboard;
    }

	public Loader getLoader() {
		return loader;
	}

	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector4f color) {
		this.color = color;
	}

	public GuiTexture getGuiTexture() {
		return guiTexture;
	}

	public void setGuiTexture(GuiTexture guiTexture) {
		this.guiTexture = guiTexture;
	}
	
	//Custom Setters, Getters & Adders
    
	public void setText(String Text) { 
		this.Text = Text; 
	}
	
	public void addText(String Text) { 
		this.Text += Text; 
	}
	
	public void addText(char Text) { 
		this.Text += Text; 
	}
	
	public String getText() { 
		return this.Text;
	}
	
	public String getName() {
		return this.Name;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
    public boolean getIsSelected() {
    	return isSelected;
    }
    
    public void setIsSelected(boolean isSelected) {
    	this.isSelected = isSelected;
    }

	public String getBorderTexture() {
		return borderTexture;
	}

	public void setBorderTexture(String borderTexture) {
		this.borderTexture = borderTexture;
	}

	public Vector2f getBorderScale() {
		return borderScale;
	}

	public void setBorderScale(Vector2f borderScale) {
		this.borderScale = borderScale;
	}
	
    public void setFont(FontType font) {
    	FontType = font;
    }
    
    public FontType getFont() {
    	return FontType;
    }
    
    public void createDisplayText(Textfield textfield, float Scale, boolean Centered) {
    	this.DisplayText = new GUIText(3, textfield.getFont(), textfield.getPosition(), Scale, Centered);
    }
    
    public void updateDisplayText(Textfield textfield) {
    	this.DisplayText.drawText(textfield.getText());
    }
    
    public void setTextColor(float r, float g, float b) {
    	this.DisplayText.setColour(r, g, b);
    }
    
    public GUIText getDisplayText() {
    	return this.DisplayText;
    }

	public Vector2f getBorderPosition() {
		return borderPosition;
	}

	public void setBorderPosition(Vector2f borderPosition) {
		this.borderPosition = borderPosition;
	}

}

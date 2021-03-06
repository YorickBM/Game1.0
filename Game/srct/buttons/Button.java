package buttons;

import guis.GuiTexture;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

import java.util.List;

public abstract class Button implements IButton {
	private Loader loader;
    private String texture;
    private Vector2f position, scale;
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private GuiTexture guiTexture;
    private boolean isHidden = false;
    private boolean isHovering = false;

    public Button(Loader loader, String texture, Vector2f position, Vector2f scale) {
        this.setLoader(loader);
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.guiTexture = new GuiTexture(loader.loadTexture(texture), position, scale);
    }

    public Button(Loader loader, String texture, Vector2f position, Vector2f scale, Vector4f color) {
        this(loader, texture, position, scale);
        this.color = color;
        this.guiTexture.setColor(color);
    }

    public void update() {
        if (!isHidden) {
        	GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            Vector2f location = guiTexture.getPosition();
            Vector2f scale = guiTexture.getScale();
            Vector2f mouseCoordinates = DisplayManager.getNormalizedMouseCoordinates();
            if (location.y + scale.y > -mouseCoordinates.y && location.y - scale.y < -mouseCoordinates.y && location.x + scale.x > mouseCoordinates.x && location.x - scale.x < mouseCoordinates.x) {
                whileHover(this);
                if (!isHovering) {
                    isHovering = true;
                    startHover(this);
                }
                
             while (Mouse.next())
                    if (Mouse.isButtonDown(0)) onClick(this);
            } else {
                if (isHovering) {
                    isHovering = false;
                    stopHover(this);
                }
                guiTexture.setScale(this.scale);
            }
        }
    }

    public void playHoverAnimation(float scaleFactor) {
        guiTexture.setScale(new Vector2f(scale.x + scaleFactor, scale.y + scaleFactor));
    }

    public void playerClickAnimation(float scaleFactor) {
        guiTexture.setScale(new Vector2f(scale.x - (scaleFactor * 2), scale.y - (scaleFactor * 2)));
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

    public boolean isHovering() {
        return isHovering;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public GuiTexture getGuiTexture() {
        return guiTexture;
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getPosition() {
        return position;
    }

    public String getTexture() {
        return texture;
    }

    public void setColor(Vector4f color) {
        guiTexture.setColor(color);
    }

    public void setScale(Vector2f scale) {
        guiTexture.setScale(scale);
    }

    public void setPosition(Vector2f position) {
        guiTexture.setPosition(position);
    }
    
    public boolean hidden() {
    	return isHidden;
    }

	public void checkHover() {
	}

	public Loader getLoader() {
		return loader;
	}

	public void setLoader(Loader loader) {
		this.loader = loader;
	}
}

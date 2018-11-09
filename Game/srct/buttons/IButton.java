package buttons;

import guis.GuiTexture;

import java.util.List;

/**
 * Created by jamesraynor on 10/31/15.
 */
public interface IButton {
    void onClick(IButton button);

    void whileHover(IButton button);

    void startHover(IButton button);

    void stopHover(IButton button);

    void playHoverAnimation(float scaleFactor);

    void playerClickAnimation(float scaleFactor);

    void hide(List<GuiTexture> guiTextures);

    void show(List<GuiTexture> guiTextures);

    void reopen(List<GuiTexture> guiTextures);
    
    void resetScale();
    
    void update();
    
    void setScale();
}

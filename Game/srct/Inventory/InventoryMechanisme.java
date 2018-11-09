package Inventory;

import guis.GuiTexture;
import models.TexturedModel;

public class InventoryMechanisme {
	
	  private GuiTexture texture;
	  private TexturedModel model;
	  private int quantity;
	  private int ID;
	  
	  public InventoryMechanisme(GuiTexture texture, TexturedModel model, int quantity, int id) {
		  this.texture = texture;
		  this.model = model;
		  this.quantity = quantity;
		  this.ID = id;
	  }
	  
	  

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public GuiTexture getTexture() {
		return texture;
	}

	public void setTexture(GuiTexture texture) {
		this.texture = texture;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	  
	  
}

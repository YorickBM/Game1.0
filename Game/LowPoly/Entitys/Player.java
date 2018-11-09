package Entitys;

import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lwjgl2.Vector2f;
import lwjgl2.Vector3f;

import Entitys.InventoryMechanisme;
import Engine.Main;
import guis.GuiTexture;
import terrains.TerrainGenerator;
import Models.TexturedModel;
import display.Window;
import Engine.Manager;
import toolbox.Maths;

import static org.lwjgl.glfw.GLFW.*;
 
public class Player extends Entity {
	
	public String Username = "Guest";
	
	public Map<Integer, InventoryMechanisme> map = new HashMap<Integer, InventoryMechanisme>();
	public Map<Integer, GuiTexture> clearMap = new HashMap<Integer, GuiTexture>();
	public Map<Integer, InventoryMechanisme> groundItems = new HashMap<Integer, InventoryMechanisme>();
	
	@SuppressWarnings("rawtypes")
	public List<Entry> Itemtexture = new ArrayList<Entry>();
 
	public static final float RUN_SPEED = 40;
    private static final float TURN_SPEED = 160;
    public static final float GRAVITY = -50;
    public static final float JUMP_POWER = 18;
    
    public int Delay = 15;
 
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    
    public int health = 10;
    public int mana = 30;
    public int stamina = 100;
    public int souls = 5;
    
    public int xp = 0;
    public int money = 0;
    public int level = 1;
    public int quest = 0;
    
    public int maxHealth = 10;
    public int maxMana = 30;
    public int maxStamina = 100;
    public int maxSouls = 5;
    
    public int Slots = 9;
    public int activeSlot = 0;
    public int onItem = 0;
    
    public boolean s0,s1,s2,s3,s4,s5,s6,s7,s8,s9 = false;
    
    public Vector3f location;
    
    private boolean isInAir = false;
 
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
            float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }
    
    //Called every Frame
    public void move(TerrainGenerator terrain, Window window) {
    	checkMouse(window);
        checkInputs(window);
        checkInventory(window);
        checkItem(window);
        super.increaseRotation(0, getCurrentTurnSpeed() * Manager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * Manager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * Manager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * Manager.getFrameTimeSeconds(), 0);
        
        float terrainHeight = terrain.getHeightNormal(getPosition().x, getPosition().z);
        
        if (super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
        
        this.location = super.getPosition();
        
    }
    
    //Manage Items
    public void checkItem(Window window) {//Hard coded key controls, Need to remove entity from entities list afther pickedup
    	if(window.isKeyReleased(GLFW_KEY_Q)) {
    		if(this.activeSlot == 0 && s0 == true) {this.dropItem(this.activeSlot); s0 = false;}
    		if(this.activeSlot == 1 && s1 == true) {this.dropItem(this.activeSlot); s1 = false;}
    		if(this.activeSlot == 2 && s2 == true) {this.dropItem(this.activeSlot); s2 = false;}
    		if(this.activeSlot == 3 && s3 == true) {this.dropItem(this.activeSlot); s3 = false;}
    		if(this.activeSlot == 4 && s4 == true) {this.dropItem(this.activeSlot); s4 = false;}
    		if(this.activeSlot == 5 && s5 == true) {this.dropItem(this.activeSlot); s5 = false;}
    		if(this.activeSlot == 6 && s6 == true) {this.dropItem(this.activeSlot); s6 = false;}
    		if(this.activeSlot == 7 && s7 == true) {this.dropItem(this.activeSlot); s7 = false;}
    		if(this.activeSlot == 8 && s8 == true) {this.dropItem(this.activeSlot); s8 = false;}
    		if(this.activeSlot == 9 && s9 == true) {this.dropItem(this.activeSlot); s9 = false;}
    		
    	}
    	
    	int EmptySlot = 0;
    	if(s9 == false) { EmptySlot = 9;}
    	if(s8 == false) { EmptySlot = 8;}
    	if(s7 == false) { EmptySlot = 7;}
    	if(s6 == false) { EmptySlot = 6;}
    	if(s5 == false) { EmptySlot = 5;}
    	if(s4 == false) { EmptySlot = 4;}
    	if(s3 == false) { EmptySlot = 3;}
    	if(s2 == false) { EmptySlot = 2;}
    	if(s1 == false) { EmptySlot = 1;}
    	if(s0 == false) { EmptySlot = 0;}

    	for(Entry<?, ?> e: groundItems.entrySet()) {
    		InventoryMechanisme groundItem = groundItems.get(e.getKey());
    		TexturedModel groundItemModel = groundItem.getModel();
    		
    		for(Entity entity: Main.entities) { 
    			if(entity.getModel().equals(groundItemModel)) {
    				Vector3f loc = new Vector3f(entity.getPosition().x, entity.getPosition().y + 1f, entity.getPosition().z);
    				
    				float distance = Maths.distanceSquared(loc.x, loc.y, loc.z, this.location.x, this.location.y, this.location.z);
    				if(distance < 8f) {
    					if(onItem >= 100) {//Need to remove entity from entities list
    						this.addItem(groundItem.getTexture(), groundItem.getModel(), EmptySlot, groundItem.getQuantity());
    						entity.setPosition(new Vector3f(entity.getPosition().x, entity.getPosition().y - 50f, entity.getPosition().z));
        					onItem = 0;
    					}
    					onItem++;
    				}
    			}
    		}  			
    	}
    }
    public void addItem(GuiTexture texture, TexturedModel model, int slot, int amount) {
    	InventoryMechanisme invItems = new InventoryMechanisme(texture, model, amount, slot);
    	
    	map.put(slot, invItems);
    	
    	if(slot == 0) s0= true;
    	if(slot == 1) s1= true;
    	if(slot == 2) s2= true;
    	if(slot == 3) s3= true;
    	if(slot == 4) s4= true;
    	if(slot == 5) s5= true;
    	if(slot == 6) s6= true;
    	if(slot == 7) s7= true;
    	if(slot == 8) s8= true;
    	if(slot == 9) s9= true;
    }
    
    public InventoryMechanisme getItem(int slot) {
    	InventoryMechanisme invItems = map.get(slot);
    	return invItems;
    }
    
    public void modifyItem(int slot, int amount) {
    	InventoryMechanisme invItems = map.get(slot);
    	invItems.setQuantity(amount);
    }
    
    public void removeItem(int slot) {
    	map.remove(slot);
    } 
    
    public void dropItem(int slot) {
    	InventoryMechanisme invItems = map.get(slot);
    	TexturedModel model = invItems.getModel();
    	
    	Vector3f loc = new Vector3f(this.location.x, this.location.y + 0.3f, this.location.z);
    	GuiTexture ItemTexture = clearMap.get(slot);
    	
    	Main.InvGui.remove(ItemTexture);
    	Main.entities.add(new Entity(model, loc, 0, 360, 0, 1f));
    	
    	map.remove(slot);
    	clearMap.remove(slot);
    	
    	groundItems.put(slot, invItems);
    }
    
	public void renderItem() { //hard coded int's
    	for(Entry<?, ?> e: map.entrySet()){    		
    		InventoryMechanisme invItems = map.get(e.getKey());
    		if(!Itemtexture.contains(e)) {
    			Itemtexture.add(e);    			
    			
    			float y = 0f;
    	    	float x = 0f;
    	    	
    	    	int slot = (int) e.getKey();
    	    	
    	    	if(slot < 5) x = -0.06325f + 0.06f * slot;
    	    	if(slot > 4) x = -0.06325f + 0.06f * (slot - 5);
    	    	if(slot > 4) y = -0.705f;
    	    	if(slot < 5) y = -0.605f;
    			
    			GuiTexture ItemTexture = new GuiTexture(invItems.getTexture().getTexture(), invItems.getTexture().getPosition(), invItems.getTexture().getScale());
    			ItemTexture.setPosition(new Vector2f(x, y));
    			
    			clearMap.put(slot, ItemTexture);
    			Main.InvGui.add(ItemTexture);
    		}
    	}
    }
    
    //Manage Inventory
    public void updateInventory(GuiTexture texture) { //hard coded int's
    	float y = 0f;
    	float x = 0f;
    	
    	if(this.activeSlot < 5) x = -0.06325f + (0.06f * this.activeSlot);
    	if(this.activeSlot > 4) x = -0.06325f + (0.06f * (this.activeSlot - 5));
    	if(this.activeSlot > 4) y = -0.705f;
    	if(this.activeSlot < 5) y = -0.605f;
    		
    	texture.setPosition(new Vector2f(x, y));
    }
   
    private void checkInventory(Window window) { // Hard coded Controls
    	//Keyboard controls of Inventory
		if(window.isKeyDown(GLFW_KEY_0)) this.activeSlot = 9;
		if(window.isKeyDown(GLFW_KEY_1)) this.activeSlot = 0;
		if(window.isKeyDown(GLFW_KEY_2)) this.activeSlot = 1;
		if(window.isKeyDown(GLFW_KEY_3)) this.activeSlot = 2;
		if(window.isKeyDown(GLFW_KEY_4)) this.activeSlot = 3;
		if(window.isKeyDown(GLFW_KEY_5)) this.activeSlot = 4;
		if(window.isKeyDown(GLFW_KEY_6)) this.activeSlot = 5;
		if(window.isKeyDown(GLFW_KEY_7)) this.activeSlot = 6;
		if(window.isKeyDown(GLFW_KEY_8)) this.activeSlot = 7;
		if(window.isKeyDown(GLFW_KEY_9)) this.activeSlot = 8;
		
		/*
		 * Mouse DWeel function still needs creation
		 *
		//Mouse controls of Inventory
		int dWheel = Mouse.getDWheel();
		if(this.activeSlot >= 9 && dWheel > 0) this.activeSlot = -1;
		if(this.activeSlot <= 0 && dWheel < 0) this.activeSlot = 10;
		
		if(this.activeSlot != 9 && dWheel > 0) this.activeSlot += 1;
		if(this.activeSlot != 0 && dWheel < 0) this.activeSlot -= 1;
		 */
    }
    
    private void checkMouse(Window window) {
    	/*
         * Mouse is Grabbed Function new in LWJGL 3
         * 
    	int mouseRotation = Mouse.getDX();
        if(Mouse.isGrabbed()) {  
        	if(mouseRotation != 0) {
            	if(mouseRotation > 2.5f) this.setCurrentTurnSpeed(-getTurnSpeed());
            	if(mouseRotation < 2.5f) this.setCurrentTurnSpeed(getTurnSpeed());
            	return;
        	}
		this.setCurrentTurnSpeed(0);
        }
        */
    }
 
    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }
 
    private void checkInputs(Window window) { // < Hard coded controls
        if (window.isKeyDown(GLFW_KEY_W)) {
            this.currentSpeed = RUN_SPEED;
        } else if (window.isKeyDown(GLFW_KEY_S)) {
            this.currentSpeed = -RUN_SPEED;
        }else {
            this.currentSpeed = 0;
        }
 
        if (window.isKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
        
        /*
         * Mouse is Grabbed Function new in LWJGL 3
         * 
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        	if(Mouse.isGrabbed() && Delay <= 0) {
        		Mouse.setGrabbed(false);
        		Delay += 15;
        		return;
        	}
        	if(!Mouse.isGrabbed() && Delay <= 0) {
        		Mouse.setGrabbed(true);
        		Delay += 15;
        		return;
        	}
        }
        
        if(Delay > 0) {
        	Delay--;
        }
        */
    }
    
    public void setStats(String stat, int value) {
    	if(stat.equalsIgnoreCase("health")) this.health = value;
    	if(stat.equalsIgnoreCase("mana")) this.mana = value;
    	if(stat.equalsIgnoreCase("stamina")) this.stamina = value;
    	if(stat.equalsIgnoreCase("souls")) this.souls = value;
    	if(stat.equalsIgnoreCase("money")) this.money = value;
    	if(stat.equalsIgnoreCase("xp")) this.xp = value;
    	
    	if(stat == "maxHealth") this.maxHealth = value;
    	if(stat == "maxMana") this.maxMana = value;
    	if(stat == "maxStamina") this.maxStamina = value;
    	if(stat == "maxSouls") this.maxSouls = value;
    }
    
    public int getStats(String stat) {
    	if(stat.equalsIgnoreCase("health")) return this.health;
    	if(stat.equalsIgnoreCase("mana")) return this.mana;
    	if(stat.equalsIgnoreCase("stamina")) return this.stamina;
    	if(stat.equalsIgnoreCase("souls")) return this.souls;
    	if(stat.equalsIgnoreCase("money")) return this.money;
    	if(stat.equalsIgnoreCase("xp")) return this.xp;
    	
    	if(stat.equalsIgnoreCase("maxHealth")) return this.maxHealth;
    	if(stat.equalsIgnoreCase("maxMana")) return this.maxMana;
    	if(stat.equalsIgnoreCase("maxStamina")) return this.maxStamina;
    	if(stat.equalsIgnoreCase("maxSouls")) return this.maxSouls;
    	return 0;
    }
    
    public int getLevel() {
    	return this.level;
    }
    
    public void setlevel(int value) {
    	this.level = value;
    }
    
    public int getQuest() {
    	return this.quest;
    }
    
    public void setQuest(int value) {
    	this.quest = value;
    }
    
    public void setUsername(String username) {
    	this.Username = username;
    }
    
    public String getUsername() {
    	return this.Username;
    }
    
    public Vector3f getLocation() {
    	return this.location;
    }

	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}

	public void setCurrentTurnSpeed(float currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}

	public static float getTurnSpeed() {
		return TURN_SPEED;
	}
 
}
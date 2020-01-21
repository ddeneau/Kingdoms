package Model;
/* A building within a kingdom
 * 
 * Holds state for economic output
 * plot space, ID, and name.  */
public interface Building {
	
	/* Enumerated type for building size. */
	public enum BuildingSize{large, medium, small}
	
	/* Enumerated type for building category. */
	public enum BuildingType{commerical, government, military, residential}
	
	/* Removes the building from the Kingdom. */
	public void destroy();
	
	/* Returns the ID. */
	public int getId();
	
	/* Returns the building name. */
	public String getName();
	
	/* Returns the economic output. */
	public int getOutput();
	
	/* Sets the ID. */
	public void setId();;
	 
	/* Upgrades the building. */
	public void upgrade();;
	
}

package Model;
import java.util.ArrayList;

import javafx.stage.Stage;

/* Represents a kingdom in a world. */
public class Kingdom  {
	private static int ids = 0; 
	private ArrayList<Building> buildings;
	private String name;
	private ArrayList<String> population;
	private int gdp;
	Stage stage;
	
	public Kingdom(String name) {
		this.name = name;
		population = WorldTools.settlePopulation();
		buildings = new ArrayList<Building>();
	}
	
	public void addBuilding(Building.BuildingType type, Building.BuildingSize size) {
		switch(type) {
			case residential:
				buildings.add(new ResidentialBuilding(ids++));
				break;
			case government:
				buildings.add(new GovernmentBuilding(ids++));
				break;				
			case military:
				buildings.add(new MilitaryBuilding(ids++));
				break;
			case commerical:
				buildings.add(new CommercialBuilding(ids++));
				break;	
			default:
			
				break;
		}
	}
	
	public void addPerson() {
		population.add(WorldTools.getPerson());
	}
	
	private void calculateGDP() {
		int total = 0;
		
		for(Building building : buildings) {
			total += building.getOutput();
			total += 100;
		}
		
		gdp = (total == 0 ? 100: total);
	}
	
	/* The going ons in the Kingdom doing a turn. */
	protected void cityAffairs() {
		calculateGDP();
	}
	
	public void computeTurn() {
		cityAffairs();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	
	}
	
	public int getGDP() {
		return gdp;
	
	}
	
	public void selectKingdom() throws Exception {
		KingdomViewer viewer = new KingdomViewer(this);
		viewer.start(stage);
	}
}

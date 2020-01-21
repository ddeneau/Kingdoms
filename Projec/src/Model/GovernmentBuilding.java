package Model;

public class GovernmentBuilding implements Building {
	private int id;
	private String name;
	private int output;
	public GovernmentBuilding(int id) {
		this.name = "New Government Building";
		this.id = id;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upgrade() {
		// TODO Auto-generated method stub
		
	}
}

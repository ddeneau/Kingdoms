package Model;

public class CommercialBuilding implements Building {
	private int id; 
	private String name;
	private int output;
	public CommercialBuilding(int id) {
		this.name = " New Commercial Buidling";
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

package app.test;

public class MaintestRun {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestCase case123 = new TestCase();
		
		case123.addInstance(1, 5);
		//case123.trainInstances();
		//case123.classifyOneInstance(123678);
		//case123.addInstance(2, 9);
		//case123.addInstance(3, 18);
		//case123.addInstance(4, 21);
		//case123.addInstance(5, 26);
		case123.trainInstances();
		case123.classifyOneInstance(2);
		
		
		case123.addInstance(2, 9);
		case123.trainInstances();
		case123.classifyOneInstance(3);
		
		
		case123.addInstance(3, 19);
		case123.trainInstances();
		case123.classifyOneInstance(4);
		

		
		
		
	}

}

package app.test;

import app.model.ServiceObject;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class TestCase {
	

	private MultilayerPerceptron machineAlgorithm;
	private Attribute Attribute1;
	private Attribute Attribute2;
	private Instances instancesToTrain;
	
	public TestCase() {
		this.machineAlgorithm = new MultilayerPerceptron();
		//this.machineAlgorithm = new SMOreg();
		//this.machineAlgorithm = new M5P();
		//this.machineAlgorithm = new NaiveBayesUpdateable();
		Attribute1 = new Attribute("timestamp");
		Attribute2 = new Attribute("responsetime");
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(Attribute1);
		fvWekaAttributes.addElement(Attribute2);
		instancesToTrain = new Instances("Rel", fvWekaAttributes, 10);
	}
	
	
	public void addInstance(double date, int respTime){
		
		Instance inst = new Instance(2);
		inst.setValue(Attribute1, date);
		inst.setValue(Attribute2, respTime);
		instancesToTrain.add(inst);
	}
	
	public void trainInstances(){
		try {
			instancesToTrain.setClassIndex(instancesToTrain.numAttributes() - 1);
			machineAlgorithm.buildClassifier(instancesToTrain);
			//instancesToTrain.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public double classifyOneInstance(double date){
		
		double predictedRespTime = -1;
		Instance inst = new Instance(2);
		try {
			inst.setValue(Attribute1, date);
			inst.setValue(Attribute2, '?');
			predictedRespTime = machineAlgorithm.classifyInstance(inst);
			System.out.println("For date: " + date + ":" + predictedRespTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return predictedRespTime;
	}
	
	
	

}

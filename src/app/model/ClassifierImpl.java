package app.model;

import java.io.Serializable;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.trees.M5P;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ClassifierImpl implements Serializable{

	private static final long serialVersionUID = -8680265277029505585L;
	
	private Instances instancesToTrain;
	//private MultilayerPerceptron machineAlgorithm;
	private SMOreg machineAlgorithm;
	//private M5P machineAlgorithm;
	
	private String serviceName;
	private int trainedInstances = 0;
	
	private Attribute Attribute1;
	private Attribute Attribute2;
	
	public ClassifierImpl(String serviceName1) {
		this.serviceName = serviceName1;
		//this.machineAlgorithm = new MultilayerPerceptron();
		this.machineAlgorithm = new SMOreg();
		//this.machineAlgorithm = new M5P();
		Attribute1 = new Attribute("timestamp");
		Attribute2 = new Attribute("responsetime");
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(Attribute1);
		fvWekaAttributes.addElement(Attribute2);
		instancesToTrain = new Instances("Rel", fvWekaAttributes, 10);
	}
	
	public void addInstance(Instance instance){
		Instance inst = new Instance(instance);
		inst.deleteAttributeAt(0);
		instancesToTrain.add(inst);
		trainedInstances++;
	}
	
	public void trainInstances(){
		try {
			System.out.println("Training for:" + serviceName +" "+ trainedInstances + " instances");
			instancesToTrain.setClassIndex(instancesToTrain.numAttributes() - 1);
			machineAlgorithm.buildClassifier(instancesToTrain);
			instancesToTrain.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ServiceObject classifyOneInstance(double date){
		
		double predictedRespTime = -1;
		Instance inst = new Instance(2);
		try {
			inst.setValue(Attribute1, date);
			inst.setValue(Attribute2, '?');
			predictedRespTime = machineAlgorithm.classifyInstance(inst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ServiceObject(serviceName, (int)predictedRespTime);
	}
}

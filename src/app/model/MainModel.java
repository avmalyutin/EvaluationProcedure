package app.model;

import java.util.ArrayList;

import weka.core.Instances;

public class MainModel {

	public RecognitionModel modelToRecognise = new RecognitionModel();
	private TaskToQueue taskToTrain;
	
	public MainModel(){
		this.taskToTrain = new TaskToQueue();
	}
	
	
	public ArrayList<ServiceObject> classifyOneInstance(double date) throws NothingToEvaluateException {
		if(this.modelToRecognise.getTrainedInstances() <= 0){
			throw new NothingToEvaluateException();
		}
		return this.modelToRecognise.classifyOneInstance(date);
	}
	
	
	public void trainFromInstances(Instances train){
		this.taskToTrain.addInstances(train);
		if(this.taskToTrain.getSizeOfQueue() > 0){
			this.modelToRecognise.trainFromInstances(MainModel.this.taskToTrain.retrieveFirstElement());
		}
		System.out.println("Training on " + this.modelToRecognise.getTrainedInstances() + " instances completed!");
		
	}
}

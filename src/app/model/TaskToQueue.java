package app.model;

import java.util.LinkedList;
import java.util.Queue;

import weka.core.Instances;

public class TaskToQueue {
	
	private Queue<Instances> instancesToTrain;

	
	public TaskToQueue() {
		this.instancesToTrain = new LinkedList<Instances>();
	}

	public boolean isQueueEmpty(){
		return instancesToTrain.size() <=0?true:false;
	}

	public void addInstances(Instances instancesToAdd){
		this.instancesToTrain.add(instancesToAdd);
	}
	
	public Instances retrieveFirstElement(){
		return this.instancesToTrain.poll();
	}
	
	public int getSizeOfQueue(){
		return this.instancesToTrain.size();
	}
	
	public Queue<Instances> getInstancesTotrain() {
		return instancesToTrain;
	}

	public void setInstancesTotrain(LinkedList<Instances> instancesTotrain) {
		this.instancesToTrain = instancesTotrain;
	}

}

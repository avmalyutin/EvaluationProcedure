package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import weka.core.Instance;
import weka.core.Instances;

public class RecognitionModel implements Serializable  {
	
	
	private static final long serialVersionUID = -7432427941917676807L;

	private int trainedInstances;
	private Map<String, ClassifierImpl> mapOfclassifiers;
	private static final ExecutorService workers = Executors.newCachedThreadPool();
	

	public RecognitionModel() {
		trainedInstances = 0;
		mapOfclassifiers = new HashMap<String, ClassifierImpl>();
	}

	public void trainFromInstances(Instances train){
	
		//list of threads to train each model for each service separately
		ArrayList<Thread> listOfThread = new ArrayList<Thread>();
		
		//split the array of instances among service models
		for(int i = 0; i<train.numInstances(); i++){
			
			Instance instance1 = train.instance(i);
			String serverName = instance1.stringValue(0);
			
			if(!mapOfclassifiers.containsKey(serverName) || mapOfclassifiers.size() <=0){
				ClassifierImpl newObject = new ClassifierImpl(serverName);
				Thread thread = new Thread(){
					@Override
					public void run(){
						newObject.trainInstances();
					} 
				};
				listOfThread.add(thread);
				mapOfclassifiers.put(serverName, newObject);
			}
			mapOfclassifiers.get(serverName).addInstance(instance1);
		}
		
		//run all thread to train services models
		for(Thread th : listOfThread){
			th.start();
		}
		
		//wait until the training of all threads will be finished
		for(Thread th : listOfThread){
			try {
				th.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		trainedInstances += train.numInstances();
		
	}
	
	public ArrayList<ServiceObject> classifyOneInstance(double date)  {

		Collection<Callable<ServiceObject>> tasks = new ArrayList<Callable<ServiceObject>>();
		
		Iterator<Entry<String, ClassifierImpl>> it = mapOfclassifiers.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, ClassifierImpl> imp = (Map.Entry<String, ClassifierImpl>) it.next();
	    	Callable<ServiceObject> th = new Callable<ServiceObject>(){
	    	    public ServiceObject call(){
	    			ClassifierImpl strToReturn2 = (ClassifierImpl)imp.getValue();
	    			ServiceObject objToRet = strToReturn2.classifyOneInstance(date);
	    			return objToRet;
	    		}
	    	};
	    	tasks.add(th);
	    	//System.out.println("Check: " + imp.getKey());
	    }
		
	    ArrayList<ServiceObject> listToReturn = new ArrayList<ServiceObject>();
	    try{
	    List<Future<ServiceObject>> results = workers.invokeAll(tasks, 10, TimeUnit.SECONDS);
	    
	    for (Future<ServiceObject> f : results) {
	    		listToReturn.add(f.get());
	    	}
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    
        return listToReturn;
    }

	public int getTrainedInstances() {
		return trainedInstances;
	}

	public void setTrainedInstances(int trainedInstances) {
		this.trainedInstances = trainedInstances;
	}
}

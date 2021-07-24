package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	/**
	 * Retrieves the single instance of this class.
	 */
	private static Diary instance= new Diary();
	private List<Report> reports;
	private AtomicInteger total;


	private Diary()
	{
		reports=new LinkedList<Report>();
		total=new AtomicInteger();
		total.set(0);
	}

	public static Diary getInstance() {
		return instance;

	}

	public List<Report> getReports() {
		return this.reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		synchronized (this.reports)
		{
			reports.add(reportToAdd);
			//this.total.set(this.total.get()+1);
		}

	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try (Writer writer = new FileWriter(filename)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(this,writer);
		}
		catch (Exception e)
		{

		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return this.total.get();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		this.total.set(total.incrementAndGet());

		//Integer oldVal;
		//Integer newVal;
		//do {
		//	oldVal= this.total.get();
		//	newVal= oldVal++;
		//}
		//while(this.total.compareAndSet(oldVal,newVal));
	}

//    public void clear() {
//		this.reports.clear();
//		this.total.set(0);
//
//    }
}

package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {

	private String serialNumber;
	private String name;
	private AtomicBoolean isAvailable;
	//Constructor
	public Agent(String serialNumber, String name)
	{
		this.serialNumber=serialNumber;
		this.name=name;
		this.isAvailable= new AtomicBoolean();
		this.isAvailable.set(true);
	}


	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber=serialNumber;
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		this.name=name;
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		return this.name;
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		return this.isAvailable.get();
	}

	/**
	 * Acquires an agent.
	 */
	public synchronized void acquire() throws InterruptedException {
		//TODO:

		while (this.isAvailable.get()==false)
			wait();

		boolean oldVal;
		do {
			oldVal=this.isAvailable.get();
		}while(!this.isAvailable.compareAndSet(oldVal,false));

	}

	/**
	 * Releases an agent.
	 */
	public synchronized void  release(){
		//TODO:
		boolean oldVal;
		do {
			oldVal=this.isAvailable.get();
		}while(!this.isAvailable.compareAndSet(oldVal,true));

		notifyAll();
	}
}

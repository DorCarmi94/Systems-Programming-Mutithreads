package bgu.spl.mics;

import java.util.concurrent.TimeUnit;


/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	
	/**
	 * This should be the the only public constructor in this class.
	 */
	private T val;
	private boolean completed;


	public Future() {
		completed=false;
		val=null;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	public T get(){
		//QUESTION: Do we need sync?
		synchronized (this) {
			while (!this.completed) {
				try {
					wait();
				} catch (InterruptedException e) {
					return null;
				}
			}
			notifyAll();
		}
		return val;
	}
	
	/**
     * Resolves the result of this Future object.
     */
	public void resolve (T result) {
		//System.out.println("Resolve: ");
		val= result;
		completed=true;
		synchronized (this)
		{
			notifyAll();
		}

	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {

		return completed;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) {
		if(this.completed)
			return this.val;
		if(timeout<=0)
			return null;
		while (!this.completed)
		{
			//long milliTime=unit.toMillis(timeout);
			synchronized (this) {
				try {
					wait(timeout);
					if(!completed) {
						//System.out.println("completed and returning null");
						return null;
					}
					else
						return val;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
		return val;
	}

}

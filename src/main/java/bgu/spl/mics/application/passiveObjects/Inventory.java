package bgu.spl.mics.application.passiveObjects;

import com.google.gson.*;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static Inventory instance= new Inventory();
	/**
     * Retrieves the single instance of this class.
     */

	private Inventory()
	{
		this.gadgets=new LinkedList<>();
	}
	public static Inventory getInstance() {
		return instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {

		for (String str: inventory) {
			this.gadgets.add(str);
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){

		for (String gad:this.gadgets) {
			if(gad.equals(gadget))
			{
				this.gadgets.remove(gad);
				return true;
			}

		}
		return false;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){

		try (Writer writer = new FileWriter(filename)) {
			Gson gson = new GsonBuilder().create();

			gson.toJson(this.gadgets,writer);

		}
		catch (Exception e)
		{

		}


	}
}

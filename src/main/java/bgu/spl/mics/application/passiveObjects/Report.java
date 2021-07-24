package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
	/**
     * Retrieves the mission name.
     */
	private String missionName;
	private int MsID;
	private int MonneyPennyID;

	private List<String> agentsSerialNumbers;
	private List<String> agentsNames;

	private String gadgetName;
	private int timeIssued;
	private int Qtime;
	private int timeCreated; //When the report was created
	public String getMissionName() {
		return missionName;

	}

	/**
	 * Sets the mission name.
	 */

	//TODO: erase constructor
	/*
	public Report(String missionName,int MsID, int Qid, int  MonneyID, List<String> agentsSerialNumbers,
				  List<String> agentsNames, String gadgetName, int timeIssued, int Qtime, int timeCreated  )
	{
		this.missionName=missionName;
		this.MsID=MsID;
		this.MonneyPennyID= MonneyID;
		this.agentsSerialNumbers=agentsSerialNumbers;
		this.agentsNames= agentsNames;
		this.gadgetName=gadgetName;
		this.timeIssued=timeIssued;
		this.Qtime=Qtime;
		this.timeCreated=timeCreated;
	}*/

	public void setMissionName(String missionName) {
		this.missionName=missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {
		return MsID;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		this.MsID=m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {
		return MonneyPennyID;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		this.MonneyPennyID=moneypenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbersNumber() {
		return this.agentsSerialNumbers;
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbers(List<String> agentsSerialNumbersNumber) {
		 this.agentsSerialNumbers= new LinkedList<>();
		for (String str: agentsSerialNumbersNumber) {
			this.agentsSerialNumbers.add(str);
		}
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		return agentsNames;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		this.agentsNames= new Vector<>();
		for (String name:agentsNames) {
			this.agentsNames.add(name);
		}
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		return gadgetName;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		this.gadgetName=gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		return Qtime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		this.Qtime= qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		return this.timeIssued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		this.timeIssued= timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		return this.timeCreated;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		this.timeCreated=timeCreated;
	}
}

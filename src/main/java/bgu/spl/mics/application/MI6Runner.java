package bgu.spl.mics.application;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    public static void main(String[] args) {
        MessageBrokerImpl instace= (MessageBrokerImpl) MessageBrokerImpl.getInstance();


        //try (FileReader fileReader= new FileReader("/home/dory/Documents/assignment2/src/main/java/bgu/spl/mics/input3.json");BufferedReader bf = new BufferedReader(fileReader);){
        try (FileReader fileReader= new FileReader("/home/dory/Documents/assignment2/inputFile.json");BufferedReader bf = new BufferedReader(fileReader);){


            Gson g = new Gson();

            JsonObject obj = g.fromJson(bf, JsonObject.class);
            JsonArray gadget = obj.get("inventory").getAsJsonArray(); //all gadget in inventory
            JsonObject services = obj.get("services").getAsJsonObject(); //all services in MI6
            JsonArray squad = obj.get("squad").getAsJsonArray();//all agents in the squad

            //System.out.println("\nWelcome to MI6\n");
            //System.out.println("\nReading information\n");

            String[] gadgetsList = new String[gadget.size()];
            for (int i = 0; i <gadget.size() ; i++) {
                gadgetsList[i]=gadget.get(i).getAsString();
            }

            Inventory inventory=Inventory.getInstance();
            inventory.load(gadgetsList);
            //System.out.println("\nInventory is ready\n");

            Agent[] agents = new Agent[squad.size()];
            for (int i = 0; i <squad.size() ; i++) {
                String name=squad.get(i).getAsJsonObject().get("name").getAsString();
                String serialNumber=squad.get(i).getAsJsonObject().get("serialNumber").getAsString();
                Agent bond=new Agent(serialNumber,name);
                agents[i]=bond;
            }

            Squad squadInstacne= Squad.getInstance();
            squadInstacne.load(agents);
            //System.out.println("\nSquad is ready\n");

            JsonArray intelligence=services.get("intelligence").getAsJsonArray();
            int numberOfIntelligence=intelligence.size();
            Intelligence[] intelligences= new Intelligence[numberOfIntelligence];
            for (int i = 0; i <numberOfIntelligence ; i++) {
                //Runs on intelligences
                //JsonArray AllmyMissions= intelligence.get("missions").getAsJsonArray();
                JsonArray AllmyMissions= intelligence.get(i).getAsJsonObject().get("missions").getAsJsonArray();
                List<MissionInfo> missionInfos = new LinkedList<>();

                for (int j = 0; j <AllmyMissions.size(); j++) {
                    //For each intelligence runs on its missions
                    MissionInfo newMission= new MissionInfo();
                    JsonObject currentMission= AllmyMissions.get(j).getAsJsonObject();
                    JsonArray agentsArray= currentMission.get("serialAgentsNumbers").getAsJsonArray();
                    List<String> lstAgents= new LinkedList<>();

                    for (int k = 0; k < agentsArray.size(); k++) {
                        //For each mission runs on its agents
                        lstAgents.add(agentsArray.get(k).getAsString());
                    }
                    newMission.setSerialAgentsNumbers(lstAgents);
                    newMission.setGadget(currentMission.get("gadget").getAsString());
                    newMission.setDuration(currentMission.get("duration").getAsInt());
                    newMission.setMissionName(currentMission.get("name").getAsString());
                    newMission.setTimeExpired(currentMission.get("timeExpired").getAsInt());
                    newMission.setTimeIssued(currentMission.get("timeIssued").getAsInt());
                    missionInfos.add(newMission);
                }

                Intelligence newIntelligence= new Intelligence(missionInfos);
                intelligences[i]= newIntelligence;

            }
            //System.out.println("\nMissions are ready, waiting to be exectued\n");

            int numberOfMs=services.get("M").getAsInt();;
            int numberOfMonneyPenny=services.get("Moneypenny").getAsInt();
            //int numberOfMonneyPenny=1;
            M[] ms =new M[numberOfMs];
            Moneypenny[] moneypennies= new Moneypenny[numberOfMonneyPenny];

            for (int i = 0; i <numberOfMs; i++) {
                M m= new M();
                ms[i]=m;
            }

            for (int i = 0; i < numberOfMonneyPenny; i++) {
                Moneypenny moneypenny= new Moneypenny();
                moneypennies[i]=moneypenny;
            }

            Q q= new Q();
            int numberOfQ=1;

            int maxTime=services.get("time").getAsInt();

            TimeService timeService= new TimeService(100,maxTime);

            int numberOfTimeServices=1;

            //System.out.println("\nCreating threads\n");

            int numberOfThreads= numberOfIntelligence+numberOfMonneyPenny+numberOfMs+numberOfQ;

            //Thread 1- intelligence
            //Thread 2- intelligence
            //Thread 3- M
            //Thread 4- M
            //Thread 5- MonneyPenny
            //Thread 6- MonneyPenny
            //Thread 7- Q
            //Thread 8- TimeService



            Thread[] threads = new Thread[numberOfThreads];
            int i=0;
            for (int j=0; j < numberOfIntelligence; j++) {

                threads[i]= new Thread(intelligences[j]);
                threads[i].setName("Intelligence"+(j+1));
                //System.out.println("\nThread "+i+"- Intelligence "+(j+1)+"\n");
                i++;

            }

            for (int j=0; j < numberOfMs; j++) {
                threads[i]= new Thread(ms[j]);
                threads[i].setName("M"+(j+1));
                //System.out.println("\nThread "+i+"- M "+(j+1)+"\n");
                i++;
            }

            for (int j=0; j <numberOfMonneyPenny; j++) {
                threads[i]= new Thread(moneypennies[j]);
                threads[i].setName("Moneypenny"+(j+1));
                //System.out.println("\nThread "+i+"- Moneypenny "+(j+1)+"\n");
                i++;
            }

            threads[i]=new Thread(q);
            threads[i].setName("Q");
            //System.out.println("\nThread "+i+"- Q\n");

            //System.out.println("\n-------------------Finishing loading----\n");
            //System.out.println("\n//////////////////////////\n");
            //System.out.println("\nStarting program\n");
            //System.out.println("\n//////////////////////////\n");
            for (int j = 0; j <threads.length ; j++) {

                threads[j].start();
            }
            Thread.sleep(100); //QUESTION??
            Thread timeSeriveThread= new Thread(timeService);
            timeSeriveThread.setName("TimeService");
            timeSeriveThread.start();
            for (int j = 0; j <threads.length ; j++) {

                threads[j].join();
            }
            //System.out.println("Therads lenght:" +threads.length);

            //System.out.println("\n-------------------Finishing threads initialization----\n");
            //System.out.println("\n//////////////////////////\n");
            //System.out.println("\nStarting missions clock\n");
            //System.out.println("\n//////////////////////////\n");


            timeSeriveThread.join();

            //System.out.println("Finished");
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);

            bf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //System.out.println("Exception: File not found");
        } catch (InterruptedException e) {
            e.printStackTrace();
            //System.out.println("Exception: Interrupt exception");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
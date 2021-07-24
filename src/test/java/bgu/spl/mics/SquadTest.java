package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SquadTest {
    private Squad testedSquad;
    private List<String> myAgents;
    Agent[] agents;
    @BeforeEach
    public void setUp(){

        testedSquad= Squad.getInstance();
        myAgents= new ArrayList<String>(5);
        agents = new Agent[5];
        for (int i = 0; i <myAgents.size(); i++) {
            Agent agent=new Agent(String.valueOf(i),"00"+String.valueOf(i));
            myAgents.add(agent.getSerialNumber());
            agents[i]=agent;
        }
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");

        //testedSquad.getAgents();
        //        testedSquad.getAgentsNames();
        //        testedSquad.load();
        //        testedSquad.releaseAgents();
        //        testedSquad.sendAgents();;

    }

    @Test
    public void testLoad()
    {

        testedSquad.load(agents);
    }





    @Test
    public void testGetAgentsName()
    {

        //TDL: ask about test load
        //Suppose to work- assert true
        List<String> names= testedSquad.getAgentsNames(myAgents);
        for (int i = 0; i < myAgents.size(); i++) {

            assertEquals(names.get(i),agents[i].getName());
            //assertEquals(names.get(i),null);
        }

        //Suppose to fail- assert false
        List<String> namesShouldAppear = new ArrayList<String>(5);
        for (int i = agents.length; i <myAgents.size()*2; i++) {
            Agent agent=new Agent(String.valueOf(i),"00"+String.valueOf(i));
            namesShouldAppear.add(agent.getSerialNumber());
        }
        List<String> names2= testedSquad.getAgentsNames(myAgents);
        for (int i = 0; i < myAgents.size(); i++) {

            assertNotEquals(names2.get(i),namesShouldAppear.get(i));
            //assertEquals(names.get(i),null);
        }
    }

    @Test
    public void testSendAgent()
    {
        List<String> agentsToSend= new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            agentsToSend.add(this.agents[i].getSerialNumber());
        }
        testedSquad.sendAgents(agentsToSend,5);

    }



    @Test
    public void testReleaseAgent()
    {
        boolean succeed= false;
        List<String> agentsToRelease = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            agentsToRelease.add(this.agents[i].getSerialNumber());
        }
        try{
            testedSquad.releaseAgents(agentsToRelease);
            //Should throw exception

        }catch (Exception e)
        {
            succeed=true;
        }
        assertTrue(succeed);
        if(succeed) {
            succeed=false;
            Agent a = new Agent("700", "00700");
            List<String> agentsToRelease2 = new ArrayList<>(1);
            agentsToRelease2.add(a.getSerialNumber());

            try {
                testedSquad.releaseAgents(agentsToRelease2);
                //Should throw exception

            } catch (Exception e) {
                succeed = true;
            }
        }
        assertTrue(succeed);
        testSendAgent();
        List<String> agentsToRelease3 = new ArrayList<>(3);
        succeed=true;
        for (int i = 0; i < 3; i++) {
            agentsToRelease3.add(this.agents[i].getSerialNumber());
        }
        try{
            testedSquad.releaseAgents(agentsToRelease3);
            //Should throw exception

        }catch (Exception e)
        {
            succeed=false;
        }
        assertTrue(succeed);

    }















}

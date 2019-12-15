package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad squad;
    private boolean agentFlag;





    @BeforeEach
    public void setUp(){
        squad = squad.getInstance();
    }


    @Test
    public void loadTest () {
        Agent[] agents = CreateAgentTestArray();
        squad.load(agents);
        for (Integer i=0; i<agents.length; i++){
            assertTrue(squad.findSpecificAgent("00"+i.toString()));
        }
    }


    private Agent[] CreateAgentTestArray() {
        Agent[] agents = new Agent[7];
        for(Integer i = 0; i<agents.length; i++){
            agents[i] = new Agent("Agent00"+i.toString(), "00"+i.toString());
            System.out.println("check inside array initialization: agent is: "+"Agent00"+i.toString());
        }
        return agents;

    }

    private List<String> CreateSerialTestList() {
        List<String> serials = new ArrayList<String>(7);
        for(Integer i = 0; i<serials.size(); i++){
           serials.set(i, "00"+i.toString());
        }
        return serials;

    }


    @Test
    public void releaseAgentsTest(){
        Agent[] agents = CreateAgentTestArray();
        squad.load(agents);
        List<String> serialsTest = CreateSerialTestList();
        squad.releaseAgents(serialsTest);
        for (Integer i = 0; i<squad.getNumOfAgents(); i++){
            assertTrue(squad.getSpecificAgent("00"+i.toString()).isAvailable());
        }
    }



    @Test
    public void testSleepInSendAgents(){

    }



    @Test
    public void testGetAgents(){
        Agent[] agents = new Agent[1];
        agents[0] = new Agent("James Bond","007");
        squad.load(agents);
        List<String> agent007List = new ArrayList<>();
        agent007List.add("007");
        //check if method finds an existing agent
        assertTrue(squad.getAgents(agent007List));
        //remove agent 007 and test that it's not there
        agents[0] = null;
        assertFalse(squad.getAgents(agent007List));
    }


    @Test
    public void testGetAgentsNames(){
        List<String> serialsTest = createSerialsListForTesting();
        Agent[] agents = CreateAgentTestArray();
        squad.load(agents);
        assertEquals(squad.getAgentsNames(serialsTest), serialsTest);
    }


    //creates a list of 3 agents with the serials: 001 , 003, 006
    public List<String> createSerialsListForTesting(){
        List<String> serialsList = new ArrayList<>();
        serialsList.add("001");
        serialsList.add("003");
        serialsList.add("006");
        return serialsList;
    }

}

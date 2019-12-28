package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.JsonArray;

import java.util.List;

public class InputInfo {

    private String[] inventory;
    private Services services;
    private Agent[] squad;


    public class Services {
        private int M;
        private int Moneypenny;
        private IntelligenceInput[] intelligence;
        private int time;

        public class IntelligenceInput {
            List<MissionInfo> missions;


            public void setMissions(List<MissionInfo>  missions){
                this.missions = missions;
            }

            public List<MissionInfo>  getMissions(){
                return missions;
            }
        }


        public void setM(int M) {
            this.M = M;
        }

        public int getM() {
            return M;
        }

        public int getMoneypenny() {
            return Moneypenny;
        }

        public void setMoneypenny(int mps) {
            this.Moneypenny = mps;
        }

        public void setIntelligence(IntelligenceInput[] intelligence) {
            this.intelligence = intelligence;
        }

        public IntelligenceInput[] getIntelligence() {
            return intelligence;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }



    }

    public String[] getInventory() {
        return this.inventory;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }

    public Services getServices() {
        return this.services;
    }


    public void setServices(Services services) {
        this.services = services;
    }

    public Agent[] getSquad(){
        return squad;
    }

    public void setSquad(Agent[] squad){
         this.squad = squad;
    }




}
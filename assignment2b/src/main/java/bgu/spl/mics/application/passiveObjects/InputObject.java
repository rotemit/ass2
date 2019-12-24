package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.JsonArray;

public class InputObject {
    /*private MissionInfo[] missions;
    private JsonArray inventory;
    private M[] ms;
    private Intelligence[] intelligences;
    private Moneypenny[] moneypennies;
    private TimeService time;
    */
    private Services services;
    private Agent[] squad;
    private JsonArray inventory;

    public class Services{
        private int M;
        private int Moneypenny;
        private MissionInfo[] intelligence;
        private int time;


        public void setM(int ms){
            this.M = ms;
        }

        public void setMoneypenny(int mps){
            this.Moneypenny = mps;
        }

        public void setIntelligences(MissionInfo[] mis){
            this.intelligence = mis;
        }

        public void setTime(int time){
            this.time = time;
        }

        public int getM(){
            return M;
        }

        public int getMPs(){
            return Moneypenny;
        }

        public MissionInfo[] getMIs(){
            return intelligence;
        }

        public int getTime(){
            return time;
        }


    }


}

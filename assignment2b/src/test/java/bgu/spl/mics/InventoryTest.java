package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory testInventory;


    @BeforeEach
    public void setUp(){
        testInventory = testInventory.getInstance();
    }

    @Test
    public void loadTest () {
        String[] inventory = createInventoryTestArray();
        testInventory.load(inventory);
        for (Integer i=0; i<inventory.length; i++){
            assertEquals(testInventory.getItem("gadget"+i.toString()), "gadget"+i.toString());
        }
    }

    private String[] createInventoryTestArray() {
        String[] inventory = new String[3];
        for(Integer i = 0; i<inventory.length; i++){
            inventory[i] = new String("gadget"+i.toString());
        }
        return inventory;
    }


    @Test
    public void testGetItem(){
        String[] inventory = createInventoryTestArray();
        testInventory.load(inventory);
        //check if item is found
        assertTrue(testInventory.getItem("gadget1"));
        //check if a nonexisting item isn't found
        assertFalse(testInventory.getItem("nonexistingItem"));
        //check if method removed first item
        assertFalse(testInventory.getItem("gadget1"));
    }

}

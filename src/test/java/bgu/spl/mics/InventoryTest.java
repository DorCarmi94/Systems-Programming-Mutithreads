package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory myInventory;
    private List<String> myGadgets;
    @BeforeEach
    public void setUp(){
        myInventory= Inventory.getInstance();
        myGadgets= new ArrayList<>(5);
        myGadgets.add("Pen");
        myGadgets.add("Umbrella");
        myGadgets.add("Gun");
        myGadgets.add("Belt");
        myGadgets.add("Coat");

    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");

        //myInventory.getItem();
        //myInventory.load();
        //myInventory.printToFile();


    }

    @Test
    public void testLoad()
    {
        String [] gadgetsToAdd = new String[myGadgets.size()];
        for (int i = 0; i < gadgetsToAdd.length; i++) {
            gadgetsToAdd[i]=myGadgets.get(i);
        }
        myInventory.load(gadgetsToAdd);
    }

    @Test
    public void testGetItem()
    {
        for (int i = 0; i < myGadgets.size(); i++) {
            assertTrue(myInventory.getItem(myGadgets.get(i)));
        }

        String itemToFail="AAA";
        assertFalse(myInventory.getItem(itemToFail));
    }

    public void testPrintToFile()
    {

    }

}

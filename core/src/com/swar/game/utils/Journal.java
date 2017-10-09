package com.swar.game.utils;

import com.swar.game.Models.RecordModel;
import com.swar.game.entities.Player;

import java.util.ArrayList;

/**
 * Created by Koma on 04.08.2017.
 */
public class Journal {
    private static Journal ourInstance = new Journal();

    public static Journal getInstance() {
        return ourInstance;
    }

    public ArrayList<Integer> shipsWhichAreBought = new ArrayList<>();

    public Player player;
    public int money;
    public int level;

    public boolean firstRun = true;
    public ArrayList<float[]> moveHistoryList = new ArrayList<>();

    public ArrayList<RecordModel> recordModels = new ArrayList<RecordModel>();
    public RecordModel recordModel = new RecordModel();

    private Journal() {
    }


}

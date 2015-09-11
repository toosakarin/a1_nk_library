package com.fuhu.konnect.library.paint.effect;

import android.util.Pair;

import java.util.ArrayList;

public abstract class IMultipleWallPaperEffect implements Effect {

	public ArrayList<Pair<Integer, Integer>> m_WallPaperList;
	
	public abstract ArrayList<Pair<Integer, Integer>> getWallPaperResId();
}

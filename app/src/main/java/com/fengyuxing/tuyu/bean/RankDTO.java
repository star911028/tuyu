package com.fengyuxing.tuyu.bean;

import java.io.Serializable;
import java.util.List;

public class RankDTO implements Serializable {


    private List<DataList> rankingArray;//
    private MyRank myRank;

    public List<DataList> getRankingArray() {
        return rankingArray;
    }

    public void setRankingArray(List<DataList> rankingArray) {
        this.rankingArray = rankingArray;
    }

    public MyRank getMyRank() {
        return myRank;
    }

    public void setMyRank(MyRank myRank) {
        this.myRank = myRank;
    }
}

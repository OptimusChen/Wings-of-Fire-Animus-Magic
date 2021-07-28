package com.wingsoffireserver.wingsoffire.Util;

public enum Levels {

    LVL_ONE(1),
    LVL_TWO,
    LVL_THREE,
    LVL_FOUR,
    LVL_FIVE,
    LVL_COUNT;


    private int value;
    private static int nextValue;
    Levels(){
        this(Counter.nextValue);
    }
    Levels(int value){
        this.value = value;
        Counter.nextValue = value + 1;
    }

    public int getValue()
    {
        return value;
    }

    private static class Counter
    {
        private static int nextValue = 0;
    }


}


package com.sumago.latestjavix.Util;

public class MonthDalesData {

    String  _actors;
    int _count;

    public MonthDalesData(String _actors,int _count)
    {
        this._actors=_actors;
        this._count=_count;
    }

    public String get_actors() {
        return _actors;
    }

    public void set_actors(String _actors) {
        this._actors = _actors;
    }

    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
    }
}

package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    long _id = 0L;

    List<TimeEntry> _lst = new ArrayList<>();
    HashMap<Long,TimeEntry> _map = new HashMap<>();

    public InMemoryTimeEntryRepository(){

    }


    @Override
    public TimeEntry create(TimeEntry timeEntry){

        _id = _id + 1;

        timeEntry.setId(_id);

        _map.put(((Long) timeEntry.getId()),timeEntry);

        _lst.add(timeEntry);

        return timeEntry;

    }


    @Override
    public TimeEntry find(long id){

        TimeEntry _findentry =  _map.get(id);


        return _findentry;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry){

        timeEntry.setId(id);

        _map.replace(id,timeEntry);

        return timeEntry;
    }


    @Override
    public void delete(long id){

        _lst.remove(_map.get(id));
        _map.remove(id);

    }

    @Override
    public List<TimeEntry> list(){

        return this._lst;

    }

}

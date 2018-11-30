package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository _invrep;
    private long timeEntryId;
    private TimeEntry expected;

   public TimeEntryController(TimeEntryRepository timeEntryRepository) {
       _invrep = timeEntryRepository;
    }

//    @Autowired
//    public TimeEntryController(JdbcTimeEntryRepository timeEntryRepository) {
//        _invrep = timeEntryRepository;
//    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry entry) {

         TimeEntry _createentry = _invrep.create(entry);


        return new ResponseEntity<TimeEntry>(_createentry,HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {

        TimeEntry _retentry = _invrep.find(timeEntryId);

        if (_retentry != null) {
            return new ResponseEntity<TimeEntry>(_retentry, HttpStatus.OK);
        } else {
            return new ResponseEntity<TimeEntry>(_retentry, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        return new ResponseEntity<List<TimeEntry>>(_invrep.list()
                ,HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable("timeEntryId") long timeEntryId,@RequestBody TimeEntry expected) {
        this.timeEntryId = timeEntryId;
        this.expected = expected;

        TimeEntry _updentry = _invrep.update(timeEntryId,expected);
        if (_updentry != null) {
            return new ResponseEntity<TimeEntry>(_updentry, HttpStatus.OK);

        } else {
            return new ResponseEntity<TimeEntry>(_updentry, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("timeEntryId") long timeEntryId) {

        _invrep.delete(timeEntryId);

        TimeEntry _delentry = _invrep.find(timeEntryId);

        if (_delentry != null) {
            return new ResponseEntity<TimeEntry>(_delentry, HttpStatus.OK);

        } else {
            return new ResponseEntity<TimeEntry>(_delentry, HttpStatus.NO_CONTENT);
        }

    }
}

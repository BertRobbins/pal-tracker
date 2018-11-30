package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {


    private final JdbcTemplate _jdbctemplate;

    long _id = 0L;

    List<TimeEntry> _lst = new ArrayList<>();
    HashMap<Long,TimeEntry> _map = new HashMap<>();

    public JdbcTimeEntryRepository(DataSource dataSource) {
        _jdbctemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry){

        ResultSet rs = null;

        try {
            Connection _conn = _jdbctemplate.getDataSource().getConnection();


            PreparedStatement statement = _conn.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            statement.execute();

            int autoIncKeyFromApi = -1;
            rs = statement.getGeneratedKeys();

            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);

                System.out.println(autoIncKeyFromApi);

            } else {

                // throw an exception from here
            }

            timeEntry.setId(autoIncKeyFromApi);

            _conn.close();
            _conn = null;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timeEntry;

    }


    @Override
    public TimeEntry find(long id){

        ResultSet rs = null;

        TimeEntry _entry = null;

        try {
            Connection _conn = _jdbctemplate.getDataSource().getConnection();


            Statement statement = _conn.createStatement();

            statement.execute("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = "+id );

            rs =  statement.getResultSet();

            if (rs != null){

                while(rs.next()) {
                    _entry = new TimeEntry(id, rs.getInt("project_id"), rs.getInt("user_id"), rs.getDate("date").toLocalDate(),
                            rs.getInt("hours"));
                }

            }
            _conn.close();
            _conn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _entry;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry){

        ResultSet rs = null;

        TimeEntry _entry = null;

        try {
            Connection _conn = _jdbctemplate.getDataSource().getConnection();


            PreparedStatement statement = _conn.prepareStatement("UPDATE time_entries " +
                            "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                            "WHERE id = ? ");

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            statement.setLong(5, id);

            statement.execute();
            _conn.close();
            _conn = null;

            return find(id);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _entry;
    }


    @Override
    public void delete(long id){

        ResultSet rs = null;

        TimeEntry _entry = null;

        try {
            Connection _conn = _jdbctemplate.getDataSource().getConnection();


            Statement statement = _conn.createStatement();

            statement.execute("DELETE FROM time_entries WHERE id = "+id );

            _conn.close();
            _conn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TimeEntry> list(){

        ResultSet rs = null;

        TimeEntry _entry = null;
        List<TimeEntry> _lst = new ArrayList<>();

        try {
            Connection _conn = _jdbctemplate.getDataSource().getConnection();


            Statement statement = _conn.createStatement();

            statement.execute("SELECT id, project_id, user_id, date, hours FROM time_entries");

            rs =  statement.getResultSet();

            if (rs != null){

                while(rs.next()) {
                    _entry = new TimeEntry(rs.getInt("id"), rs.getInt("project_id"), rs.getInt("user_id"), rs.getDate("date").toLocalDate(),
                            rs.getInt("hours"));

                    _lst.add(_entry);
                }

            }

            _conn.close();
            _conn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _lst;

    }
}

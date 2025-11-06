package com.example.tasksapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tasksapp.model.Task;

import java.util.List;

@Dao
public interface TaskDao {


    @Query("SELECT * from tasks")
    LiveData<List<Task>> getAllTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Task> tasks);

    @Query("DELETE FROM tasks")
    void deleteAll();
}

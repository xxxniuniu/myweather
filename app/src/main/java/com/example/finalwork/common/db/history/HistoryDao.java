package com.example.finalwork.common.db.history;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(HistoryLog ...historyLogs);

    @Update
    void updateHistory(HistoryLog ...historyLogs);

    @Delete
    void deleteHistory(HistoryLog ...historyLogs);

    @Query("SELECT * FROM tb_history WHERE username=:username GROUP BY city")
    List<HistoryLog> getHistoryLogByUsername(String username);

    @Query("SELECT DISTINCT username FROM tb_history WHERE city=:nowcity")
    List<String> getUsernameIfCityInHistory(String nowcity);

    //
    @Query("SELECT * FROM tb_history WHERE username=:username AND city=:nowcity")
    List<HistoryLog> getHistoryLogByUsernameAndCity(String username,String nowcity);

    // 获取用户浏览历史中的城市名
    @Query("SELECT DISTINCT city FROM tb_history WHERE username=:username")
    List<String> getAllCityInHistoryByUsername(String username);

    @Query("SELECT tem FROM tb_history WHERE username=:username AND city=:city")
    List<String> getTemByUsernameAndCity(String username,String city);

    @Query("UPDATE tb_history SET username=:newname WHERE username=:oldname")
    int updateUsername(String newname,String oldname);

    @Query("SELECT date FROM tb_history WHERE username=:username AND city=:city")
    List<String> getDateByUsernameAndCity(String username,String city);

    // 获取用户浏览历史中的城市名，并限制个数
    @Query("SELECT DISTINCT city FROM tb_history WHERE username=:username ORDER BY id DESC LIMIT :citynum")
    List<String> getAllCityInHistoryByUsernameLimitCitynum(String username,int citynum);
}
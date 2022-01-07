package com.example.finalwork.common.db.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(MyUser ...users);

    @Update
    void updateUser(MyUser ...users);

    @Delete
    void deleteUser(MyUser ...users);

    // 获得所有用户名
    @Query("SELECT username FROM tb_user")
    List<String> getAllUsernames();
    // 获得所有MyUser
    @Query("SELECT * FROM tb_user ORDER BY id DESC")
    List<MyUser> getAllUsers();
    // 根据用户名获得密码
    @Query("SELECT passwd FROM tb_user WHERE username=:name")
    String getPasswd(String name);
    // 获取登陆状态为1的用户名
    @Query("SELECT username FROM tb_user WHERE login=1")
    String getUsernameByLogin();

    @Query("SELECT login FROM tb_user WHERE username=:name")
    int getLoginByUsername(String name);

    @Query("SELECT passwd FROM tb_user WHERE username=:name")
    String getPasswdByUsername(String name);

    @Query("UPDATE tb_user SET login=1 WHERE username=:name")
    int updateUserLoginByUsernameIn(String name);

    @Query("UPDATE tb_user SET login=0 WHERE username=:name")
    int updateUserLoginByUsernameOut(String name);

    @Query("UPDATE tb_user SET login=0 WHERE username<>:name")
    int updateUserLoginByUsernameAllOut(String name);

    @Query("UPDATE tb_user SET username=:name WHERE login=1")
    int updateUsername(String name);

    @Query("UPDATE tb_user SET passwd=:pwd WHERE login=1")
    int updatePasswd(String pwd);

    @Query("UPDATE tb_user SET city=:nowcity WHERE username=:username")
    int updateCity(String nowcity,String username);

    @Query("UPDATE tb_user SET citynum=:newcitynum WHERE login=1")
    int updateCitynum(int newcitynum);

    @Query("SELECT city FROM tb_user WHERE username=:username")
    String getCityByUsername(String username);

    @Query("UPDATE tb_user SET city=:nowcity WHERE login=1")
    int updateCityByLogin(String nowcity);

    @Query("SELECT citynum FROM tb_user WHERE username=:username")
    int getCitynumByUsername(String username);

    @Query("SELECT focuscity FROM tb_user WHERE username=:username")
    String getFocuscityByUsername(String username);

    @Query("UPDATE tb_user SET focuscity=:focus WHERE username=:username")
    int updateFocuscityByUsername(String focus,String username);


}

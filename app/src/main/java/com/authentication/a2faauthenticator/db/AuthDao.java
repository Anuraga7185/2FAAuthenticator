package com.authentication.a2faauthenticator.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.authentication.a2faauthenticator.entity.AuthenticationDetail;

import java.util.List;

@Dao
public interface AuthDao {

    @Insert
    void insert(AuthenticationDetail authenticationDetail);

    @Query("SELECT * FROM auth_creds")
    LiveData<List<AuthenticationDetail>> getAllAccounts();

    @Query("Select * from auth_creds where secretKey = :secretKey")
    AuthenticationDetail checkExistence(String secretKey);

}

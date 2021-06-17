package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.model.Divida;

import java.util.List;

@Dao
public interface DividaDao {
    @Query("SELECT * FROM Divida")
    List<Divida> getAll();

    @Update
    void update(Divida divida);

    @Insert
    void insertAll(Divida... dividas);

    @Delete
    void delete(Divida model);

    @Query("DELETE FROM Divida WHERE id = :Id")
    int deleteByUserId(long Id);

    @Query("SELECT SUM(valor) FROM Divida WHERE pago = 1")
    double totalPago();

    @Query("SELECT SUM(valor) FROM Divida WHERE pago = 0")
    double totalNaoPago();

    @Query("SELECT * FROM Divida WHERE pago = 1 and categoria =:categoria")
    double totalPagasCategoria(int categoria);

    @Query("SELECT * FROM Divida WHERE pago = 0 and categoria =:categoria")
    double totalNaoPagasCategoria(int categoria);
}

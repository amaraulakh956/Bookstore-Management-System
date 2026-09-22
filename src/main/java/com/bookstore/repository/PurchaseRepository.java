package com.bookstore.repository;

import com.bookstore.model.PurchaseRecord;

import java.util.List;

public interface PurchaseRepository {

    List<PurchaseRecord> findAll();

    void save(PurchaseRecord record);
}

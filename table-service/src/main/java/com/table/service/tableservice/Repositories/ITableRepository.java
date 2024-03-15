package com.table.service.tableservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.table.service.tableservice.Model.TableModel;

@Repository
public interface ITableRepository extends JpaRepository<TableModel, Long>{

}

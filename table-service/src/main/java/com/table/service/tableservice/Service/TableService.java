package com.table.service.tableservice.Service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.table.service.tableservice.Model.TableModel;
import com.table.service.tableservice.Repositories.ITableRepository;

@Service
public class TableService {

    @Autowired
    ITableRepository tableRepository;


    public ArrayList<TableModel> getTables(){
        return (ArrayList<TableModel>) tableRepository.findAll();
    }

    public TableModel saveTable( TableModel tableModel){
        return tableRepository.save(tableModel);
    }

    public Optional<TableModel> getTableById(Long id){
        return tableRepository.findById(id);
    }

    public TableModel updateTableDisponibility(Long id, Boolean available){
        Optional<TableModel> tableOptional = tableRepository.findById(id);
        if (tableOptional.isPresent()) {
            TableModel table = tableOptional.get();
            table.setAvailable(available);
            return tableRepository.save(table);
        }else{
            return null;
        }
    }
}

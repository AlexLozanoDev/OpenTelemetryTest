package com.table.service.tableservice.Service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.table.service.tableservice.Model.TableModel;
import com.table.service.tableservice.Repositories.ITableRepository;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

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
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.TableId", id);
            
            return tableRepository.findById(id);

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    public TableModel updateTableDisponibility(Long id, Boolean available){
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.TableId", id);
            
            Optional<TableModel> tableOptional = tableRepository.findById(id);
            if (tableOptional.isPresent()) {
                TableModel table = tableOptional.get();
                table.setAvailable(available);
                return tableRepository.save(table);
            }else{
                return null;
            }

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}

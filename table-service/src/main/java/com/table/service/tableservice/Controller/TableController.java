package com.table.service.tableservice.Controller;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.table.service.tableservice.Model.TableModel;
import com.table.service.tableservice.Service.TableService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/table")
public class TableController {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableService tableService;

    @GetMapping
    public ArrayList<TableModel> gettTableModels(){
        LOGGER.info("Se consulto todas las mesas");
        return this.tableService.getTables();
    }

    @PostMapping
    public TableModel saveTable (@RequestBody TableModel tableModel){
        LOGGER.info("Se creo una mesa nueva");
        return this.tableService.saveTable(tableModel);
    }

    @GetMapping(path = "/{id}")
    public Optional<TableModel> getTableById(@PathVariable("id") Long id){
        LOGGER.info("Se consulto la mesa " + id);
        return this.tableService.getTableById(id);
    }

    @PutMapping(path = "/{id}")
    public TableModel updateAvailabilityTable(@RequestBody TableModel request, @PathVariable("id") Long id){
        LOGGER.info("Se actializo la mesa " + id);
        return this.tableService.updateTableDisponibility(id, request.getAvailable());
    }

}

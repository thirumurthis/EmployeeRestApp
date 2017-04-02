/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package com.example.rest;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeDAO edao = new EmployeeListDAO();

    // Get all employees
    @RequestMapping(method = RequestMethod.GET)
    public Employee[] getAll() {
        return edao.getAllEmployees().toArray(new Employee[0]);
    }

    // Get an employee
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity get(@PathVariable long id) {

        Employee match = null;
        match = edao.getEmployee(id);
        
        if (match != null) {
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get employees by lastName (Week 2)
    @RequestMapping(method = RequestMethod.GET, value = "/lastname/{name}")
    public ResponseEntity getByLastName(@PathVariable String name) {

        List<Employee> lastNameMatch = null;
        lastNameMatch =
                edao.getAllEmployees().stream().filter(a->(name!=null && a.getLastName().toLowerCase().contains(name.toLowerCase())))
                        .collect(Collectors.toList());
        if(lastNameMatch!=null && lastNameMatch.size()>0)
       return new ResponseEntity(lastNameMatch,HttpStatus.OK);
        else
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);

    }

    // Get employee by title (Week 2)
    @RequestMapping(method=RequestMethod.GET, value="/title/{value}")
    public ResponseEntity getByTitle(@PathVariable String value){
        List<Employee> titleMatch= null;
        titleMatch = edao.getAllEmployees().stream().filter(a->(value!=null && a.getTitle().toLowerCase().contains(value.toLowerCase())))
                .collect(Collectors.toList());
        if(titleMatch!=null && titleMatch.size()>0)
            return new ResponseEntity(titleMatch, HttpStatus.OK);
        else return new ResponseEntity(null,HttpStatus.NOT_FOUND);
    }


    // Get employee by dept (Week 2)
    @RequestMapping(method=RequestMethod.GET, value="/department/{deptname}")
    public ResponseEntity getByDept(@PathVariable String deptname){
        List<Employee> deptNameList = null;
        deptNameList = edao.getAllEmployees().stream().filter(dept->
                (deptname!=null && dept.getDept().toLowerCase().contains(deptname.toLowerCase())))
        .collect(Collectors.toList());
        if(deptNameList!=null && deptNameList.size()>0)
            return new ResponseEntity(deptNameList,HttpStatus.OK);
        else return  new ResponseEntity(null,HttpStatus.NOT_FOUND);
    }

    
    // Add an employee
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody Employee employee) {
        if (edao.add(employee)) {
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an employee
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody Employee employee) {

        if (edao.update(id, employee)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Delete a employee (Week 3)
    @RequestMapping(method = RequestMethod.DELETE,value = "{id}")
    public ResponseEntity deleteEmployee(@PathVariable long id){
        Employee employeeObjToDel = edao.getAllEmployees().stream().filter(empId->(id==empId.getId())).findFirst().orElse(null);
        if(employeeObjToDel!=null) {
            edao.getAllEmployees().remove(employeeObjToDel);
            return new ResponseEntity("Employee: "+id+" deleted successfully!!! ",HttpStatus.OK);
        }else
        {
            return new ResponseEntity(null,HttpStatus.NOT_FOUND);
        }
    }
    

}

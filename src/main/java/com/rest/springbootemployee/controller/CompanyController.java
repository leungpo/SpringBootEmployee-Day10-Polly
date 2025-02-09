package com.rest.springbootemployee.controller;

import com.mongodb.lang.NonNull;
import com.rest.springbootemployee.controller.dto.CompanyRequest;
import com.rest.springbootemployee.controller.dto.CompanyResponse;
import com.rest.springbootemployee.controller.dto.EmployeeResponse;
import com.rest.springbootemployee.controller.mapper.CompanyMapper;
import com.rest.springbootemployee.controller.mapper.EmployeeMapper;
import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.exception.InvalidIdException;
import com.rest.springbootemployee.service.CompanyService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;
    private CompanyMapper companyMapper;
    private EmployeeMapper employeeMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper, EmployeeMapper employeeMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public List<CompanyResponse> getAll() {
        return companyMapper.toResponseList(companyService.findAll());
    }

    @GetMapping("/{id}")
    public CompanyResponse getById(@NonNull @PathVariable String id) {
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException();
        }
        return companyMapper.toResponse(companyService.findById(id));
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeResponse> getEmployees(@NonNull @PathVariable String id) {
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException();
        }
        return employeeMapper.toResponseList(companyService.getEmployees(id));
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<CompanyResponse> getByPage(Integer page, Integer pageSize) {
        return companyMapper.toResponseList(companyService.findByPage(page, pageSize));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompanyResponse create(@RequestBody CompanyRequest companyRequest) {
        Company company = companyMapper.toEntity(companyRequest);
        Company createdCompany = companyService.create(company);
        return companyMapper.toResponse(createdCompany);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@NonNull @PathVariable String id, @RequestBody CompanyRequest companyRequest) {
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException();
        }
        Company company = companyMapper.toEntity(companyRequest);
        Company updatedCompany = companyService.update(id, company);
        return companyMapper.toResponse(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@NonNull @PathVariable String id) {
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException();
        }
        companyService.delete(id);
    }
}

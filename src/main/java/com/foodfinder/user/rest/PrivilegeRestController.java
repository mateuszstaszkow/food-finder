package com.foodfinder.user.rest;

import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/privileges")
class PrivilegeRestController {

    private final PrivilegeService privilegeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<PrivilegeDTO> getPrivilegeList(Pageable pageable) {
        return privilegeService.getPrivilegeList(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addPrivilege(@RequestBody PrivilegeDTO privilegeDTO) {
        privilegeService.postPrivilege(privilegeDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PrivilegeDTO getPrivilege(@PathVariable Long id) {
        return privilegeService.getPrivilege(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updatePrivilege(@PathVariable Long id, @RequestBody PrivilegeDTO privilegeDTO) {
        privilegeService.updatePrivilege(id, privilegeDTO);
    }
}
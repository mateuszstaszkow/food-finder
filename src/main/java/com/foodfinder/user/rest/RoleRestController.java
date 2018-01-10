package com.foodfinder.user.rest;

import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/roles")
class RoleRestController {

    private final RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<RoleDTO> getRoleList(Pageable pageable) {
        return roleService.getRoleList(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addRole(@RequestBody RoleDTO roleDTO) {
        roleService.postRole(roleDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public RoleDTO getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(id, roleDTO);
    }
}
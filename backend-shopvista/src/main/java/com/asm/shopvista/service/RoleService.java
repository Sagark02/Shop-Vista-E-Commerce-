package com.asm.shopvista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asm.shopvista.entity.Role;
import com.asm.shopvista.repository.RoleRepository;


@Service
public class RoleService{

    @Autowired
    private RoleRepository roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
}

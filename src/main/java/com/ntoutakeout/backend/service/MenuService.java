package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElse(null);
    }
}

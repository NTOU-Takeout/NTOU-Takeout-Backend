package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public void validateParameters(Map<String, String[]> parameters) throws InvalidParameterException {
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (!key.equals("keyword") && !key.equals("sortBy") && !key.equals("sortDir"))
                throw new InvalidParameterException("Invalid parameter: " + key);
            if (value.length != 1)
                throw new InvalidParameterException("Invalid number of parameters");
        }
    }

    public List<String> getStoresIdFilteredAndSorted(String keyword, String sortBy, String sortDir) throws InvalidParameterException {
        if(!sortBy.equals("rating") && !sortBy.equals("averageSpend") && !sortBy.equals("name"))
            throw new InvalidParameterException("Invalid sort by: " + sortBy);
        if(!sortDir.equals("asc") && !sortDir.equals("desc"))
            throw new InvalidParameterException("Invalid sort dir: " + sortDir);

        List<Store> stores;
        switch (sortBy) {
            case "name" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByNameDesc(keyword);
            case "rating" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByRatingAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByRatingDesc(keyword);
            case "averageSpend" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByAverageSpendAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByAverageSpendDesc(keyword);
            default -> {
                return null;
            }
        }

        return stores.stream().map(Store::getId).toList();
    }

    public List<Store> getStoreByIds(List<String> ids) {
        return ids.stream()
                .map(storeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}

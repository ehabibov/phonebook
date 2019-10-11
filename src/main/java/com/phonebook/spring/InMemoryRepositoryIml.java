package com.phonebook.spring;

import com.phonebook.main.InMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Keeps phoneBook data in memory in ordered in accordance to addition.
 */
@Repository
public class InMemoryRepositoryIml implements InMemoryRepository {

    private Map<String, Set<String>> data;

    /**
     * no args constructor
     */
    public InMemoryRepositoryIml() {
        // LinkedHashMap is chosen because usually iteration order matters
        this(new LinkedHashMap<>());
    }

    /**
     * this constructor allows to inject initial data to the repository
     *
     * @param data
     */
    public InMemoryRepositoryIml(Map<String, Set<String>> data) {
        this.data = new LinkedHashMap<>(data);
    }

    @Override
    public Map<String, Set<String>> findAll() {
        return new LinkedHashMap<>(this.data);
    }

    @Override
    public Set<String> findAllPhonesByName(String name) {
        Set<String> numbers = data.get(name);
        return numbers == null ? null : new HashSet<>(numbers);
    }

    @Override
    public String findNameByPhone(String phone) {
        String name = null;
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            if (entry.getValue().contains(phone)) {
                name = entry.getKey();
            }
        }
        return name;
    }

    @Override
    public void addPhone(String name, String phone) throws IllegalArgumentException {
        String owner = this.findNameByPhone(phone);
        if (owner != null){
            throw new IllegalArgumentException("Phone number already assigned to: " + owner);
        }
        if (data.containsKey(name)){
            data.get(name).add(phone);
        } else {
            data.put(name, new HashSet<String>() {{add(phone);}});
        }
    }

    @Override
    public void removePhone(String phone) throws IllegalArgumentException {
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            if (entry.getValue().contains(phone)) {
                entry.getValue().remove(phone);
                if (entry.getValue().isEmpty()) {
                    data.entrySet().remove(entry);
                }
                return;
            }
        }
        throw new IllegalArgumentException("No such phone number");
    }
}
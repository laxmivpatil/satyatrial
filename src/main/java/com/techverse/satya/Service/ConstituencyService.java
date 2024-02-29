package com.techverse.satya.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.Constituency;
import com.techverse.satya.Repository.ConstituencyRepository;

import java.util.List;

@Service
public class ConstituencyService {
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public ConstituencyService(ConstituencyRepository constituencyRepository) {
        this.constituencyRepository = constituencyRepository;
    }

    public List<Constituency> listAllConstituencies() {
        return constituencyRepository.findAll();
    }

    public Constituency saveConstituency(Constituency constituency) {
        return constituencyRepository.save(constituency);
    }

    public Constituency getConstituencyById(Long id) {
        return constituencyRepository.findById(id).orElse(null);
    }

    public void deleteConstituency(Long id) {
        constituencyRepository.deleteById(id);
    }
    public List<Constituency> getConstituenciesByDistrictName(String districtName) {
    	 return constituencyRepository.findByDistrictNameIgnoreCase(districtName);
    }
}

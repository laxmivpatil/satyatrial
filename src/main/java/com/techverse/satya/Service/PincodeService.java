package com.techverse.satya.Service;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.Pincode;
import com.techverse.satya.Repository.PincodeRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PincodeService {

    private final PincodeRepository pincodeRepository;

    @Autowired
    public PincodeService(PincodeRepository pincodeRepository) {
        this.pincodeRepository = pincodeRepository;
    }
    public List<Pincode> readAndStoreData(String filePath) {
        List<Pincode> pincodeList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

            Iterator<Row> rowIterator = sheet.iterator();
            // Skip the header row if it exists
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String pincode = new DecimalFormat("#").format(row.getCell(0).getNumericCellValue());
                 
                String district = row.getCell(1).getStringCellValue();
               
                String state = row.getCell(2).getStringCellValue();
                

                pincodeList.add(new Pincode(pincode, district, state));
            }
            System.out.println(pincodeList.size());

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return pincodeList;
    }

    public void saveAll(List<Pincode> pincodes) {
    	System.out.println("hi save all");
        // Create a set to keep track of unique PIN codes
        Set<String> uniquePincodes = new HashSet<>();
        List<Pincode> pincodeList = new ArrayList<>();

        for (Pincode pincode : pincodes) {
            String currentPincode = pincode.getPincode();

            // Check if the current PIN code is unique
            if (!uniquePincodes.contains(currentPincode)) {
                // Save the Pincode if it is unique
                
                 
                // Add the PIN code to the set to mark it as processed
                uniquePincodes.add(currentPincode);
                pincodeList.add(pincode);
            } else {
                // Handle duplicate entry (you may choose to skip or update based on your business logic)
                // For now, we skip the duplicate entry
               // System.out.println("Duplicate entry found for PIN code: " + currentPincode);
            }
        }
        System.out.println("pincodeList "+pincodeList.size());
        pincodeRepository.saveAll(pincodeList);
    }

    public List<Pincode> getAllPincodes() {
        return pincodeRepository.findAll();
    }
    
    public Optional<Pincode> getcitystate(String pincode){
    	
    	return pincodeRepository.findByPincode(pincode);
    }

    // Add other service methods as needed

}

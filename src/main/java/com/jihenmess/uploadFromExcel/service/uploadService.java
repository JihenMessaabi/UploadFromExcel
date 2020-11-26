package com.jihenmess.uploadFromExcel.service;

import com.jihenmess.uploadFromExcel.enumeration.Gender;
import com.jihenmess.uploadFromExcel.model.Account;
import com.jihenmess.uploadFromExcel.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Jihen.Messaabi
 */

@Service
@Slf4j
public class uploadService {

    @Autowired
    private AccountRepository accountRepository;

    @Value("${excel.path}")
    private String path;

    public void uploadFile() {
        if (accountRepository.count() > 0)
            return;
        File file = null;
        try {
            file = ResourceUtils.getFile(path);
            saveAccounts(file);
            log.info("File Uploaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveAccounts(File file) {
        try {
            List<Account> accounts = excelToAccounts(new FileInputStream(file));
            accountRepository.saveAll(accounts);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }


    public List<Account> excelToAccounts(FileInputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            List<Account> accounts = new ArrayList<Account>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                //Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                rowNumber++;
                if (rowNumber > sheet.getLastRowNum()+1) {
                    break;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Account account = new Account();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            account.setFirstName((currentCell.getCellType() == CellType.STRING) ? currentCell.getStringCellValue() : "");
                            break;

                        case 1:
                            account.setLastName((currentCell.getCellType() == CellType.STRING) ? currentCell.getStringCellValue() : "");
                            break;

                        case 2:
                            account.setGender((currentCell.getCellType() == CellType.STRING && (currentCell.getStringCellValue().equals("MALE") || currentCell.getStringCellValue().equals("FEMALE"))) ? Gender.valueOf(currentCell.getStringCellValue()) : null);
                            break;

                        case 3:
                            account.setAge((currentCell.getCellType() == CellType.NUMERIC) ? (int) currentCell.getNumericCellValue() : null);
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                accounts.add(account);
            }

            workbook.close();

            return accounts;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


}

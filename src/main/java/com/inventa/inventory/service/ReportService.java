package com.inventa.inventory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.repository.TransactionRepository;

@Service
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Page<Transaction> getReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable);
    }

    public List<Transaction> getFilteredReports(LocalDate from, LocalDate to, String status) {
        List<Transaction> reports = transactionRepository.findAll();
        Stream<Transaction> stream = reports.stream();

        // mengambil data yang datenya >= from
        if(from != null) {
            stream = stream.filter(item -> !item.getDate().isBefore(from));
        }

        // mengambil data yang datenya <= to
        if(to != null) {
            stream = stream.filter(item -> !item.getDate().isAfter(to));
        }

        // mengambil data yang sesuai dengan status yang diinginkan
        if(status != "") {
            stream = stream.filter(item -> item.getStatus().equals(status));
        }

        reports = stream.collect(Collectors.toList());

        return reports;
    }
}

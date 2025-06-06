package com.inventa.inventory.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.PdfExporter;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.service.ReportService;

@Controller("reportController")
@RequestMapping("/admin")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report-management")
    public String redirectToReportManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        int pageSize = 5;
        Page<Transaction> reports = reportService.getReports(page, pageSize);
        
        setModelAttributes(reports, null, null, "", page, model);

        return "admin/report-management";
    }

    @GetMapping("/report-management/filter")
    public String redirectToFilteredReportManagement(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) String status, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        int pageSize = 5;
        List<Transaction> data = reportService.getFilteredReports(from, to, status);
        
        // buat menjadi page
        int start = page * pageSize;
        int end = Math.min(start + pageSize, data.size());
        List<Transaction> pageContent = data.subList(start, end);
        Page<Transaction> reports = new PageImpl<>(pageContent, PageRequest.of(page, pageSize), data.size());

        setModelAttributes(reports, from, to, status, page, model);

        return "admin/report-management";
    }

    @GetMapping("/report-management/download-pdf")
    public String downloadReport(HttpServletResponse response, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) String status, HttpSession session) throws IOException {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

        List<Transaction> filteredReports = reportService.getFilteredReports(from, to, status);

        try {
            PdfExporter.export(filteredReports, from, to, status, response.getOutputStream());
            response.getOutputStream().flush();
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to Download PDF");
        }

        return "admin/report-management";
    }

    public void setModelAttributes(Page<Transaction> reports, LocalDate from, LocalDate to, String status, int page, Model model) {
        model.addAttribute("reports", reports.getContent());
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", reports.getTotalPages());
    }
}

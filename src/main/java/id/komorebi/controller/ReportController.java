package id.komorebi.controller;

import id.komorebi.model.ReportEntry;
import id.komorebi.service.ReportingService;
import id.komorebi.util.UIHelper;

import java.time.LocalDate;
import java.util.List;

public class ReportController {

    private final ReportingService service;

    public ReportController(ReportingService service) {
        this.service = service;
    }

    public String loadReport() {
        LocalDate today = LocalDate.now();
        try {
            double totalSales = service.getTotalSales(today);
            int transactionCount = service.getTransactionCount(today);
            List<ReportEntry> topItems = service.getTopMenuItems(today);
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== Daily Sales Report for ").append(today).append(" ===").append("\n\n");
            sb.append("Total Sales: Rp ").append(String.format("%.2f", totalSales)).append("\n");
            sb.append("Transaction Count: ").append(transactionCount).append("\n\n");
            sb.append("--- Top Selling Items ---\n");
            for (ReportEntry entry : topItems) {
                sb.append(entry.getName()).append(": ").append(entry.getQuantitySold()).append(" sold, Revenue: Rp ").append(entry.getRevenue()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            UIHelper.showError("Failed to generate report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    public double getDailyTotal(LocalDate date) {
        try {
            return service.getTotalSales(date);
        } catch (Exception e) {
            UIHelper.showError("Failed to get daily total: " + e.getMessage());
            return 0.0;
        }
    }

    public int getTransactionCount(LocalDate date) {
        try {
            return service.getTransactionCount(date);
        } catch (Exception e) {
            UIHelper.showError("Cannot load transaction count: " + e.getMessage());
            return 0;
        }
    }
}

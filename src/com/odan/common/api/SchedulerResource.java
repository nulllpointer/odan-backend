package com.odan.common.api;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.odan.common.json.JSONValidatorEngine;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/scheduler")
public class SchedulerResource extends RestAction {

	@Action(value = "create_sales_invoice", results = { @Result(type = "json") })
	public String createSalesInvoice() {
//		boolean success = SalesModel.createSalesInvoice();
//		if (success) {
//			setSuccess();
//		} else {
//			getData().put("log", APILogger.getList());
//			setError("Error Creating Sales Invoice");
//			APILogger.clear();
//		}

		return SUCCESS;
	}

	@Action(value = "sales/invoiceable", results = { @Result(type = "json") })
	public String getSalesInvoiceable() {
		setSuccess();

		try {
			JSONValidatorEngine.validateRequest("sales/get", getRequest());
//			List<Sales> sales = SalesModel.getInvoiceableSales();
//			getData().put("sales", sales);

		} catch (IOException | ProcessingException e) {
			// TODO Auto-generated catch block
			setError("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		return SUCCESS;
	}

	@Action(value = "invoice/recurring", results = { @Result(type = "json") })
	public String getRecurringInvoice() throws IOException {
		setSuccess();

		// JSONValidatorEngine.validateRequest("sales/get", getRequest());
//		List<Invoice> invoices = InvoiceModel.getRecurrableInvoices();
//		getData().put("invoices", invoices);

		return SUCCESS;
	}

	@Action(value = "create_recurring_invoice", results = { @Result(type = "json") })
	public String createRecurringInvoice() {
//		boolean success = InvoiceModel.createRecurInvoice(InvoiceModel.getRecurrableInvoices());
//		if (success) {
//			setSuccess();
//		} else {
//			getData().put("log", APILogger.getList());
//			setError("Error Creating Recurring Invoice");
//			APILogger.clear();
//		}

		return SUCCESS;
	}

	@Action(value = "update_invoice_duestatus", results = { @Result(type = "json") })
	public String updateInvoiceDueStatus() {
//		boolean success = InvoiceModel.updateInvoiceDueStatus();
//		if (success) {
//			setSuccess();
//		} else {
//			getData().put("log", APILogger.getList());
//			setError("Error setting Invoice status");
//			APILogger.clear();
//		}

		return SUCCESS;
	}

	@Action(value = "create_invoice_journal", results = { @Result(type = "json") })
	public String createInvoiceJournal() {
		boolean success = true;
//		List<Invoice> invoices = InvoiceModel.getPendingJournalInvoices();

		try {
			// excluding future date invoices.
//			for (Invoice inv : invoices) {
//				
//				InvoiceType type = InvoiceType.SENT;
//				if(inv.getType() == InvoiceType.RECEIVED.getFlag()) {
//					type = InvoiceType.RECEIVED;
//				}
//				AccountingJournalModel.createInvoiceEntry(inv, type);
//			}
			APILogger.add(APILogType.SUCCESS, "Journal Entry created");
		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Journal Entry Creation Failed");
			APILogger.add(APILogType.ERROR, ex.getMessage());
			success = false;
		}

		if (success) {
			setSuccess();
		} else {
			getData().put("log", APILogger.getList());
			setError("Error creating journal entries");
			APILogger.clear();
		}

		return SUCCESS;
	}

}

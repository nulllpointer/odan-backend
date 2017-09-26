package com.odan.billing.invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odan.billing.invoice.model.Invoice;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.InvoiceStatus;
import com.odan.common.model.Flags.InvoiceType;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;

public class InvoiceQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		Invoice invoice = (Invoice) HibernateUtils.get(Invoice.class, id);
		return invoice;

	}

	@Override
	public List<Object> get(Query q) {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		String whereSQL = " WHERE 1=1 ";

		int page = 1;
		int limit = 10;
		int offset = 0;
		if (q.has("page")) {
			page = Parser.convertObjectToInteger(q.get("page"));
		}

		if (q.has("limit")) {
			limit = Parser.convertObjectToInteger(q.get("limit"));
		}

		if (q.has("ownerId")) {
			whereSQL += " AND ownerId = :ownerId ";
			sqlParams.put("ownerId", q.get("ownerId"));
		}

		if (q.has("account_number")) {
			whereSQL += " AND account_number = :accountNumber ";
			sqlParams.put("accountNumber", q.get("account_number"));
		}

		// Invoice Number
		if (q.has("invoiceNumber")) {
			String value = (String) q.get("invoiceNumber");
			whereSQL += " AND invoice_number = " + HibernateUtils.s(value);
		}

		// Customer Id
		if (q.has("customerId")) {
			List<String> customerIdList = (List<String>) q.get("customerId");
			if (customerIdList.size() > 0) {
				String whereCustomer = "";
				for (String customerId : customerIdList) {
					whereCustomer += "customer_id = " + HibernateUtils.s(customerId) + " OR ";
				}
				if (whereCustomer.length() > 3) {
					whereCustomer = whereCustomer.substring(0, whereCustomer.length() - 3);
				}
				whereSQL += " AND (" + whereCustomer + ")";
			}
		}

		// User Id
		if (q.has("userId")) {
			List<String> userIdList = (List<String>) q.get("userId");
			if (userIdList.size() > 0) {
				String whereUser = "";
				for (String userId : userIdList) {
					System.out.println(userId);
					System.out.println(HibernateUtils.s(userId));
					whereUser += "user_id = " + HibernateUtils.s(userId) + " OR ";
				}
				if (whereUser.length() > 3) {
					whereUser = whereUser.substring(0, whereUser.length() - 3);
				}
				whereSQL += " AND (" + whereUser + ")";
			}
		}

		// Active Status (Active, Pending)
		if (q.has("userStatus")) {
			List<String> userStatus = (List<String>) q.get("userStatus");
			if (userStatus.size() > 0) {
				String whereStatus = "";
				for (String status : userStatus) {
					whereStatus += "status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}
				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Active Status (Active, Pending)
		if (q.has("customerStatus")) {
			List<String> customerStatus = (List<String>) q.get("customerStatus");
			if (customerStatus.size() > 0) {
				String whereStatus = "";
				for (String status : customerStatus) {
					whereStatus += "customer_status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}
				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Due Status (Undue, Due, Overdue)
		if (q.has("dueStatus")) {
			List<String> dueStatus = (List<String>) q.get("dueStatus");
			if (dueStatus.size() > 0) {
				String whereStatus = "";
				for (String status : dueStatus) {
					whereStatus += "due_status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}
				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Payment Status (Unpaid, Partialpaid, Paid)
		if (q.has("paymentStatus")) {
			List<String> paymentStatus = (List<String>) q.get("paymentStatus");
			if (paymentStatus.size() > 0) {
				String whereStatus = "";
				for (String status : paymentStatus) {
					whereStatus += "payment_status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}
				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Refund Status (Refund Requested, Refund Approved, Refund
		// Rejected)
		if (q.has("refundStatus")) {
			List<String> refundStatus = (List<String>) q.get("refundStatus");
			if (refundStatus.size() > 0) {
				String whereStatus = "";
				for (String status : refundStatus) {
					whereStatus += "refund_status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}
				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Writeoff Status (Written Off)
		if (q.has("writeoffStatus")) {
			List<String> writeoffStatus = (List<String>) q.get("writeoffStatus");
			if (writeoffStatus.size() > 0) {
				String whereStatus = "";
				for (String status : writeoffStatus) {
					whereStatus += "writeoff_status = " + Flags.convertInputToFlag(status, "InvoiceStatus") + " OR ";
				}
				if (whereStatus.length() > 3) {
					whereStatus = whereStatus.substring(0, whereStatus.length() - 3);
				}

				whereSQL += " AND (" + whereStatus + ")";
			}
		}

		// Invoice Mode
		if (q.has("mode")) {
			whereSQL += " AND mode = " + Flags.convertInputToFlag(q.get("mode"), "InvoiceMode");
		}

		if (q.has("invoiceDateStart") ^ q.has("invoiceDateEnd")) {
			APILogger.add(APILogType.WARNING, "Invoice date start or date end missing.");
		} else if (q.has("invoiceDateStart")) {
			whereSQL += " AND DATE(invoiceDate) BETWEEN DATE(" + HibernateUtils.s((String) q.get("invoiceDateStart"))
					+ ") AND DATE(" + HibernateUtils.s((String) q.get("invoiceDateEnd")) + ") ";
		}

		if (q.has("dueDateStart") ^ q.has("dueDateEnd")) {
			APILogger.add(APILogType.WARNING, "Due date start or date end missing.");
		} else if (q.has("dueDateStart")) {
			whereSQL += " AND DATE(dueDate) BETWEEN DATE(" + HibernateUtils.s((String) q.get("dueDateStart"))
					+ ") AND DATE(" + HibernateUtils.s((String) q.get("dueDateEnd")) + ") ";
		}

		if (q.has("invoiceDateAfter")) {
			whereSQL += " AND DATE(invoiceDate) > DATE(" + HibernateUtils.s((String) q.get("invoiceDateAfter")) + ") ";
		}
		if (q.has("invoiceDateBefore")) {
			whereSQL += " AND DATE(invoiceDate) < DATE(" + HibernateUtils.s((String) q.get("invoiceDateBefore")) + ") ";
		}
		if (q.has("dueDateAfter")) {
			whereSQL += " AND DATE(dueDate) > DATE(" + HibernateUtils.s((String) q.get("dueDateAfter")) + ") ";
		}
		if (q.has("dueDateBefore")) {
			whereSQL += " AND DATE(dueDate) < DATE(" + HibernateUtils.s((String) q.get("dueDateBefore")) + ") ";
		}

		if (q.has("recurringPeriod")) {
			whereSQL += " AND recurring_period = " + Flags.convertInputToFlag(q.get("recurringPeriod"), "TimePeriod");
		}

		if (q.has("transactionNumber")) {
			whereSQL += " AND transaction_number = " + HibernateUtils.s((String) q.get("transactionNumber"));
		}

		if (q.has("saleId")) {
			whereSQL += " AND sale_id = " + HibernateUtils.s((String) q.get("saleId"));
		}

		if (q.has("note")) {
			whereSQL += " AND note ILIKE " + HibernateUtils.s((String) q.get("note"));
		}

		offset = (limit * (page - 1));

		List<Object> invoices = HibernateUtils.select("FROM Invoice " + whereSQL, sqlParams, limit, offset);
		return invoices;
	}



	public List<Object> getAttachmentByInvoiceId(String invoiceId) {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("invoiceId", Parser.convertObjectToLong(invoiceId));
		List<Object> attachments = HibernateUtils.select("FROM InvoiceAttachment WHERE invoiceId=:invoiceId",
				sqlParams);
		return attachments;
	}

	public Integer getSalesCountById(Query q) {
		Long productId = Parser.convertObjectToLong(q.get("productId"));
		String sql = "SELECT COUNT(s.id) AS count FROM sales s WHERE s.product_id = :productId";
		HashMap<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("productId", productId);

		List<Map<String, Object>> queryResult = (List<Map<String, Object>>) HibernateUtils.selectSQL(sql, sqlParams);
		Integer count = Parser.convertObjectToInteger(queryResult.get(0).get("count"));
		return count;
	}

	// Get Payable Invoices.
	public List<Invoice> getPayableInvoices(Query q) {
		HashMap<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("paidStatus", InvoiceStatus.PAID.getFlag());
		sqlParams.put("overdueStatus", InvoiceStatus.OVERDUE.getFlag());
		List<Invoice> invoices = HibernateUtils
				.select("FROM Invoice WHERE payment_status != :paidStatus AND due_status != :overdueStatus", sqlParams);
		return invoices;
	}

	public List<Invoice> getRecurrableInvoices() {
		HashMap<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("type", InvoiceType.CUSTOMER_INVOICE.getFlag());
		sqlParams.put("currentTime", DateTime.getCurrentTimestamp());
		List<Invoice> invoices = HibernateUtils
				.select("FROM Invoice WHERE mode = :mode AND type = :type AND nextRecurDate < :currentTime", sqlParams);
		return invoices;
	}

	// Get invoices that are candidate for due status change.
	public List<Invoice> getDueableInvoices() {
		HashMap<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("paidStatus", InvoiceStatus.PAID.getFlag());
		sqlParams.put("overdueStatus", InvoiceStatus.OVERDUE.getFlag());
		List<Invoice> invoices = HibernateUtils
				.select("FROM Invoice WHERE payment_status != :paidStatus AND due_status != :overdueStatus", sqlParams);
		return invoices;
	}

	// Get invoices that are candidate for journal ledger entry.
	public List<Invoice> getPendingJournalInvoices() throws CommandException {
		String sql = "SELECT i.id FROM invoice AS i LEFT JOIN accounting_journal AS aj ON i.id = aj.ref_id "
				+ "WHERE DATE(i.invoice_date) <= DATE(:currentDate) AND aj.id IS NULL;";

		HashMap<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("currentDate", DateTime.getCurrentTimestamp());

		List<Invoice> invoices = new ArrayList<Invoice>();
		List<Map<String, Object>> queryResult = (List<Map<String, Object>>) HibernateUtils.selectSQL(sql, sqlParams);

		for (Map<String, Object> qRow : queryResult) {
			Long invoiceId = Parser.convertObjectToLong(qRow.get("id"));
			Invoice inv = (Invoice) HibernateUtils.get(Invoice.class, invoiceId);
			invoices.add(inv);
		}

		return invoices;
	}



}

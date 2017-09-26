package com.odan.billing.invoice;

import com.odan.billing.catalog.product.model.Product;
import com.odan.billing.customer.CustomerQueryHandler;
import com.odan.billing.customer.model.Customer;
import com.odan.billing.invoice.command.*;
import com.odan.billing.invoice.model.Invoice;
import com.odan.billing.invoice.model.InvoiceItem;
import com.odan.billing.sales.model.Sales;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.*;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvoiceCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateCustomerInvoice.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(AcceptInvoice.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateVendorBill.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateInvoiceItem.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateRecurringInvoice.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateSalesInvoice.class, InvoiceCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateInvoiceDueStatus.class, InvoiceCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateCustomerInvoice) {
			handle((CreateCustomerInvoice) c);
		} else if (c instanceof AcceptInvoice) {
			handle((AcceptInvoice) c);
		} else if (c instanceof CreateVendorBill) {
			handle((CreateVendorBill) c);
		} else if (c instanceof CreateInvoiceItem) {
			handle((CreateInvoiceItem) c);
		} else if (c instanceof CreateRecurringInvoice) {
			handle((CreateRecurringInvoice) c);
		} else if (c instanceof CreateSalesInvoice) {
			handle((CreateSalesInvoice) c);
		} else if (c instanceof UpdateInvoiceDueStatus) {
			handle((UpdateInvoiceDueStatus) c);
		}
	}

	public void handle(CreateCustomerInvoice c) {

		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {

			ArrayList<Object> customerIdList = null;
			List<Invoice> invoiceList = new ArrayList<Invoice>();

			if (c.has("customerId")) {
				customerIdList = (ArrayList<Object>) c.get("customerId");
			}

			InvoiceType invoiceType = (InvoiceType) Flags.convertInputToEnum(c.get("type"), "InvoiceType");
			for (Object _customerId : customerIdList) {
				Long customerId = Parser.convertObjectToLong(_customerId);
				Invoice invoice = this._handleSaveInvoice(c, customerId, invoiceType);

				if (invoice == null) {
					throw new ValidationException("Invoice creation failed for customer id " + customerId);
				}

				invoiceList.add(invoice);
			}

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}

			APILogger.add(APILogType.SUCCESS, "Invoice(s) has been created successfully.");
			c.setObject(invoiceList);
		} catch (CommandException cex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			APILogger.add(APILogType.ERROR, "Invoice Creation Failed", ex);
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(CreateVendorBill c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			c.set("type", "vendor_bill");

			Long vendorId = Parser.convertObjectToLong(c.get("vendorId"));

			Invoice invoice = this._handleSaveInvoice(c, vendorId, InvoiceType.VENDOR_BILL);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}

			APILogger.add(APILogType.SUCCESS, "Invoice(s) has been created successfully.");
			c.setObject(invoice);

		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {

			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	private Invoice _handleSaveInvoice(ICommand c, Long entityId, InvoiceType invoiceType) throws Exception {
		Double subTotal = 0.0;
		Double taxTotal = 0.0;
		Double discountTotal = 0.0;
		Double total = 0.0;
		Double dueAmount = 0.0;

		Double taxRate = 0.0;
		TaxRateType taxRateType = null;

		Double discount = 0.0;
		InvoiceDiscountType discountType = null;

		boolean isNew = false;

		Invoice invoice = null;

		Long ownerId = Authentication.getUserId();
		Long customerId = null, vendorId = null;

		if (invoiceType == InvoiceType.CUSTOMER_INVOICE) {
			customerId = entityId;
		} else if (invoiceType == InvoiceType.VENDOR_BILL) {
			customerId = ownerId;
			vendorId = entityId;
		}

		Customer cust = (Customer) (new CustomerQueryHandler()).getById(customerId);
		boolean isCustomerSystemUser = cust.getUserId() != null;


		// If Timeperiod is other than one time than recurring dates
		// should be defined.
		TimePeriod recurPeriod = (TimePeriod) Flags.convertInputToEnum(c.get("recurringPeriod"), "TimePeriod");
		if (!recurPeriod.equals(TimePeriod.ONE_TIME)) {
			if (!c.has("recurStartDate")) {
				APILogger.add(APILogType.ERROR, "Recur start date is not defined.");
				throw new ValidationException("Recur start date is not defined.");
			}

			if (!c.has("recurEndDate")) {
				APILogger.add(APILogType.ERROR, "Recur end date is not defined.");
				throw new ValidationException("Recur end date is not defined.");
			}
		}

		if (recurPeriod.equals(TimePeriod.ONE_TIME)) {
			if (c.has("recurStartDate")) {
				APILogger.add(APILogType.ERROR, "One time invoice can not have recur start date.");
				throw new ValidationException("One time invoice can not have recur start date.");
			}
			if (c.has("recurEndDate")) {
				APILogger.add(APILogType.ERROR, "One time invoice can not have recur end date.");
				throw new ValidationException("One time invoice can not have recur end date.");
			}
		}

		// If tax rate type is defined then tax rate should be defined
		// too and vice versa.
		if (c.has("taxRateType") && !c.has("taxRate")) {
			APILogger.add(APILogType.ERROR, "Tax rate should be defined.");
			throw new ValidationException("Tax rate should be defined.");
		} else if (c.has("taxRate") && !c.has("taxRateType")) {
			APILogger.add(APILogType.ERROR, "Tax rate type should be defined.");
			throw new ValidationException("Tax rate type should be defined.");
		}

		// If discount type is defined then discount should be defined
		// too and vice versa.
		if (c.has("discountType") && !c.has("discount")) {
			APILogger.add(APILogType.ERROR, "Discount should be defined.");
			throw new ValidationException("Discount should be defined.");
		} else if (c.has("discount") && !c.has("discountType")) {
			APILogger.add(APILogType.ERROR, "Discount type should be defined.");
			throw new ValidationException("Discount type should be defined.");
		}

		List<HashMap<String, Object>> itemsData = (List<HashMap<String, Object>>) c.get("invoiceItems");

		// Set restriction for maximum number of invoice items.
		if (itemsData.size() > 20) {
			APILogger.add(APILogType.ERROR, "Invoice can have max 20 items.");
			throw new ValidationException("Invoice can have max 20 items.");
		}

		if (c.has("id")) {
			invoice = (Invoice) HibernateUtils.get(Invoice.class, Parser.convertObjectToLong(c.get("id")));
			invoice.setId(Parser.convertObjectToLong(c.get("id")));
			isNew = false;
		} else {
			invoice = new Invoice();
			isNew = true;
		}

		if (isNew && invoiceType == InvoiceType.CUSTOMER_INVOICE) {
			invoice.setInvoiceNumber(this._generateInvoiceNumber());
		} else if (c.has("invoiceNumber") && invoiceType == InvoiceType.VENDOR_BILL) {
			invoice.setInvoiceNumber((String) c.get("invoiceNumber"));
		} else if (isNew) {
			APILogger.add(APILogType.ERROR, "Invoice number not provided.");
			throw new CommandException();
		}

		if (c.has("transactionNumber")) {
			invoice.setTransactionNumber((String) c.get("transactionNumber"));
		}

		if (c.has("invoiceDate")) {
			invoice.setInvoiceDate(Parser.convertObjectToTimestamp(c.get("invoiceDate")));
		}

		if (c.has("dueDate")) {
			invoice.setDueDate(Parser.convertObjectToTimestamp(c.get("dueDate")));
		}

		if (customerId != null) {
			invoice.setCustomerId(customerId);
		}

		if (vendorId != null) {
			invoice.setVendorId(vendorId);
		}

		if (c.has("taxRateType")) {
			taxRateType = (TaxRateType) Flags.convertInputToEnum(c.get("taxRateType"), "TaxRateType");
			invoice.setTaxRateType(taxRateType.getFlag());
		}

		if (c.has("taxRate")) {
			taxRate = Parser.convertObjectToDouble(c.get("taxRate"));
			invoice.setTaxRate(taxRate);
		}

		if (c.has("discountType")) {
			discountType = (InvoiceDiscountType) Flags.convertInputToEnum(c.get("discountType"), "InvoiceDiscountType");
			invoice.setDiscountType(discountType.getFlag());
		}

		if (c.has("discount")) {
			discount = Parser.convertObjectToDouble(c.get("discount"));
			invoice.setDiscount(discount);
		}

		if (isNew) {
			invoice.setStatus(InvoiceStatus.ACTIVE.getFlag());
			if (invoiceType == InvoiceType.CUSTOMER_INVOICE && isCustomerSystemUser) {
				invoice.setCustomerStatus(InvoiceStatus.PENDING.getFlag());
			} else {
				invoice.setCustomerStatus(InvoiceStatus.ACTIVE.getFlag());
			}
			invoice.setPaymentStatus(InvoiceStatus.UNPAID.getFlag());
			invoice.setDueStatus(InvoiceStatus.UNDUE.getFlag());
		}

		if (c.has("paymentTerm")) {
			invoice.setPaymentTerm(Flags.convertInputToFlag(c.get("paymentTerm"), "InvoicePaymentTerm"));
		}

		if (c.has("note")) {
			invoice.setNote((String) c.get("note"));
		}

		if (c.has("data")) {
			invoice.setData((String) c.get("data"));
		}

		invoice.setType(invoiceType.getFlag());

		if (c.has("recurringPeriod")) {
			invoice.setRecurringPeriod(recurPeriod.getFlag());
		}

		if (c.has("recurEndDate")) {
			invoice.setRecurEndDate(Parser.convertObjectToTimestamp(c.get("recurEndDate")));
		}

		if (c.has("recurStartDate")) {
			invoice.setRecurStartDate(Parser.convertObjectToTimestamp(c.get("recurStartDate")));
		}

		if (!recurPeriod.equals(TimePeriod.ONE_TIME) && invoice.getRecurStartDate().before(invoice.getInvoiceDate())) {
			APILogger.add(APILogType.ERROR, "Recur start date can not be before invoice date.");
			throw new ValidationException("Recur start date can not be before invoice date.");
		}

		if (isNew) {
			invoice.setOwnerId(ownerId);
			invoice.setUserId(ownerId);
		}

		HibernateUtils.save(invoice, c.getTransaction());

		List<Object> invoiceItems = new ArrayList<Object>();
		if (itemsData != null) {
			for (HashMap<String, Object> itemData : itemsData) {
				itemData.put("invoice", invoice);
				ICommand createItemCommand = new CreateInvoiceItem(itemData, c.getTransaction());
				CommandRegister.getInstance().process(createItemCommand);
				InvoiceItem item = (InvoiceItem) createItemCommand.getObject();
				if (item == null) {
					APILogger.add(APILogType.ERROR, "Error creating invoice item.");
					throw new ValidationException("Error creating invoice item.");
				}
				invoiceItems.add(item);
				subTotal += item.getSubTotal();
				taxTotal += item.getTaxTotal();
			}
		}

		if (isNew) {
			invoice.setCreatedAt(DateTime.getCurrentTimestamp());
		} else {
			invoice.setUpdatedAt(DateTime.getCurrentTimestamp());
		}

		total = subTotal + taxTotal;

		// Calculate discount total
		if (discount > 0) {
			if (discountType.equals(InvoiceDiscountType.PERCENTAGE)) {
				discountTotal = subTotal * (discount / 100);
			} else {
				discountTotal = discount;
			}
		}

		dueAmount = total - discountTotal;

		if (isNew) {
			invoice.setDiscountTotal(discountTotal);
			invoice.setSubTotal(subTotal);
			invoice.setTaxTotal(taxTotal);
			invoice.setTotalAmount(total);

			invoice.setDueAmount(dueAmount);
			invoice.setBalanceAmount(dueAmount);
		}

		// setting the initial data of recur
		if (invoice.getRecurringPeriod() != TimePeriod.ONE_TIME.getFlag()) {
			Timestamp nextRecur = DateTime.getNextRecurDate(invoice.getRecurringPeriod(), invoice.getInvoiceDate(),
					invoice.getRecurEndDate());
			if (nextRecur != null) {
				invoice.setNextRecurDate(nextRecur);
			}
		}

		invoice = (Invoice) HibernateUtils.save(invoice, c.getTransaction());
		return invoice;
	}

	public void handle(AcceptInvoice c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Invoice invoice = (Invoice) (new InvoiceQueryHandler())
					.getById(Parser.convertObjectToLong(c.get("invoiceId")));

			if (invoice == null) {
				APILogger.add(APILogType.ERROR, "Invoice Id (" + c.get("invoiceId") + ") not found.");
				throw new CommandException();
			}

			Long ownerId = Authentication.getUserId();
			Long requestUserId = ownerId;
			Long customerId = invoice.getCustomerId();
			Customer cust = (Customer) (new CustomerQueryHandler()).getById(customerId);
			boolean isCustomerSystemUser = cust.getUserId() != null;

			if (isCustomerSystemUser && cust.getUserId() != requestUserId) {
				// For customer who are already system user.
				APILogger.add(APILogType.ERROR, "Invoice can only be accepted by invoice customer.");
				throw new CommandException();
			} else if (invoice.getCustomerStatus() != InvoiceStatus.PENDING.getFlag()) {
				APILogger.add(APILogType.ERROR, "Invoice Id (" + c.get("invoiceId") + ") is already accepted.");
				throw new CommandException();
			}

			if (isCustomerSystemUser) {
				List<HashMap<String, Object>> itemsData = (List<HashMap<String, Object>>) c.get("invoiceItems");
				List<InvoiceItem> items = invoice.getItems();

				if (items.size() != itemsData.size()) {
					APILogger.add(APILogType.ERROR, "Items count incorrect.");
					throw new CommandException();
				}

				List<Long> tempIdList = new ArrayList<Long>();
				for (HashMap<String, Object> idt : itemsData) {
					Long itemId = Parser.convertObjectToLong(idt.get("itemId"));
					Long accountingClassId = Parser.convertObjectToLong(idt.get("accountingClassId"));
					if (tempIdList.contains(itemId)) {
						APILogger.add(APILogType.ERROR, "Invoice item (" + itemId.toString() + ") duplicate mapping.");
						throw new CommandException();
					}

					boolean matchedItem = false;
					for (InvoiceItem item : items) {
						if (item.getId().equals(itemId)) {
							matchedItem = true;
							break;
						}
					}
					if (matchedItem) {
						tempIdList.add(itemId);
					} else {
						APILogger.add(APILogType.ERROR, "Invalid item id (" + itemId.toString() + ").");
						throw new CommandException();
					}

				}

				// Update Invoice Items
				for (HashMap<String, Object> idt : itemsData) {
					Long itemId = Parser.convertObjectToLong(idt.get("itemId"));
					Long aclassId = Parser.convertObjectToLong(idt.get("accountingClassId"));

					for (InvoiceItem item : items) {
						HibernateUtils.save(item, trx);
					}
				}

			}

			invoice.setCustomerStatus(InvoiceStatus.ACTIVE.getFlag());
			HibernateUtils.save(invoice, trx);



			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}
			c.setObject(invoice);

		} catch (CommandException ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			APILogger.add(APILogType.ERROR, "Invoice Creation Failed", ex);
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			APILogger.add(APILogType.ERROR, "Invoice Creation Failed", ex);
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(CreateInvoiceItem c) {

		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {

			boolean isNew = false;
			boolean isTaxRatePercentage = false;
			Invoice invoice = (Invoice) c.get("invoice");

			if (TaxRateType.PERCENTAGE.getFlag().equals(invoice.getTaxRateType())) {
				isTaxRatePercentage = true;
			}

			InvoiceItem invoiceItem = null;

			if (c.has("id")) {
				invoiceItem = (InvoiceItem) HibernateUtils.get(InvoiceItem.class,
						Parser.convertObjectToLong(c.get("id")));
				isNew = false;
			} else {
				invoiceItem = new InvoiceItem();
				isNew = true;
			}

			// Set Invoice Id - Links with invoice
			invoiceItem.setInvoiceId(invoice.getId());

			if (c.has("title")) {
				invoiceItem.setTitle((String) c.get("title"));
			}

			if (c.has("description")) {
				invoiceItem.setDescription((String) c.get("description"));
			}

			if (c.has("quantity")) {
				invoiceItem.setQuantity(Parser.convertObjectToInteger(c.get("quantity")));
			} else if (isNew) {
				invoiceItem.setQuantity(1);
			}



			if (c.has("price")) {
				invoiceItem.setPrice(Parser.convertObjectToDouble(c.get("price")));
			}

			Byte taxable = Taxable.YES.getFlag();

			if (c.has("taxable")) {
				taxable = (Byte) Flags.convertInputToFlag(c.get("taxable"), "Taxable");
				invoiceItem.setTaxable(taxable);
			}

			Double subTotal = invoiceItem.getPrice() * invoiceItem.getQuantity();
			Double taxTotal = 0.0;
			if (taxable.equals(Taxable.YES.getFlag())) {
				if (isTaxRatePercentage) {
					taxTotal = subTotal * (invoice.getTaxRate() / 100.0);
				} else {
					taxTotal = invoice.getTaxRate();
				}
			}

			invoiceItem.setSubTotal(subTotal);
			invoiceItem.setTaxTotal(taxTotal);

			if (isNew) {
				invoiceItem.setCreatedAt(DateTime.getCurrentTimestamp());
			} else {
				invoiceItem.setUpdatedAt(DateTime.getCurrentTimestamp());
			}

			HibernateUtils.save(invoiceItem, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}
			c.setObject(invoiceItem);

		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			ex.printStackTrace();
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(CreateRecurringInvoice c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Invoice invoice = (Invoice) (c.get("invoice"));

			long timeDueDiff = invoice.getDueDate().getTime() - invoice.getInvoiceDate().getTime();

			Byte pType = invoice.getRecurringPeriod();
			Timestamp invoiceDate = invoice.getNextRecurDate();

			Invoice recurInvoice = this._cloneInvoice(invoice, true, trx);

			Timestamp invoiceDueDate = new Timestamp(invoiceDate.getTime() + timeDueDiff);

			recurInvoice.setInvoiceDate(invoiceDate);
			recurInvoice.setDueDate(invoiceDueDate);
			recurInvoice.setRecurParentId(invoice.getId());
			recurInvoice.setRecurringPeriod(null);
			recurInvoice.setRecurStartDate(null);
			recurInvoice.setRecurEndDate(null);
			recurInvoice.setStatus(InvoiceStatus.ACTIVE.getFlag());
			recurInvoice.setDueStatus(InvoiceStatus.UNDUE.getFlag());
			recurInvoice.setPaymentStatus(InvoiceStatus.UNPAID.getFlag());
			recurInvoice.setRefundStatus(null);
			recurInvoice.setWriteoffStatus(null);
			HibernateUtils.save(recurInvoice, trx);

			Timestamp nextInvoiceDate = DateTime.getNextTimestamp(invoiceDate, pType);

			if (nextInvoiceDate != null && nextInvoiceDate.after(invoice.getRecurStartDate())
					&& (nextInvoiceDate.before(invoice.getRecurEndDate())
							|| nextInvoiceDate.equals(invoice.getRecurEndDate()))) {
				invoice.setNextRecurDate(nextInvoiceDate);
			} else {
				invoice.setNextRecurDate(null);
			}

			HibernateUtils.save(invoice, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}

			c.setObject(invoice);

		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(UpdateInvoiceDueStatus c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Invoice invoice = (Invoice) (c.get("invoice"));

			if (!invoice.getPaymentStatus().equals(InvoiceStatus.PAID.getFlag())) {
				if (DateTime.compare(DateTime.getCurrentTimestamp(), invoice.getDueDate()) == 0) {
					invoice.setDueStatus(Flags.InvoiceStatus.DUE.getFlag());
					invoice.setUpdatedAt(DateTime.getCurrentTimestamp());
				} else if (DateTime.compare(DateTime.getCurrentTimestamp(), invoice.getDueDate()) == 1) {
					invoice.setDueStatus(Flags.InvoiceStatus.OVERDUE.getFlag());
					invoice.setUpdatedAt(DateTime.getCurrentTimestamp());
				}
			}
			HibernateUtils.save(invoice, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}

			c.setObject(invoice);

		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	private Invoice _cloneInvoice(Invoice invoice, Boolean isReceiverCopy, Transaction aTrx) {
		boolean success = false;
		boolean isCommitable = false;
		if (aTrx == null) {
			isCommitable = true;
		}

		// HibernateUtils.openSession();
		Transaction trx = aTrx;
		if (isCommitable) {
			trx = HibernateUtils.beginTransaction();
		}

		Invoice newInvoice = null;
		try {
			newInvoice = new Invoice();
			List<InvoiceItem> invoiceItems = invoice.getItems();

			newInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
			newInvoice.setSaleId(invoice.getSaleId());
			newInvoice.setTransactionNumber(invoice.getTransactionNumber());

			newInvoice.setVendorId(invoice.getVendorId());
			newInvoice.setCustomerId(invoice.getCustomerId());

			newInvoice.setPaymentTerm(invoice.getPaymentTerm());

			newInvoice.setInvoiceDate(invoice.getInvoiceDate());
			newInvoice.setDueDate(invoice.getDueDate());

			newInvoice.setDiscountTotal(invoice.getDiscountTotal());
			newInvoice.setSubTotal(invoice.getSubTotal());
			newInvoice.setTaxTotal(invoice.getTaxTotal());
			newInvoice.setTotalAmount(invoice.getTotalAmount());
			newInvoice.setDueAmount(invoice.getDueAmount());
			newInvoice.setBalanceAmount(invoice.getBalanceAmount());

			newInvoice.setStatus(invoice.getStatus());
			newInvoice.setCustomerStatus(invoice.getCustomerStatus());
			newInvoice.setPaymentStatus(invoice.getPaymentStatus());
			newInvoice.setDueStatus(invoice.getDueStatus());
			newInvoice.setRefundStatus(invoice.getRefundStatus());
			newInvoice.setWriteoffStatus(invoice.getWriteoffStatus());

			newInvoice.setTaxRateType(invoice.getTaxRateType());
			newInvoice.setTaxRate(invoice.getTaxRate());
			newInvoice.setDiscountType(invoice.getDiscountType());
			newInvoice.setDiscount(invoice.getDiscount());
			newInvoice.setNote(invoice.getNote());
			newInvoice.setData(invoice.getData());

			newInvoice.setType(invoice.getType());

			newInvoice.setUserId(invoice.getUserId());
			newInvoice.setCustomerId(invoice.getCustomerId());
			newInvoice.setOwnerId(invoice.getOwnerId());
			newInvoice.setCreatedAt(invoice.getCreatedAt());
			newInvoice.setUpdatedAt(invoice.getUpdatedAt());
			newInvoice.setDeletedAt(invoice.getDeletedAt());

			HibernateUtils.save(newInvoice, trx);

			for (InvoiceItem invoiceItem : invoiceItems) {
				InvoiceItem newInvoiceItem = new InvoiceItem();
				newInvoiceItem.setInvoiceId(newInvoice.getId());
				newInvoiceItem.setTitle(invoiceItem.getTitle());
				newInvoiceItem.setDescription(invoiceItem.getDescription());
				newInvoiceItem.setAccountingCodeId(invoiceItem.getAccountingCodeId());
				newInvoiceItem.setCustomerAccountingCodeId(invoiceItem.getCustomerAccountingCodeId());
				newInvoiceItem.setQuantity(invoiceItem.getQuantity());
				newInvoiceItem.setPrice(invoiceItem.getPrice());
				newInvoiceItem.setSubTotal(invoiceItem.getSubTotal());
				newInvoiceItem.setTaxTotal(invoiceItem.getTaxTotal());
				newInvoiceItem.setTaxable(invoiceItem.getTaxable());

				newInvoiceItem.setCreatedAt(invoiceItem.getCreatedAt());
				newInvoiceItem.setUpdatedAt(invoiceItem.getUpdatedAt());
				newInvoiceItem.setDeletedAt(invoiceItem.getDeletedAt());

				HibernateUtils.save(newInvoiceItem, trx);
			}

			if (isCommitable) {
				HibernateUtils.commitTransaction(trx);
			}

		} catch (Exception ex) {

			if (isCommitable) {
				HibernateUtils.rollbackTransaction(trx);
			}
			success = false;

		} finally {
			if (isCommitable) {
				HibernateUtils.closeSession();
			}
		}

		return newInvoice;
	}

	public void handle(CreateSalesInvoice c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Sales sales = (Sales) c.get("sales");
			Product p = sales.getProduct();
			System.out.println(p.getTitle());
			Double subTotal = 0.0;
			Double taxTotal = 0.0;
			Double total = 0.0;
			Double balance = 0.0;
			Double taxRate = 0.0;
			Double discount = 0.0;
			String invoiceNumber = this._generateInvoiceNumber();
			boolean isTaxRatePercentage = false;

			Invoice inv = new Invoice();

			inv.setInvoiceNumber(invoiceNumber);
			inv.setSaleId(sales.getId());

			inv.setInvoiceDate(sales.getStartDate());
			inv.setDueDate(sales.getStartDate());
			inv.setCustomerId(sales.getCustomerId());
			inv.setTaxRateType(TaxRateType.AMOUNT.getFlag());
			inv.setTaxRate(taxRate);

			inv.setStatus(InvoiceStatus.ACTIVE.getFlag());

			inv.setPaymentStatus(InvoiceStatus.UNPAID.getFlag());
			inv.setDueStatus(InvoiceStatus.UNDUE.getFlag());
			inv.setCustomerStatus(InvoiceStatus.PENDING.getFlag());
			inv.setPaymentTerm(InvoicePaymentTerm.RECEIPT.getFlag());

			inv.setType(InvoiceType.CUSTOMER_INVOICE.getFlag());

			inv.setOwnerId(sales.getOwnerId());
			inv.setUserId(sales.getOwnerId());
			HibernateUtils.save(inv, trx);



			total = subTotal + taxTotal;
			balance = total - discount;

			inv.setDiscount(discount);
			inv.setDiscountType(InvoiceDiscountType.AMOUNT.getFlag());
			inv.setDiscountTotal(discount);
			inv.setBalanceAmount(balance);

			inv.setSubTotal(subTotal);
			inv.setTaxTotal(taxTotal);
			inv.setDueAmount(balance);
			inv.setTotalAmount(total);

			inv.setCreatedAt(sales.getCreatedAt());

			HibernateUtils.save(inv, trx);

			sales.setIsInvoiced(SalesIsInvoiced.YES.getFlag());
			HibernateUtils.save(sales, trx);

			// Calculate next sales date.
			Timestamp ts = DateTime.getCurrentTimestamp();
			if (sales.getStatus().equals(EntityStatus.ACTIVE.getFlag())
					&& sales.getIsInvoiced().equals(SalesIsInvoiced.YES.getFlag())
					&& sales.getNextInvoiceAt().before(ts)) {
				System.out.println("CALCULATING NEXT SALES DATE");

				Timestamp start = sales.getStartDate();
				Timestamp end = sales.getEndDate();
				Timestamp current = sales.getNextInvoiceAt();

				Timestamp next = null;



				// If next sales date is in range the update date otherwise mark
				// sales as inactive.
				if (next != null && next.after(start) && (next.before(end) || next.equals(end))) {
					sales.setNextInvoiceAt(next);
					sales.setIsInvoiced(SalesIsInvoiced.NO.getFlag());
				} else {
					sales.setNextInvoiceAt(null);
					sales.setIsInvoiced(SalesIsInvoiced.YES.getFlag());
					sales.setStatus(EntityStatus.INACTIVE.getFlag());
				}

			}

			HibernateUtils.save(sales, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}

			// c.setObject();
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			APILogger.add(APILogType.ERROR, "Invoice Creation Failed", ex);
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	private String _generateInvoiceNumber() {
		Long timestamp = System.currentTimeMillis() / 1000L;
		return "10" + timestamp.toString();
	}

}

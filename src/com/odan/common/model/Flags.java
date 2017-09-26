package com.odan.common.model;

public abstract class Flags {

	public interface EntityFlags {
		public Byte getFlag();

		public String getString();
	}

	public enum EntityStatus implements EntityFlags {
		INACTIVE("InActive", (byte) 0), ACTIVE("Active", (byte) 1), DISABLED("Disabled", (byte) 10);

		private final String string;
		private final byte flag;

		private EntityStatus(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum EntityType implements EntityFlags {
		USER("User", (byte) 1), CUSTOMER("Customer", (byte) 2), VENDOR("Vendor", (byte) 3), INVOICE("Invoice",
				(byte) 11), INVOICE_ITEM("Invoice Item", (byte) 12), DISCOUNT("Discount", (byte) 15), SALES("Sales",
						(byte) 31), PRODUCT("Product", (byte) 32), CATEGORY("Category", (byte) 33), FEATURE("Feature",
								(byte) 34), ACCOUNTING_CLASS("Accounting Class", (byte) 41), TRANSACTION("Transaction",
										(byte) 51), COMMISSION("Commission",
												(byte) 52), DEDUCTION("Deduction", (byte) 53);

		private final String string;
		private final byte flag;

		private EntityType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum SalesIsInvoiced implements EntityFlags {
		NO("No", (byte) 0), YES("Yes", (byte) 1);

		private final String string;
		private final byte flag;

		private SalesIsInvoiced(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum ReceiptType implements EntityFlags {
		SENT("Sent", (byte) 1), RECEIVED("Received", (byte) 2);

		private final String string;
		private final byte flag;

		private ReceiptType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum BillingAccountType implements EntityFlags {
		BANK("Bank", (byte) 1), PAYPAL("Paypal", (byte) 2), SKRILL("Skrill", (byte) 3);

		private final String string;
		private final byte flag;

		private BillingAccountType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum BillingAccountCategory implements EntityFlags {
		PRIMARY("Primary", (byte) 1), SECONDARY("Secondary", (byte) 2);

		private final String string;
		private final byte flag;

		private BillingAccountCategory(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	// public enum RatePlanChargeBillingType implements EntityFlags {
	// ONE_TIME("One Time", (byte) 1), RECURRING("Recurring", (byte) 2);
	//
	// private final String string;
	// private final byte flag;
	//
	// private RatePlanChargeBillingType(String string, byte flag) {
	// this.string = string;
	// this.flag = flag;
	// }
	//
	// public Byte getFlag() {
	// return this.flag;
	// }
	//
	// public String getString() {
	// return this.string;
	// }
	// }

	public enum RatePlanChargeDiscountType implements EntityFlags {
		AMOUNT("Amount", (byte) 1), PERCENTAGE("Percentage", (byte) 2);

		private final String string;
		private final byte flag;

		private RatePlanChargeDiscountType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum InvoiceStatus implements EntityFlags {
		INACTIVE("Inactive", (byte) 0), ACTIVE("Active", (byte) 1), PENDING("Pending", (byte) 2), UNPAID("Unpaid",
				(byte) 11), PARTIAL_PAID("Partial Paid", (byte) 12), PAID("Paid", (byte) 13), UNDUE("Undue",
						(byte) 21), DUE("Due", (byte) 22), OVERDUE("Overdue", (byte) 23), REFUND_REQUESTED(
								"Refund Requested",
								(byte) 31), REFUND_APPROVED("Refund Approved", (byte) 32), REFUND_REJECTED(
										"Refund Rejected", (byte) 33), WRITTEN_OFF("Written Off", (byte) 41);

		private final String string;
		private final byte flag;

		private InvoiceStatus(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	// public enum InvoiceCopy implements EntityFlags {
	// OWNER("Owner", (byte) 1), RECEIVER("Receiver", (byte) 2);
	//
	// private final String string;
	// private final byte flag;
	//
	// private InvoiceCopy(String string, byte flag) {
	// this.string = string;
	// this.flag = flag;
	// }
	//
	// public Byte getFlag() {
	// return this.flag;
	// }
	//
	// public String getString() {
	// return this.string;
	// }
	// }

	public enum InvoicePaymentTerm implements EntityFlags {
		RECEIPT("Receipt", (byte) 1), DUEDATE("Due Date", (byte) 2);

		private final String string;
		private final byte flag;

		private InvoicePaymentTerm(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum InvoiceType implements EntityFlags {
		CUSTOMER_INVOICE("Customer Invoice", (byte) 1), VENDOR_BILL("Vendor BIll", (byte) 2);

		private final String string;
		private final byte flag;

		private InvoiceType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum InvoiceRefundType implements EntityFlags {
		PARTIAL("Partial", (byte) 1), FULL("Full", (byte) 2);

		private final String string;
		private final byte flag;

		private InvoiceRefundType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum TaxRateType implements EntityFlags {
		AMOUNT("Amount", (byte) 1), PERCENTAGE("Percentage", (byte) 2);

		private final String string;
		private final byte flag;

		private TaxRateType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum InvoiceDiscountType implements EntityFlags {
		AMOUNT("Amount", (byte) 1), PERCENTAGE("Percentage", (byte) 2);

		private final String string;
		private final byte flag;

		private InvoiceDiscountType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum TimePeriod implements EntityFlags {
		ONE_TIME("One Time", (byte) 0), DAILY("Daily", (byte) 1), WEEKLY("Weekly", (byte) 7), MONTHLY("Monthly",
				(byte) 30), QUARTERLY("Quarterly",
						(byte) 25), HALF_YEARLY("Half Yearly", (byte) 50), YEARLY("Yearly", (byte) 100);

		private final String string;
		private final byte flag;

		private TimePeriod(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum Taxable implements EntityFlags {
		NO("No", (byte) 0), YES("Yes", (byte) 1);

		private final String string;
		private final byte flag;

		private Taxable(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum AccountingCodeSegment implements EntityFlags {
		TRANSACTION("Transaction", (byte) 1), INVOICE("Invoice", (byte) 2), BOTH("Both", (byte) 3);

		private final String string;
		private final byte flag;

		private AccountingCodeSegment(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum AccountingClassCategory implements EntityFlags {
		INCOME("Income", (byte) 1), EXPENSE("Expense", (byte) 2);

		private final String string;
		private final byte flag;

		private AccountingClassCategory(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum AccountingPeriodStatus implements EntityFlags {
		CLOSED("Closed", (byte) 1), OPEN("Open", (byte) 2), REOPENED("Reopened", (byte) 3);

		private final String string;
		private final byte flag;

		private AccountingPeriodStatus(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum AccountingHead implements EntityFlags {
		ASSET("Assets", (short) 1000), ACCOUNT_RECEIVABLE("Account Receivable", (short) 1010), CASH("Cash",
				(short) 1020), LIABILITY("Liabilities", (short) 3000), ACCOUNT_PAYABLE("Account Payable",
						(short) 3010), TAX_PAYABLE("Tax Payable", (short) 3020), DEFERRED_REVENUE("Deferred Revenue",
								(short) 3030), CASH_ACCOUNT("Customer Cash on Account",
										(short) 3040), REVENUE("Revenue", (short) 5000), EXPENSE("Expense",
												(short) 7000), CAPITAL("Capital", (short) 9000);

		private final String string;
		private final short flag;

		private AccountingHead(String string, short flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			System.out.println("===>Unimplemented Function<====");
			return null;
		}

		public Short getCode() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}

		public Short getId() {
			return this.flag;
		}
	}

	public enum JournalEntryType implements EntityFlags {
		DEBIT("Debit", (byte) 1), CREDIT("Credit", (byte) 2);

		private final String string;
		private final byte flag;

		private JournalEntryType(String string, byte flag) {
			this.string = string;
			this.flag = flag;
		}

		public Byte getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public enum RmsTransactionState {
		NEW("New", "NEW"), PRECLOSED("Pre Close", "PRE_CLOSED"), CLOSED("Closed", "CLOSED"), UNCLOSED("Unclosed",
				"UNCLOSED");

		private final String string;
		private final String flag;

		private RmsTransactionState(String string, String flag) {
			this.string = string;
			this.flag = flag;
		}

		public String getFlag() {
			return this.flag;
		}

		public String getString() {
			return this.string;
		}
	}

	public static EntityFlags convertInputToEnum(Object input, String en) {
		String enumString = (String) input;
		EntityFlags ef = null;
		try {
			if (enumString != null) {
				enumString = enumString.toUpperCase();
				String enumClassName = "com.odan.common.model.Flags$" + en;
				Class enumClass = Class.forName(enumClassName);
				Object enumValue = Enum.valueOf(enumClass, enumString);
				ef = ((EntityFlags) enumValue);
			}
		} catch (Exception ex) {
			// Do Nothing
			ex.printStackTrace();
		}
		return ef;
	}

	public static Byte convertInputToFlag(Object input, String en) {
		Byte flag = null;
		try {
			EntityFlags ef = Flags.convertInputToEnum(input, en);
			flag = ef.getFlag();
		} catch (Exception ex) {
			// Do Nothing
			ex.printStackTrace();
		}
		return flag;
	}

	public static Byte convertInputToStatus(Object input) {
		String str = (String) input;
		Byte status = (byte) 1;
		if (str != null) {
			status = EntityStatus.valueOf(str.toUpperCase()).getFlag();
		}
		return status;
	}
	public enum SpecificDateType {
		TODAY, TOMORROW, YESTERDAY, LAST_MONTH, LAST_WEEK, NEXT_WEEK, NEXT_MONTH, THIS_WEEK, THIS_MONTH;
	}
}

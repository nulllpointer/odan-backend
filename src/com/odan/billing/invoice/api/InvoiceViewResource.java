package com.odan.billing.invoice.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.odan.common.application.CommandException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.invoice.InvoiceQueryHandler;
import com.odan.billing.invoice.model.Invoice;
import com.odan.common.api.BaseAction;
import com.odan.common.utils.Parser;

import br.eti.mertz.wkhtmltopdf.wrapper.Pdf;
import br.eti.mertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;

@ParentPackage("default")
@Namespace(value = "/v1")
public class InvoiceViewResource extends BaseAction {

	private String type = "application/pdf";
	private String filename;
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getType() {
		return this.type;
	}

	public String getFilename() {
		return this.filename;
	}

	@Action(value = "invoice/view/html", results = { @Result(name = "success", location = "/invoice/view.jsp") })
	public String viewInvoice() throws CommandException {
		HttpServletRequest request = ServletActionContext.getRequest();

		Long id = Parser.convertObjectToLong(this.getId());
		Invoice invoice = (Invoice) (new InvoiceQueryHandler()).getById(id);
		System.out.println("Invoice -------- " + invoice);
		request.setAttribute("invoice", invoice);
		return SUCCESS;
	}

	@Action(value = "invoice/view/pdf", results = {
			@Result(name = "success", type = "stream", params = { "contentType", "${type}", "inputName", "inputStream",
					"bufferSize", "1024", "contentDisposition", "attachment; filename=\"${filename}\"" }) })
	public String generateInvoicePdf() {

		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String serverBasePath = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
					request.getContextPath()).toString();

			String id = request.getAttribute("id").toString();
			System.out.println("TEST");
			System.out.println("Invoice id ------ " + id);
			System.out.println("Context Path --- " + request.getContextPath().toString());
			System.out.println("Request URL ---- " + serverBasePath);

			filename = "invoice-" + id + ".pdf";
			String filePath = request.getServletContext().getRealPath("pdf").toString();
			new File(filePath).mkdirs();

			System.out.println("File path ---- " + filePath + "\\" + filename);
			
			WrapperConfig wc = new WrapperConfig("/usr/local/bin/wkhtmltopdf");
			Pdf pdf = new Pdf(wc);
			pdf.addPage(serverBasePath + "/v1/invoice/view/html?id=" + id, PageType.url);

			// Save the PDF
			pdf.saveAs(filePath + "\\" + filename);

			File invoicePdf = new File(filePath + "\\" + filename);
			inputStream = new FileInputStream(invoicePdf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
}

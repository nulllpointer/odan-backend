<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Invoice</title>
    <link rel="stylesheet" href="<s:url value='/invoice/style.css'/>" media="all" />
  </head>
  <body>
    <header class="clearfix">
      <div id="logo">
        <img src="<s:url value='/invoice/logo.png'/>">
      </div>
      <h1>INVOICE ${invoice.invoiceNumber}</h1>
      <div id="company" class="clearfix">
        <div>Company Name${invoice.invoiceNumber}</div>
        <div>455 Foggy Heights,<br /> AZ 85004, US</div>
        <div>(602) 519-0450</div>
        <div><a href="mailto:company@example.com">company@example.com</a></div>
      </div>
      <div id="project">
        <div><span>PROJECT</span> Website development</div>
        <div><span>CLIENT</span> John Doe</div>
        <div><span>ADDRESS</span> 796 Silver Harbour, TX 79273, US</div>
        <div><span>EMAIL</span> <a href="mailto:john@example.com">john@example.com</a></div>
        <div><span>DATE</span> August 17, 2015</div>
        <div><span>DUE DATE</span> September 17, 2015</div>
      </div>
    </header>
    <main>
      <table>
        <thead>
          <tr>
            <th class="service">SERVICE</th>
            <th class="desc">DESCRIPTION</th>
            <th>PRICE</th>
            <th>QTY</th>
            <th>TOTAL</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="item" items="${invoice.items}">
			  <tr>
	            <td class="service">${item.accountingCode}</td>
	            <td class="desc">${item.description}</td>
	            <td class="unit">$${item.price}</td>
	            <td class="qty">${item.quantity}</td>
	            <td class="total">$${item.subTotal}</td>
	          </tr>
		  </c:forEach>
		  <!--<tr>
            <td class="service">Design</td>
            <td class="desc">Creating a recognizable design solution based on the company's existing visual identity</td>
            <td class="unit">$40.00</td>
            <td class="qty">26</td>
            <td class="total">$1,040.00</td>
          </tr>
          <tr>
            <td class="service">Development</td>
            <td class="desc">Developing a Content Management System-based Website</td>
            <td class="unit">$40.00</td>
            <td class="qty">80</td>
            <td class="total">$3,200.00</td>
          </tr>
          <tr>
            <td class="service">SEO</td>
            <td class="desc">Optimize the site for search engines (SEO)</td>
            <td class="unit">$40.00</td>
            <td class="qty">20</td>
            <td class="total">$800.00</td>
          </tr>
          <tr>
            <td class="service">Training</td>
            <td class="desc">Initial training sessions for staff responsible for uploading web content</td>
            <td class="unit">$40.00</td>
            <td class="qty">4</td>
            <td class="total">$160.00</td>
          </tr> -->
          <tr>
            <td colspan="4">TAX <c:if test="${invoice.taxRateType==2}">${invoice.taxRate}%</c:if></td>
            <td class="total">$${invoice.tax}</td>
          </tr>
          <tr>
            <td colspan="4">SUBTOTAL</td>
            <td class="total">$${invoice.amount}</td>
          </tr>
          <tr>
            <td colspan="4">DISCOUNT</td>
            <td class="total">$${invoice.discount}</td>
          </tr>
          <tr>
            <td colspan="4" class="grand total">GRAND TOTAL</td>
            <td class="grand total">$${invoice.dueAmount}</td>
          </tr>
        </tbody>
      </table>
      <div id="notices">
        <div>NOTICE:</div>
        <div class="notice">A finance charge of 1.5% will be made on unpaid balances after 30 days.</div>
      </div>
    </main>
    <footer>
      Invoice was created on a computer and is valid without the signature and seal.
    </footer>
  </body>
</html>
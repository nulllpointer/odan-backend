//var BaseURL = "http://localhost:8080/faapi/";
var currentURL = window.location.href;
var BaseURL = currentURL.substr(0, currentURL.toLowerCase().indexOf("/faapi/") + 7);

$(document).ready(function() {
	$(".api-form").off("click").on("click", function() {
		var path = $(this).data("ui");
		var api = $(this).data("api");
		var method = $(this).data("method");
		createForm(path, api, method)
	});

	$(".api-table").off("click").on("click", function() {
		var api = $(this).data("api");
		var method = $(this).data("method");
		var root = $(this).data("root");
		var filter = $(this).data("filter");
		var filterData = getFilters(filter, method.toLowerCase() == "get");
		createTable(api, method, filterData, root)
	});
	convertFieldsToDropDown();
	$(".nav-section:not(.always-visible)").hide();
});

function createForm(path, api, method) {
	var id = path.replace(/\//g, "-").replace(/\./g, "-");
	$.ajax({
		url : BaseURL + "ui/json/" + path + ".json",
		success : function(response) {
			var schema = response;
			console.log(schema);

			$("#container").html(
					'<h3>' + schema.title + '</h3><form id="form-' + id
							+ '"></form>' + '<h4>Request</h4>'
							+ '<div id="request-' + id
							+ '" class="alert alert-info"></div>'
							+ '<h4>Response</h4>' + '<div id="response-' + id
							+ '" class="alert alert-success"></div>');
			initJSONForm(id, schema, api, method);
		},
		error : function(err) {
			console.log(err);
		}
	});
}

function initJSONForm(id, schema, api, method) {
	$('#form-' + id).jsonForm(
			{
				schema : schema,
				validate : false,
				onSubmit : function(errors, values) {
					console.log(errors);
					if (errors) {
						console.error("ERROR IN FORM VALIDATION");
					} else {
						
						var formId = $("#container").find("form").attr("id")
						switch(formId) {
						case "form-invoice-send":
							values.mode = "internal";
							values.type = "sent";
							break;

						case "form-invoice-receive_external":
							values.mode = "external";
							values.type = "received";
							break;
						}

						var formData = JSON.stringify(values);
						sendAPIRequest(api, method, formData, function(json) {
							$("#request-" + id).JSONView(values);
							$("#response-" + id).JSONView(json);
							if (json.result == "error") {
								$("#response-" + id).addClass("alert-error")
										.removeClass("alert-success")
										.removeClass("alert-warning");
							} else if (json.result == "success") {
								$("#response-" + id).addClass("alert-error")
										.removeClass("alert-success")
										.removeClass("alert-warning");
							} else {
								$("#response-" + id).removeClass(
										"alert-success").removeClass(
										"alert-error");
							}
						});
					}
				}
			});

	convertFieldsToDropDown();
	hideFields();
	invoicePageEvents();
	salesPageEvents();
	$("._jsonform-array-addmore").on("click", function() {
		convertFieldsToDropDown();
		hideFields();
	});
}

function convertFieldsToDropDown() {
	$("form[id^='form-'] input[id*='Id'], input[id='activeapiuserid']").each(
			function() {
				var id = $(this).attr("id");
				var idParts = id.split("-");
				var elemName = idParts[idParts.length - 1];
				elemName = elemName.toLowerCase();

				if (elemName.indexOf(".") !== -1) {
					elemName = elemName.split(".");
					elemName = elemName[elemName.length - 1];
				}

				if (elemName.indexOf("[") !== -1) {
					elemName = elemName.substr(0, elemName.indexOf("["));
				}

				var sd = {};
				sd.url = "";
				sd.root = "";
				sd.key = "";
				sd.value = "";
				
				var filterData = "";

				switch (elemName) {
				case "head":
				case "parentid":
					sd.elem = id;
					sd.url = "accounting/heads";
					sd.root = "heads";
					sd.key = "id";
					sd.value = "title";
					break;

				case "accountingclassid":
					sd.elem = id;
					sd.url = "accounting/class";
					sd.root = "accountingClass";
					sd.key = "id";
					sd.value = "title";
					filterData = getFilters("OwnerActive", true);
					break;
				case "activeapiuserid":
				case "customerid":
				case "userid":
				case "payeeid":
				case "payerid":
					sd.elem = id;
					sd.url = "sync/user";
					sd.root = "users";
					sd.key = "entityId";
					sd.value = "entityTitle";
					filterData = getFilters("Active", true);
					break;
				case "featureid":
					sd.elem = id;
					sd.url = "sales/feature";
					sd.root = "features";
					sd.key = "id";
					sd.value = "title";
					filterData = getFilters("OwnerActive", true);
					break;
				case "rateplanid":
					sd.elem = id;
					sd.url = "sales/rateplan";
					sd.root = "ratePlans";
					sd.key = "id";
					sd.value = "title";
					filterData = getFilters("OwnerActive", true);
					break;
				case "categoryid":
					sd.elem = id;
					sd.url = "sales/category";
					sd.root = "categories";
					sd.key = "id";
					sd.value = "title";
					filterData = getFilters("OwnerActive", true);
					break;
				case "productid":
					sd.elem = id;
					sd.url = "sales/product";
					sd.root = "products";
					sd.key = "id";
					sd.value = "title";
					filterData = getFilters("OwnerActive", true);
					break;
				case "deductionid":
					sd.elem = id;
					sd.url = "sync/deduction";
					sd.root = "deductions";
					sd.key = "entityId";
					sd.value = "entityTitle";
					filterData = getFilters("OwnerActive", true);
					break;
					
				case "invoiceid":
					sd.elem = id;
					sd.url = "invoice";
					sd.root = "invoices";
					sd.key = "id";
					sd.value = "invoiceNumber";
					sd.url = "invoice";
					
					var form = $(this).closest("form");
					if(form.attr("id") == "form-invoice-writeoff") {
						filterData = getFilters("MySentInvoiceList", true);
					}
					else if(form.attr("id") == "form-invoice-refund_request") {
						filterData = getFilters("MyReceivedInvoiceList", true);
					}
					else if(form.attr("id") == "form-invoice-receive_internal") {
						filterData = getFilters("MyPendingInvoiceList", true);
					}
					else {
						filterData = getFilters("MyInvoiceList", true);
					}
					break;
				case "entityid":
					sd.elem = id;
					sd.url = "invoice/payable";
					sd.root = "invoices";
					sd.key = "id";
					sd.value = "invoiceNumber";
					filterData = getFilters("Payable", true);
					break;
				case "billingaccountid":
					sd.elem = id;
					sd.url = "payment/account";
					sd.root = "accounts";
					sd.key = "id";
					sd.value = "accountName";
					filterData = getFilters("OwnerActive", true);
					break;
					
				}

				function renderDropdown(params) {
					$.ajax({
						method : "GET",
						url : BaseURL + "v1/" +params.url,
						data : params.data,
						contentType : "application/json",
						beforeSend : function(request) {
							request.setRequestHeader("REW3-UserId", $("#activeapiuserid").val());
						},
						success : function(response) {
							var s = $("<select></select>");
							var elem = $("[id^='" + params.elem + "']");
							var id = elem.attr("id");
							s.attr("id", elem.attr("id"));
							s.attr("name", elem.attr("name"));

							var list = response[params.root];
							if (list !== null && list != undefined) {
								var o = $("<option value=''></option>");
								s.append(o);
								
								for (var i = 0; i < list.length; i++) {
									var o = $("<option></option>");
									o.html(list[i][sd.value]);
									o.attr("value", list[i][sd.key]);
									s.append(o);
								}
								elem.replaceWith(s);
							}
							
							if(id == "activeapiuserid") {
								$("#activeapiuserid").off("change").on("change", function(){
									$(this).attr("readonly", "true");
									$(".nav-section").show();
								});
							}
						}
					})
				}

				if (sd.url != "") {
					sd.data = filterData;
					console.log(sd);
					renderDropdown(sd);
				}
			});
}

function getFilters(filterName, get) {
	var filters = {};
	var activeUserId = $("#activeapiuserid").val();
	if(filterName == undefined) {
		filterName = "";
	}
	if(filterName == "MySentInvoiceList") {
		filters.userId = activeUserId;
		filters.userStatus = "active";
	}
	
	if(filterName == "MyReceivedInvoiceList") {
		filters.customerId = activeUserId;
		filters.customerStatus = "active";
	}
	
	if(filterName == "MyPendingInvoiceList") {
		filters.customerId = activeUserId;
		filters.customerStatus = "pending";
	}
	
	if(filterName.indexOf("Active") !== -1) {
		filters.status = "active";
	}
	if(filterName.indexOf("Owner") !== -1) {
		filters.ownerId = activeUserId;
	}
	if(filterName.indexOf("User") !== -1) {
		filters.userId = activeUserId;
	}
	if(filterName.indexOf("Customer") !== -1) {
		filters.customerId = activeUserId;
	}
	if(filterName.indexOf("Payable") !== -1) {
		filters.customerId = activeUserId;
		filters.paymentStatus = "unpaid,partial_paid";
	}
	if(filterName.indexOf("Receivable") !== -1) {
		filters.userId = activeUserId;
		filters.paymentStatus = "unpaid,partial_paid";
	}
	
	if(get != undefined && get == true) {
		filters = $.param(filters);
	}
	else {
		filters = JSON.stringify(filters);
	}
	
	return filters;
}

function invoicePageEvents() {
	$("#jsonform-0-elt-isGlobal").off("click").on("click", onGlobalClick);
	function onGlobalClick(e) {
		var isGlobal = $("#jsonform-0-elt-isGlobal").is(":checked");
		if (isGlobal) {
			$("#jsonform-0-elt-customerId").find("._jsonform-array-buttons")
					.show();
		} else {
			$("#jsonform-0-elt-customerId").find("._jsonform-array-buttons")
					.hide();
		}
	}
	onGlobalClick();
}

function salesPageEvents() {
	$("#form-sales-rateplan-save [name='billingType']").off("change").on(
			"change", onRatePlanTypeChange);
	function onRatePlanTypeChange(e) {
		var select = $("#form-sales-rateplan-save [name='billingType']");
		if (select.length == 0) {
			return;
		}
		var ratePlanType = select.val();
		if (ratePlanType == 'onetime') {
			$("[name='billingPeriod']").closest(".control-group").hide();
		} else {
			$("[name='billingPeriod']").closest(".control-group").show();
		}
	}
	onRatePlanTypeChange(null);

}

function hideFields() {
	$(".control-group").each(function() {
		if ($(this).find(".control-label").length == 0) {
			$(this).hide();
		}
	})
}

function renderTable(data, id) {
	if (id == undefined) {
		id = "";
		window.datatableid = Math.round(Math.random() * 1000);
	}
	var tableId = "datatable-main-" + window.datatableid;
	if (id != "") {
		tableId = "datatable-" + id;
	}

	var haveChild = false;
	if (hasChildTable(data)) {
		haveChild = true;
	}

	var html = "";
	var header = "";
	var body = "";
	for ( var key in data[0]) {
		if (typeof data[0][key] != "object" || data[0][key] == null) {
			header += "<th>" + key + "</th>";
		}
	}

	if (haveChild) {
		header = "<th></td>" + header;
	}

	header = "<thead><tr>" + header + "</tr></thead>";
	for (var i = 0; i < data.length; i++) {
		body += "<tr>";
		if (haveChild) {
			body += "<td class='center'><img class='table-expand-collapse' src='http://i.imgur.com/SD7Dz.png' data-index='"
					+ i + "'></td>";
		}
		for ( var key in data[0]) {
			if (typeof data[0][key] != "object" || data[0][key] == null) {
				body += "<td>" + data[i][key] + "</td>";
			}
		}
		body += "</tr>";
	}
	body = "<tbody>" + body + "</tbody>";
	html = "<table id='" + tableId + "'>" + header + body + "</table>";

	return html;
}

function renderChildTable(data, id) {
	var html = "";
	for ( var key in data) {
		if (typeof data[key] == "object" && data[key] != null) {
			html += "<div class='child-table'>";
			html += "<h3>" + key + "</h3>";
			html += renderTable(data[key], id + "-" + key);
			html += '<div class="clearfix"></div>';
			html += "</div>";
		}
	}
	html = "<div class='child-tables'>" + html + "</div>";
	return html;
}

function hasChildTable(data) {
	var containsChild = false;
	for ( var key in data[0]) {
		if (typeof data[0][key] == "object" && typeof data[0][key] != null) {
			containsChild = true;
		}
	}
	return containsChild;
}

function createTable(api, method, filterData, root) {
	sendAPIRequest(api, method, filterData, function(response) {
		$("#container").html('');
		initDataTable(response[root]);
	});
}

function initDataTable(data) {
	var tableHtml = renderTable(data);
	$("#container").html(tableHtml);
	var mainTableElement = $("#datatable-main-" + window.datatableid);

	// Initialize DataTables, with no sorting on the 'details' column
	var mainTableConfig = {
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"aaSorting" : [ [ 1, 'asc' ] ]
	};
	if (hasChildTable(data)) {
		mainTableConfig["aoColumnDefs"] = [ {
			"bSortable" : false,
			"aTargets" : [ 0 ]
		} ];
	}
	var mainTable = mainTableElement.dataTable(mainTableConfig);

	mainTableElement.find('.table-expand-collapse').live(
			'click',
			function() {
				var index = $(this).data("index");

				var nTr = $(this).parents('tr')[0];
				if (mainTable.fnIsOpen(nTr)) {
					/* This row is already open - close it */
					this.src = "http://i.imgur.com/SD7Dz.png";
					mainTable.fnClose(nTr);
				} else {
					/* Open this row */
					this.src = "http://i.imgur.com/d4ICC.png";
					mainTable.fnOpen(nTr, renderChildTable(data[index], index),
							'details');
					childTable = $("table[id^='datatable-" + index + "']")
							.dataTable();
				}
			});

}

function sendAPIRequest(api, method, data, callback) {
	if(api.indexOf("invoice/{id}/accept") != -1) {
		var temp = JSON.parse(data);
		api = api.replace("{id}", temp.invoiceId);
	}
	if (callback == undefined) {
		callback = function() {
		};
	}
	$.ajax({
		url : BaseURL + api,
		type : method,
		data : data,
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		beforeSend : function(request) {
			request.setRequestHeader("REW3-UserId", $("#activeapiuserid").val());
		},
		success : function(response) {
			callback(response);
		}
	});
}
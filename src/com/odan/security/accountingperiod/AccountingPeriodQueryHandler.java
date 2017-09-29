package com.odan.security.accountingperiod;

import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.security.accountingperiod.model.AccountingPeriod;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class AccountingPeriodQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		AccountingPeriod ap = (AccountingPeriod) HibernateUtils.get(AccountingPeriod.class,
				Parser.convertObjectToLong(id));
		return ap;

	}

	@Override
	public List<Object> get(Query q) {
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

		offset = (limit * (page - 1));

		List<Object> aPeriods = HibernateUtils.select("FROM AccountingPeriod " + whereSQL, null, limit, offset);
		return aPeriods;
	}

	public Object getByTimestamp(Timestamp ts, Long ownerId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("date", ts);
		params.put("ownerId", ownerId);
		List<AccountingPeriod> accPeriods = HibernateUtils.select(
				"FROM AccountingPeriod WHERE :date BETWEEN startDate AND endDate AND ownerId = :ownerId", params);

		AccountingPeriod accp = null;
		if (accPeriods.size() > 0) {
			accp = accPeriods.get(0);
		}

		return accp;
	}

	public List<AccountingPeriod> getCloseable() {
		String whereSQL = " WHERE 1=1 ";
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();

		Timestamp ts = DateTime.getCurrentTimestamp();
		whereSQL += " AND endDate < :currentDate  AND status = :status ";
		sqlParams.put("currentDate", ts.toString());


		List<AccountingPeriod> accPeriods = HibernateUtils.select("FROM AccountingPeriod " + whereSQL, sqlParams);

		return accPeriods;
	}
}

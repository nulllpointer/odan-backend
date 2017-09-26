package com.odan.common.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.common.utils.PatternMatcher;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class HibernateUtils {

	public static void main(String[] args) {

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private HibernateUtils() {
		// for singleton use
	}

	private static SessionFactory hibernateSessionFactory = null;
	private static Session s = null;
	private static boolean debug = true;

	public static SessionFactory getSessionFactory() {
		try {
			if (hibernateSessionFactory == null) {
				configureSession();
			}
		} catch (HibernateException e) {
			if (debug) {
				System.err.println("Error in configuring Hibernate Session!");
			}
			e.printStackTrace();
		}

		return hibernateSessionFactory;
	}

	private static void configureSession() {
		Configuration cfg = new Configuration();
		cfg.configure();
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
		hibernateSessionFactory = cfg.buildSessionFactory(ssrb.build());
	}

	public static boolean isSessionOpen() {
		System.out.println("=> Check Session Open");
		return (s != null && s.isOpen());
	}

	public static Session openSession() {
		System.out.println("=> Get Session");
		SessionFactory sf = getSessionFactory();
		if (!HibernateUtils.isSessionOpen()) {
			if (debug) {
				System.out.println("=> Session Opened");
			}
			s = sf.openSession();
		}

		return s;
	}

	public static boolean closeSession() {
		boolean done = false;
		// if (!SyncMutex.hibernateIsLocked()) {
		if (s.isOpen()) {
			if (debug) {
				System.out.println("=> Session Closed");
			}
			s.close();
			done = true;
		}
		// }
		return done;
	}

	public static Transaction getTransaction() {
		if (debug) {
			System.out.println("=> Get Transaction");
		}
		Transaction trx = s.getTransaction();
		return trx;
	}

	public static Transaction beginTransaction() {
		System.out.println("Begin Transaction");
		Transaction trx = getTransaction();
		if (trx == null || !trx.isActive()) {
			if (debug) {
				System.out.println("=> New Transaction Created");
			}
			trx = s.beginTransaction();
		}

		return trx;
	}

	public static boolean rollbackTransaction(Transaction trx) {
		boolean done = false;
		// if (!SyncMutex.hibernateIsLocked()) {
		trx.rollback();
		done = true;
		// }

		if (debug) {
			System.out.println("Tried Rollback Transaction: " + done);
		}
		return done;
	}

	public static boolean commitTransaction(Transaction trx) {
		boolean done = false;
		// if (!SyncMutex.hibernateIsLocked()) {
		trx.commit();
		done = true;
		// }

		if (debug) {
			System.out.println("Tried Commit Transaction: " + done);
		}
		return done;
	}

	public static synchronized List select(String hql, Map<String, Object> params, Integer limit, Integer offset) {

		boolean isNewSession = !HibernateUtils.isSessionOpen();
		Session session = null;
		List results = null;
		try {
			session = openSession();
			Query query = createQuery(hql, params, limit, offset);
			results = query.list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (isNewSession) {
				closeSession();
			}
		}
		return results;
	}

	public static synchronized List select(String hql, Map<String, Object> params, Integer limit) {
		return select(hql, params, limit, null);
	}

	public static synchronized List select(String hql, Map<String, Object> params) {
		return select(hql, params, null, null);
	}

	public static synchronized List select(String hql) {
		return select(hql, null, null, null);
	}

	public static synchronized List selectSQL(String sql, Map<String, Object> params, Integer limit, Integer offset) {
		boolean isNewSession = !HibernateUtils.isSessionOpen();
		Session session = null;
		List results = null;
		try {
			session = openSession();
			Query query = createSQLQuery(sql, params, limit, offset);
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			results = query.list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (isNewSession) {
				closeSession();
			}
		}
		return results;
	}

	public static synchronized List selectSQL(String sql, Map<String, Object> params, Integer limit) {
		return selectSQL(sql, params, limit, null);
	}

	public static synchronized List selectSQL(String sql, Map<String, Object> params) {
		return selectSQL(sql, params, null, null);
	}

	public static synchronized List selectSQL(String sql) {
		return selectSQL(sql, null, null, null);
	}

	public static synchronized boolean query(String hql, Map<String, Object> params, Transaction aTrx) {
		boolean success = false;
		boolean isCommitable = false;
		if (aTrx == null) {
			isCommitable = true;
		}

		Session session = null;
		Transaction trx = aTrx;
		try {
			session = HibernateUtils.openSession();
			if (isCommitable) {
				trx = HibernateUtils.beginTransaction();
			}
			Query query = createQuery(hql, params, null, null);
			success = (query.executeUpdate() > 0);

			session.flush();
			if (isCommitable) {
				commitTransaction(trx);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (isCommitable) {
				rollbackTransaction(trx);
			}
			throw e;
		} finally {
			if (isCommitable) {
				closeSession();
			}
		}
		return success;
	}

	public static synchronized boolean query(String hql, Map<String, Object> params) {
		return query(hql, params, null);
	}

	public static synchronized Object save(Object obj) throws JsonProcessingException {
		return save(obj, null);
	}

	public static synchronized Object save(Object obj, Transaction aTrx) throws JsonProcessingException {

		boolean success = false;
		boolean isCommitable = false;
		if (aTrx == null) {
			isCommitable = true;
		}

		Session session = null;
		Transaction trx = aTrx;
		try {
			session = HibernateUtils.openSession();
			if (isCommitable) {
				trx = HibernateUtils.beginTransaction();
			}
			session.saveOrUpdate(obj);

			session.flush();

			if (isCommitable) {
				commitTransaction(trx);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (isCommitable) {
				rollbackTransaction(trx);
			}
			throw e;
		} finally {

			if (isCommitable) {
				closeSession();
			}
		}
		return obj;
	}

	public static synchronized boolean delete(Object obj, Transaction aTrx) {
		boolean success = false;
		boolean isCommitable = false;
		if (aTrx == null) {
			isCommitable = true;
		}

		Session session = null;
		Transaction trx = aTrx;
		try {
			session = HibernateUtils.openSession();
			if (isCommitable) {
				trx = HibernateUtils.beginTransaction();
			}

			session.delete(obj);
			session.flush();
			if (isCommitable) {
				commitTransaction(trx);
			}
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (isCommitable) {
				rollbackTransaction(trx);
			}
			throw e;
		} finally {

			if (isCommitable) {
				closeSession();
			}
		}
		return success;
	}

	public static synchronized boolean delete(Object obj) {
		return delete(obj, null);
	}

	public static synchronized Object get(Class cls, Long id) throws CommandException {
		boolean isNewSession = !HibernateUtils.isSessionOpen();
		Session session = null;
		Object obj = null;

		if (id != null) {
			try {
				session = openSession();
				obj = session.get(cls, id);
				AbstractEntity entity = (AbstractEntity) obj;


			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (isNewSession) {
					closeSession();
				}
			}
		}
		return obj;
	}

	public static Query createQuery(String hql, Map<String, Object> params, Integer limit, Integer offset)
			throws HibernateException {
		Session session = openSession();

		Query query = session.createQuery(hql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		if (offset != null) {
			query.setFirstResult(offset);
		}
		return query;
	}

	public static SQLQuery createSQLQuery(String sql, Map<String, Object> params, Integer limit, Integer offset)
			throws HibernateException {
		Session session = openSession();
		SQLQuery query = session.createSQLQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		if (offset != null) {
			query.setFirstResult(offset);
		}
		return query;

	}

	public static String s(String value) {
		String quote = "'";
		String formatted = String.format("'%s'", value.replaceAll(quote, quote + quote));
		return formatted;
	}

	public static List<Object> select(String hql, HashMap<String, Object> params, HashMap<String, String> filterParams, HashMap<String, Object> requestParams, int limit, int offset) {
		boolean isNewSession = !HibernateUtils.isSessionOpen();
		Session session = null;
		List<Object> results = null;

		try {
			session = openSession();

			hql = filter(hql, filterParams, requestParams, params);

			Query query = createQuery(hql, params, limit, offset);
			results = query.list();



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (isNewSession) {
				closeSession();
			}
		}
		return results;
	}

	private static String filter(String hql, HashMap<String, String> filterParams, HashMap<String, Object> requestParams, HashMap<String, Object> sqlParams) {
		for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
			String s = (String) entry.getValue();

			String key = entry.getKey();


			Matcher numberRangeMatcher = PatternMatcher.numberRangeMatch(s);
			if (numberRangeMatcher.find()) {
				hql += " AND " + key + " BETWEEN :value1 AND :value2";
				sqlParams.put("value1", Double.parseDouble(numberRangeMatcher.group(1)));
				sqlParams.put("value2", Double.parseDouble(numberRangeMatcher.group(2)));


			}
			Matcher startsWithMatcher = PatternMatcher.startsWithMatch(key);
			if (startsWithMatcher.find()) {
				String field = startsWithMatcher.group(1);
				hql += " AND " + field + " LIKE " + HibernateUtils.s(s + "%");
			}

			Matcher endsWithMatcher = PatternMatcher.endsWithMatch(key);
			if (endsWithMatcher.find()) {
				String field = endsWithMatcher.group(1);

				hql += " AND " + field + " LIKE" + HibernateUtils.s("%" + s);
				;

			}

			Matcher containsMatcher = PatternMatcher.containsMatch(key);
			if (containsMatcher.find()) {
				String field = containsMatcher.group(1);
				hql += " AND " + field + " LIKE " + HibernateUtils.s("%" + s + "%");
			}

			Matcher notContainMatcher = PatternMatcher.notContainMatch(key);
			if (notContainMatcher.find()) {

				String field = notContainMatcher.group(1);
				hql += " AND " + field + " NOT LIKE " + HibernateUtils.s("%" + s + "%");
			}
			Matcher specificDateMatcher = PatternMatcher.specificDateMatch(s);
			if (specificDateMatcher.find()) {

				String val = specificDateMatcher.group(1);
				System.out.println(val);
				Flags.SpecificDateType type = Flags.SpecificDateType.valueOf(val.toUpperCase());

				Timestamp startSpecificDate = DateTime.getStartTimestamp(type);
				Timestamp endSpecificDate = DateTime.getEndTimestamp(type);

				hql += " AND " + key + " BETWEEN " + HibernateUtils.s(startSpecificDate.toString()) + " AND " + HibernateUtils.s(endSpecificDate.toString());

			}

			Matcher isNotEqualMatcher = PatternMatcher.isNotEqualMatch(s);
			if (isNotEqualMatcher.find()) {
				String val = isNotEqualMatcher.group(2);


				hql += " AND " + key + " != " + val;
				sqlParams.put("value3", endsWithMatcher.group(1));
				sqlParams.put("value4", endsWithMatcher.group(2));
			}


			Matcher emptyMatcher = PatternMatcher.emptyMatch(key);
			if (emptyMatcher.find()) {
				String field = emptyMatcher.group(1);
				hql += " AND " + field + " is  NULL";
				// hql += " AND " + field + " LIKE " + HibernateUtils.s("%" + s + "%");
			}
			Matcher nonEmptyMatcher = PatternMatcher.nonEmptyMatch(key);
			if (nonEmptyMatcher.find()) {
				String field = nonEmptyMatcher.group(1);
				hql += " AND " + field + " is NOT NULL";
				// hql += " AND " + field + " LIKE " + HibernateUtils.s("%" + s + "%");
			}


			Matcher dateRangeMatcher = PatternMatcher.dateRangeMatch(s);
			if (dateRangeMatcher.find()) {
				hql += " AND " + key + " BETWEEN date(:value1) AND date(:value2)";
				sqlParams.put("value1", dateRangeMatcher.group(1));
				sqlParams.put("value2", dateRangeMatcher.group(2));
			}
			Matcher keyMatcher = PatternMatcher.keyMatch(key);
			Matcher dateMatcher = PatternMatcher.dateFormatMatch(s);
			if (keyMatcher.find()) {
				if (dateMatcher.find()) {
					String field = keyMatcher.group(1);
					String operator = keyMatcher.group(2);
					String queryDate = dateMatcher.group(1);
					String op = Parser.getOperator(operator);
					hql += " AND " + field + op + "date(" + HibernateUtils.s(queryDate) + ")";

				}
			}
		}

		// hql = filterEmptyOrNonEmpty(hql, requestParams.keySet());

		hql = sort(hql, filterParams);

		return hql;
	}

	private static String sort(String hql, HashMap<String, String> filterParams) {
		if (filterParams.containsKey("sort")) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			try {
				map = mapper.readValue((String) filterParams.get("sort"), new TypeReference<Map<String, String>>() {
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (map != null) {
				hql += "ORDER BY ";
				int count = map.size();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if (entry.getValue().equals("1")) {
						hql += entry.getKey() + " ASC";

					}
					if (entry.getValue().equals("-1")) {

						hql += entry.getKey() + " DESC";
					}
					if (count > 1) {
						hql += ",";
					}
					count--;

				}
			}
		}
		return hql;
	}

	private static String filterEmptyOrNonEmpty(String hql, Set<String> keys) {
		if (keys != null) {

			for (String s : keys) {
				if (s.contains("-non-empty")) {
					String value = s.split("-non-empty")[0];
					hql += " AND " + value + " is NOT NULL";
					break;
				}
				if (s.contains("-empty")) {
					String value = s.split("-empty")[0];
					hql += " AND " + value + " is NULL";
					break;
				}
			}
		}
		return hql;
	}

	public static Object createQuery(String hql, Map<String, Object> params)
			throws HibernateException {
		Session session = openSession();

		Query query = session.createQuery(hql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

		}

		return query.uniqueResult();
	}

}

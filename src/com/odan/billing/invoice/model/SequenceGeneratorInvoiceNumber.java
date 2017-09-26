package com.odan.billing.invoice.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.odan.common.database.HibernateUtils;

public class SequenceGeneratorInvoiceNumber implements IdentifierGenerator {
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		System.out.println("GENERATOR STARTED");
		String prefix = "10";
		Connection connection = session.connection();
		try {

			PreparedStatement ps = connection.prepareStatement("SELECT nextval ('invoice_number') as nextval");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				long id = rs.getLong("nextval");
				String number = prefix + StringUtils.leftPad("" + id, 8, '0');
				System.out.println("Generated invoice number: " + number);
				HibernateUtils.closeSession();
				return number;
			}

		} catch (SQLException e) {
			System.out.println(e);
			throw new HibernateException("Unable to generate Invoice Number Sequence");
		}
		return null;
	}

}
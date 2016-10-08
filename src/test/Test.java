package test;

import java.math.BigDecimal;
import java.util.Date;

public class Test
{

	private int test_id;
	private String test_varchar;
	private Date test_date;
	private BigDecimal test_numeric;
	private String test_char;

	public Test()
	{
	}

	public Test( int test_id, String test_varchar, Date test_date, BigDecimal test_numeric, String test_char )
	{
		this.test_id = test_id;
		this.test_varchar = test_varchar;
		this.test_date = test_date;
		this.test_numeric = test_numeric;
		this.test_char = test_char;
	}

	public int getTest_id()
	{
		return test_id;
	}

	public void setTest_id( int test_id )
	{
		this.test_id = test_id;
	}

	public String getTest_varchar()
	{
		return test_varchar;
	}

	public void setTest_varchar( String test_varchar )
	{
		this.test_varchar = test_varchar;
	}

	public Date getTest_date()
	{
		return test_date;
	}

	public void setTest_date( Date test_date )
	{
		this.test_date = test_date;
	}

	public BigDecimal getTest_numeric()
	{
		return test_numeric;
	}

	public void setTest_numeric( BigDecimal test_numeric )
	{
		this.test_numeric = test_numeric;
	}

	public String getTest_char()
	{
		return test_char;
	}

	public void setTest_char( String test_char )
	{
		this.test_char = test_char;
	}

}

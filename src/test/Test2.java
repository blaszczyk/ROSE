package test;

import javax.persistence.*;


@Entity
@Table(name="TEST2")
public class Test2
{

	private int test2_id;
	private String vc;
	private int iii;

	public Test2()
	{
	}

	public Test2( String vc, int iii )
	{
		this.vc = vc;
		this.iii = iii;
	}

	@Id
	@GeneratedValue
	@Column(name="TEST2_ID")
	public int getTest2_id()
	{
		return test2_id;
	}

	public void setTest2_id( int test2_id )
	{
		this.test2_id = test2_id;
	}

	@Column(name="VC")
	public String getVc()
	{
		return vc;
	}

	public void setVc( String vc )
	{
		this.vc = vc;
	}

	@Column(name="iii")
	public int getIii()
	{
		return iii;
	}

	public void setIii( int iii )
	{
		this.iii = iii;
	}

}

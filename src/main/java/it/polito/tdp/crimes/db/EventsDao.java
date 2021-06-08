package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;

public class EventsDao
{

	public List<Event> listAllEvents()
	{
		String sql = "SELECT * FROM events";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getCategories()
	{
		String sql = "SELECT DISTINCT(e.offense_category_id) as cat "
				+ "FROM denver_crimes.`events` AS e "
				+ "ORDER BY (e.offense_category_id)";
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			List<String> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(res.getString("cat"));
				}
				catch (Throwable t)
				{
					throw new RuntimeException("ERRORE DATABASE" + t);
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<LocalDate> getDate()
	{
		String sql = "SELECT DISTINCT DATE(e.reported_date) AS date "
				+ "FROM denver_crimes.`events` AS e "
				+ "ORDER BY e.reported_date ";
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			List<LocalDate> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(res.getDate("date").toLocalDate());
				}
				catch (Throwable t)
				{
					throw new RuntimeException("ERRORE DATABASE" + t);
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getVertici(String category, LocalDate data)
	{
		String sql = "SELECT DISTINCT ( e.offense_type_id ) AS tipo "
					+ "FROM denver_crimes.`events` AS e "
					+ "WHERE e.offense_category_id = ? "
					+ "AND DATE(e.reported_date) = ? ";

		List<String> list = new ArrayList<>();
		
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setString(2, data.toString());

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					if(!list.contains(res.getString("tipo")))
						list.add(res.getString("tipo")); 
				}
				catch (Throwable t)
				{
					throw new RuntimeException("ERRORE DATABASE" + t);
				}
			}

			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String category, LocalDate data)
	{
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(*) AS peso "
					+ "FROM denver_crimes.`events` AS e1, denver_crimes.`events` AS e2 "
					+ "WHERE e1.offense_type_id > e2.offense_type_id "
					+ "		AND e1.precinct_id = e2.precinct_id "
					+ "		AND e1.offense_category_id = ? "
					+ "		AND e1.offense_category_id = e2.offense_category_id "
					+ "		AND DATE(e1.reported_date) = DATE(e2.reported_date) "
					+ "		AND DATE(e1.reported_date) = ? "
					+ "GROUP BY e1.offense_type_id, e2.offense_type_id ";
		
		List<Adiacenza> list = new ArrayList<>();
		
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setString(2, data.toString());
			
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					Adiacenza a = new Adiacenza(res.getString("id1"), res.getString("id2"), res.getInt("peso"));
					if(!list.contains(a))
						list.add(a); 
				}
				catch (Throwable t)
				{
					throw new RuntimeException("ERRORE DATABASE" + t);
				}
			}
			
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

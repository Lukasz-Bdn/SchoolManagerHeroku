package pl.schoolmanager.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Controller("/")
public class HomeController {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	
	@GetMapping("/")
	public String home() {
		return "home/home";
	}

	@GetMapping("login")
	public String loginGet() {
		return "home/login";
	}

	@PostMapping("login")
	public String loginPost() {
		return "redirect:/";
	}

	@GetMapping("403")
	public String accessDenied() {
		return "errors/accessDenied";
	}
	
	
	@RequestMapping("/db")
	String db(Map<String, Object> model, Model m) {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS names (name varchar(80))");
			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
			stmt.executeUpdate("INSERT INTO names VALUES ('Lukasz')");
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
			ResultSet rs2 = stmt.executeQuery("SELECT name FROM names");

			ArrayList<String> output = new ArrayList<String>();
			while (rs.next()) {
				output.add("Read from DB: " + rs.getTimestamp("tick"));
				System.out.println(rs.getTimestamp("tick"));
			}

			ArrayList<String> output2 = new ArrayList<String>();
			while (rs.next()) {
				output2.add("Read from DB: " + rs.getTimestamp("tick"));
				System.out.println(rs.getString("name") );
			}

			
			model.put("records", output);
			m.addAttribute("results", output);
			
			return "home/db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "home/error";
		}
	}
	
}
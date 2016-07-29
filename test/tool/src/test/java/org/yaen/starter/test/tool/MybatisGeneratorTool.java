package org.yaen.starter.test.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * mybatis generator
 * 
 * @author Yaen 2016年7月29日下午1:40:36
 */
public class MybatisGeneratorTool {

	public static void main(String[] args) {
		try {
			System.out.println("start generator ...");
			List<String> warnings = new ArrayList<String>();
			boolean overwrite = true;
			File configFile = new File(
					MybatisGeneratorTool.class.getResource("classpath:/MybatisGeneratorConfig.xml").getFile());
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(configFile);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			System.out.println("end generator!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLParserException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

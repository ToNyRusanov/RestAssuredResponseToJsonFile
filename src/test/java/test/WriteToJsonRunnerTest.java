package test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
				glue = "stepDef",
				tags = "@jenkins",
				plugin = {"pretty","html:target/cucumber-report"})
public class WriteToJsonRunnerTest {
	//if you want to run this test runner with maven, always end the class name with test

}

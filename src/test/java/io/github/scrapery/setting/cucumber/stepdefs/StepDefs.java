package io.github.scrapery.setting.cucumber.stepdefs;

import io.github.scrapery.setting.ScraperSettingApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ScraperSettingApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
